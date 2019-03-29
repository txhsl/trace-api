package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/3/29
 * @description: none
 */

public class Message {
    private PermissionSwapper permission;
    private String to;
    private boolean isAccepted;
    private boolean isRead;

    public Message() {

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
}
