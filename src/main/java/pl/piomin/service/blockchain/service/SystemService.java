package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
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

        try {
            LOGGER.info("Blockchain height: " + web3j.ethBlockNumber().send().getBlockNumber() + ". Connected!");
        } catch (Exception e){
            LOGGER.error("Connection failed. " + e.getMessage());
        }
    }

    public String recover() {
        try {
            Resource resource = new ClassPathResource("system.json");
            JSONObject json = new JSONObject(new String(FileCopyUtils.copyToByteArray(resource.getFile())));
            return json.getString("address");
        } catch (Exception e) {
            LOGGER.error("Address of System Contract not found. " + e.getMessage());
        }
        return null;
    }

    public String reset(Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT).send();
        LOGGER.info("System Contract deployed: " + system.getContractAddress());

        return system.getContractAddress();
    }

    public TransactionReceipt addRC(String sysAddr, String name, Address rcAddr, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = system.setIndex(new Utf8String(name), rcAddr).send();

        return transactionReceipt;
    }

    public TransactionReceipt setRC(String sysAddr, String userAddr, String roleName, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = system.register(new Address(userAddr), new Utf8String(roleName)).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public Address getRC(String sysAddr, Credentials credentials) throws Exception {
        return getRC(sysAddr, credentials.getAddress(), credentials);
    }

    public Address getRC(String sysAddr, String userAddr, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String rcAddr = system.getRC(new Address(userAddr)).send().getValue();

        LOGGER.info("Read succeed: " + rcAddr);
        return new Address(rcAddr);
    }
}
