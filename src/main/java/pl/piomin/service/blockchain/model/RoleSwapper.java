package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/3/21
 * @description: none
 */
public class RoleSwapper {
    private String name;
    private String address;
    private boolean permitted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getPermitted() {
        return permitted;
    }

    public void setPermitted(boolean permitted) {
        this.permitted = permitted;
    }
}