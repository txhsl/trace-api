package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
public class User {

    private String name;
    private int role;
    private String IPAddress;
    private String address;

    public User() {

    }

    public User(String name, int role, String ip, String address) {
        this.name = name;
        this.role = role;
        this.IPAddress = ip;
        this.address = address;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
