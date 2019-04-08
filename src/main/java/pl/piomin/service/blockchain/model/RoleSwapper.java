package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/3/21
 * @description: none
 */
public class RoleSwapper {
    private String name;
    private String address;

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

    public boolean hasAddress() {
        return address != null;
    }
}
