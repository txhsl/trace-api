package pl.piomin.service.blockchain.model;

import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * @author: HuShili
 * @date: 2019/3/29
 * @description: none
 */

public class Message {
    private PermissionSwapper permission;
    private String to;
    private RemoteCall<TransactionReceipt> request;
    private TransactionReceipt receipt;
    private boolean isAccepted;
    private boolean isRead;

    public Message(PermissionSwapper permissionSwapper) {
        this.permission = permissionSwapper;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public RemoteCall<TransactionReceipt> getRequest() {
        return request;
    }

    public void setRequest(RemoteCall<TransactionReceipt> request) {
        this.request = request;
    }

    public void setReceipt(TransactionReceipt receipt) {
        this.receipt = receipt;
    }

    public TransactionReceipt getReceipt() {
        return receipt;
    }
}
