package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/2/20
 * @description: none
 */
public class PermissionSwapper {

    private String propertyName;
    private boolean isRead;
    private String target;

    public PermissionSwapper() {

    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }
}
