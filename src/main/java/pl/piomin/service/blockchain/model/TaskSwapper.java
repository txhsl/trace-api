package pl.piomin.service.blockchain.model;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.CompletableFuture;

/**
 * @author: HuShili
 * @date: 2019/3/22
 * @description: none
 */
public class TaskSwapper {
    private String taskName;
    private String taskContent;
    private CompletableFuture<TransactionReceipt> future;
    private TransactionReceipt receipt;

    public TaskSwapper(String taskName) {
        this.taskName = taskName;
    }

    public TaskSwapper(String taskName, String taskContent) {
        this.taskName = taskName;
        this.taskContent = taskContent;
    }

    public TaskSwapper() {}

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public CompletableFuture<TransactionReceipt> getFuture() {
        return future;
    }

    public void setFuture(CompletableFuture<TransactionReceipt> future) {
        this.future = future;
    }

    public TransactionReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(TransactionReceipt receipt) {
        this.receipt = receipt;
    }
}
