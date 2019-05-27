package pl.piomin.service.blockchain.service;

import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public BlockchainService(Web3j web3j) {
        this.web3j = web3j;
    }

    public RemoteCall<TransactionReceipt> transfer(String target, int amount, Credentials credentials) throws IOException, TransactionException, InterruptedException {
        return Transfer.sendFunds(web3j, credentials, target, BigDecimal.valueOf(amount), Convert.Unit.ETHER);
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
        return Convert.fromWei(wei.toString(), Convert.Unit.ETHER).doubleValue();
    }

    public boolean subscribeContract(String address) throws InterruptedException {
        cache.put(address, new ArrayList<>());

        Disposable sub = web3j.replayPastAndFutureTransactionsFlowable(DefaultBlockParameterName.EARLIEST)
                .subscribe(tx -> {
                    if (tx.getTo() != null && tx.getTo().equals(address)) {
                        cache.get(address).add(tx);
                    }
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
