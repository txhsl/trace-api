package pl.piomin.service.blockchain.service;

import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;
import pl.piomin.service.blockchain.model.BlockchainTransaction;
import pl.piomin.service.blockchain.model.TaskSwapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

@Service
public class BlockchainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainService.class);

    private final Web3j web3j;
    private final int RECEIENT = 7;

    private ArrayList<TaskSwapper> pendingTx = new ArrayList<>();
    private ArrayList<TaskSwapper> completedTx = new ArrayList<>();
    private ArrayList<TaskSwapper> errorTx = new ArrayList<>();

    public BlockchainService(Web3j web3j) {
        this.web3j = web3j;
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

    public ArrayList<org.web3j.protocol.core.methods.response.Transaction> getUserHistory(String address) throws InterruptedException {
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

    public ArrayList<org.web3j.protocol.core.methods.response.Transaction> getContractHistory(String address) throws InterruptedException {
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

    public void addPending(TaskSwapper future) {
        pendingTx.add(future);
    }

    public ArrayList<TaskSwapper> getCompleted() {
        checkCompleted();
        return completedTx;
    }
    public int[] getCompleted(String[] addresses) {
        int[] result = new int[addresses.length];
        int i = 0;
        for (String address : addresses) {
            result[i++] = completedTx.stream().filter(tx -> tx.getTaskSender().equals(address)).toArray(TaskSwapper[]::new).length;
        }
        return result;
    }
    public int[] getCompleted(int height) {
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

    public ArrayList<TaskSwapper> getPending() {
        checkCompleted();
        return pendingTx;
    }

    public ArrayList<TaskSwapper> getErrorTx() {
        return errorTx;
    }

    private void checkCompleted() {
        ArrayList<TaskSwapper> cached = new ArrayList<>();

        for (TaskSwapper pending : pendingTx) {
            if (pending.getFuture().isDone()) {
                cached.add(pending);
                try {
                    pending.setReceipt(pending.getFuture().get());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    errorTx.add(pending);
                }
                completedTx.add(pending);
            }
        }

        for (TaskSwapper completed : cached) {
            pendingTx.remove(completed);
        }
    }

    public int getHight() throws IOException {
        return web3j.ethBlockNumber().send().getBlockNumber().intValue();
    }
}
