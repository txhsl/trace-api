package pl.piomin.service.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.CompletableFuture;

/**
 * @author: HuShili
 * @date: 2019/3/29
 * @description: none
 */

public class Message {
    private PermissionSwapper permission;
    private String to;
    private String time;
    @JsonIgnore
    private RemoteCall<TransactionReceipt> request;
    @JsonIgnore
    private CompletableFuture<TransactionReceipt> receipt;
    private boolean isAccepted;
    private boolean isRead;

    public Message(PermissionSwapper permissionSwapper, RemoteCall<TransactionReceipt> request,
                   String to, String time) {
        this.permission = permissionSwapper;
        this.request = request;
        this.to = to;
        this.time = time;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public PermissionSwapper getPermission() {
        return permission;
    }

    public void setPermission(PermissionSwapper permission) {
        this.permission = permission;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public RemoteCall<TransactionReceipt> getRequest() {
        return request;
    }

    public void setRequest(RemoteCall<TransactionReceipt> request) {
        this.request = request;
    }

    public void setReceipt(CompletableFuture<TransactionReceipt> receipt) {
        this.receipt = receipt;
    }

    public CompletableFuture<TransactionReceipt> getReceipt() {
        return receipt;
    }

}
