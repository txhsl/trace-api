package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/7/8
 * @description: none
 */
public class NewContractSwapper {
    private String name;
    private String admin;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
