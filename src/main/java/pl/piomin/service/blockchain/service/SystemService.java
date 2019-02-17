package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.contract.System_sol_System;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/17
 * @description: none
 */
@Service
public class SystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemService.class);

    private final Web3j web3j;

    public SystemService(Web3j web3j) {
        this.web3j = web3j;
    }

    public Address reset(){
        return null;
    }

    public TransactionReceipt register(Address sysAddr, Address rcAddr, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(sysAddr.toString(), web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = system.register(rcAddr).send();
        return transactionReceipt;
    }

    public Address getSC(Address sysAddr, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(sysAddr.toString(), web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String rcAddr = system.getRC(new Address(credentials.getAddress())).send().getValue();
        return new Address(rcAddr);
    }
}
