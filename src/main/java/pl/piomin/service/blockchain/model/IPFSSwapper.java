package pl.piomin.service.blockchain.model;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.CompletableFuture;

/**
 * @author: HuShili
 * @date: 2019/3/22
 * @description: none
 */
public class IPFSSwapper {
    private String fileName;
    private String fileHash;
    private CompletableFuture<TransactionReceipt> future;
    private TransactionReceipt receipt;

    public IPFSSwapper(String fileName, String fileHash) {
        this.fileHash = fileHash;
        this.fileName = fileName;
    }

    public IPFSSwapper() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
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
