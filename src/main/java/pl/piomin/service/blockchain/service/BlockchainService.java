package pl.piomin.service.blockchain.service;

import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import pl.piomin.service.blockchain.model.BlockchainTransaction;
import pl.piomin.service.blockchain.model.TaskSwapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Component
@EnableScheduling
@Service
public class BlockchainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainService.class);

    private final Web3j web3j;
    private static final int RECEIENT = 7;

    private static ArrayList<TaskSwapper> pendingTx = new ArrayList<>();
    private static ArrayList<TaskSwapper> completedTx = new ArrayList<>();
    private static ArrayList<TaskSwapper> errorTx = new ArrayList<>();

    private Map<String, ArrayList<org.web3j.protocol.core.methods.response.Transaction>> cache = new HashMap<>();
    private Map<String, Disposable> listener = new HashMap<>();

    // Event definition
    private static final Event NEW_USER = new Event("user", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}));
    private static final Event NEW_ROLE = new Event("role", Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    private static final Event NEW_PROPERTY = new Event("data", Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    private static final Event ARBITRATE = new Event("transfer", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint8>(false) {}));
    private static final Event PERMIT_READER = new Event("manage", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Address>(true) {}));
    private static final Event PERMIT_WRITER = new Event("own", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Address>(true) {}));
    private static final Event WRITE = new Event("user", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>(false) {}));

    // Event definition hash
    private static final String NEW_USER_HASH = EventEncoder.encode(NEW_USER);
    private static final String NEW_ROLE_HASH = EventEncoder.encode(NEW_ROLE);
    private static final String NEW_PROPERTY_HASH = EventEncoder.encode(NEW_PROPERTY);
    private static final String ARBITRATE_HASH = EventEncoder.encode(ARBITRATE);
    private static final String PERMIT_READER_HASH = EventEncoder.encode(PERMIT_READER);
    private static final String PERMIT_WRITER_HASH = EventEncoder.encode(PERMIT_WRITER);
    private static final String WRITE_HASH = EventEncoder.encode(WRITE);

    public BlockchainService(Web3j web3j) {
        this.web3j = web3j;
    }

    public RemoteCall<TransactionReceipt> transfer(String target, int amount, Credentials credentials) throws IOException, TransactionException, InterruptedException {
        return Transfer.sendFunds(web3j, credentials, target, BigDecimal.valueOf(amount), Convert.Unit.WEI);
    }

    public BlockchainTransaction process(BlockchainTransaction trx) throws IOException {

        EthAccounts accounts = web3j.ethAccounts().send();
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(accounts.getAccounts().get(trx.getFromId()), DefaultBlockParameterName.LATEST).send();

        Transaction transaction = Transaction.createEtherTransaction(
                accounts.getAccounts().get(trx.getFromId()), transactionCount.getTransactionCount(), BigInteger.valueOf(trx.getValue()),
                BigInteger.valueOf(21_000), accounts.getAccounts().get(trx.getToId()), BigInteger.valueOf(trx.getValue()));

        EthSendTransaction response = web3j.ethSendTransaction(transaction).send();

        if (response.getError() != null) {
            trx.setAccepted(false);
            LOGGER.info("Tx rejected: {}", response.getError().getMessage());
            return trx;
        }

        trx.setAccepted(true);
        String txHash = response.getTransactionHash();
        LOGGER.info("Tx hash: {}", txHash);

        trx.setId(txHash);
        EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash).send();

        receipt.getTransactionReceipt().ifPresent(transactionReceipt -> LOGGER.info("Tx receipt:  {}", transactionReceipt.getCumulativeGasUsed().intValue()));

        return trx;

    }

    public double getBalance(String address) throws IOException {
        BigInteger wei =  web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send().getBalance();
        double balance = Convert.fromWei(wei.toString(), Convert.Unit.ETHER).doubleValue();
        LOGGER.info("Balance read: " + balance);
        return balance;
    }

    public boolean subscribeContract(String sysAddr, String address) {
        cache.put(address, new ArrayList<>());

        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, sysAddr);

        // Pull all the events for this contract
        Disposable sub = web3j.ethLogFlowable(filter).subscribe(log -> {
            String eventHash = log.getTopics().get(0); // Index 0 is the event definition hash

            if(eventHash.equals(PERMIT_READER_HASH) || eventHash.equals(PERMIT_WRITER_HASH) ||
                eventHash.equals(WRITE_HASH)) {
                Address addr = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {});
                if (addr.toString().equals(address)) {
                    String txHash = log.getTransactionHash();
                    web3j.ethGetTransactionByHash(txHash).send().getTransaction().ifPresent(tx -> cache.get(address).add(tx));
                }
            }
            else if(eventHash.equals(NEW_PROPERTY_HASH) || eventHash.equals(NEW_ROLE_HASH) ||
                eventHash.equals(NEW_USER_HASH) || eventHash.equals(ARBITRATE_HASH)) {
                if (!cache.keySet().contains(sysAddr)) {
                    cache.put(sysAddr, new ArrayList<>());
                }
                String txHash = log.getTransactionHash();
                web3j.ethGetTransactionByHash(txHash).send().getTransaction().ifPresent(tx -> cache.get(sysAddr).add(tx));
            }

            /*if(eventHash.equals(NEW_USER_HASH)) {
                Address user = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {});
                Utf8String roleName = (Utf8String) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(2), new TypeReference<Utf8String>() {});
            }
            else if(eventHash.equals(NEW_ROLE_HASH)) {
                Utf8String roleName = (Utf8String) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Utf8String>() {});
                Address rcAddr = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(2), new TypeReference<Address>() {});
                Address admin = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(3), new TypeReference<Address>() {});
            }
            else if(eventHash.equals(NEW_PROPERTY_HASH)) {
                Utf8String propertyName = (Utf8String) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Utf8String>() {});
                Address scAddr = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(2), new TypeReference<Address>() {});
                Address admin = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(3), new TypeReference<Address>() {});
            }
            else if(eventHash.equals(ARBITRATE_HASH)) {
                Address from = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {});
                Address to = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(2), new TypeReference<Address>() {});
                Uint8 amount = (Uint8) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(3), new TypeReference<Uint8>() {});
            }

            else if(eventHash.equals(PERMIT_READER_HASH)) {
                Address rcAddr = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {});
                Utf8String propertyName = (Utf8String) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(2), new TypeReference<Utf8String>() {});
                Address scAddr = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(3), new TypeReference<Address>() {});
            }
            else if(eventHash.equals(PERMIT_WRITER_HASH)) {
                Address rcAddr = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {});
                Utf8String propertyName = (Utf8String) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(2), new TypeReference<Utf8String>() {});
                Address scAddr = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(3), new TypeReference<Address>() {});
            }

            else if(eventHash.equals(WRITE_HASH)) {
                Address scAddr = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {});
                Utf8String id = (Utf8String) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(2), new TypeReference<Utf8String>() {});
                Utf8String data = (Utf8String) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(3), new TypeReference<Utf8String>() {});
            }*/
        });

        listener.putIfAbsent(address, sub);
        return true;
    }

    public Map<String, ArrayList<org.web3j.protocol.core.methods.response.Transaction>> getSubscribe() {
        return cache;
    }

    public boolean unsubscribeContract(String address) {
        try {
            listener.get(address).dispose();
            listener.remove(address);
            cache.remove(address);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return false;
        }
    }

    public ArrayList<org.web3j.protocol.core.methods.response.Transaction> getFromHistory(String address) throws InterruptedException {
        ArrayList<org.web3j.protocol.core.methods.response.Transaction> result = new ArrayList<>();
        Disposable sub = web3j.replayPastTransactionsFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
            .subscribe(tx -> {
                if (tx.getFrom().equals(address) && tx.getTo() != null) {
                    result.add(tx);
                }
            });
        while(!sub.isDisposed()){
            Thread.sleep(1000);
        }
        return result;
    }

    public ArrayList<org.web3j.protocol.core.methods.response.Transaction> getToHistory(String address) throws InterruptedException {
        ArrayList<org.web3j.protocol.core.methods.response.Transaction> result = new ArrayList<>();
        Disposable sub = web3j.replayPastTransactionsFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(tx -> {
                    if (tx.getTo() != null && tx.getTo().equals(address)) {
                        result.add(tx);
                    }
                });
        while(!sub.isDisposed()){
            Thread.sleep(1000);
        }
        return result;
    }

    public static void addPending(TaskSwapper future) {
        pendingTx.add(future);
    }

    public static ArrayList<TaskSwapper> getCompleted() {
        return completedTx;
    }
    public static int[] getCompleted(String[] addresses) {
        int[] result = new int[addresses.length];
        int i = 0;
        for (String address : addresses) {
            result[i++] = completedTx.stream().filter(tx -> tx.getTaskSender().equals(address)).toArray(TaskSwapper[]::new).length;
        }
        return result;
    }
    public static int[] getCompleted(int height) {
        int[] result = new int[RECEIENT];
        int[] heights = new int[RECEIENT];
        for(int j = 0; j < RECEIENT; j++) {
            heights[j] = height - RECEIENT + j + 1;
        }

        int i = 0;
        for (int blockNum : heights){
            result[i++] = completedTx.stream().filter(tx -> tx.getReceipt().getBlockNumber().intValue() == blockNum).toArray(TaskSwapper[]::new).length;
        }
        return result;
    }

    public static ArrayList<TaskSwapper> getPending() {
        return pendingTx;
    }

    public static ArrayList<TaskSwapper> getErrorTx() {
        return errorTx;
    }

    @Scheduled(fixedDelay = 5000)
    private void checkCompleted() {
        ArrayList<TaskSwapper> cached = new ArrayList<>();

        for (TaskSwapper pending : pendingTx) {
            if (pending.getFuture().isDone()) {
                cached.add(pending);
                try {
                    pending.setReceipt(pending.getFuture().get());
                    completedTx.add(pending);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    errorTx.add(pending);
                }
            }
        }

        for (TaskSwapper completed : cached) {
            pendingTx.remove(completed);
        }
    }

    public boolean checkDuplicated(String taskName) {
        for (TaskSwapper pending : pendingTx) {
            if (pending.getTaskName().equals(taskName)) {
                return true;
            }
        }
        return false;
    }

    public int getHight() throws IOException {
        return web3j.ethBlockNumber().send().getBlockNumber().intValue();
    }
}
