package pl.piomin.service.blockchain.model;

import org.web3j.abi.datatypes.Address;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
public class User {

    private Address address;
    private String password;

    public User() {

    }

    public User(Address address, String password) {
        this.address = address;
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
