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
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.contract.System_sol_System;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
    private String sysAddress;

    private final int REQUEST_LIMIT = 10;
    private final Web3j web3j;

    public SystemService(Web3j web3j) {
        this.web3j = web3j;

        try {
            LOGGER.info("Blockchain height: " + web3j.ethBlockNumber().send().getBlockNumber() + ". Connected!");
        } catch (Exception e) {
            LOGGER.error("Connection failed. " + e.getMessage());
        }

        sysAddress = recover();
    }

    private String recover() {
        try {
            Resource resource = new ClassPathResource("system.json");
            JSONObject json = new JSONObject(new String(FileCopyUtils.copyToByteArray(resource.getFile())));
            return json.getString("address");
        } catch (Exception e) {
            LOGGER.error("Address of System Contract not found. " + e.getMessage());
        }
        return null;
    }

    public void reset(Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT).send();
        LOGGER.info("System Contract deployed: " + system.getContractAddress());

        this.sysAddress = system.getContractAddress();
    }

    public TransactionReceipt addRC(String name, Address rcAddr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.setRcIndex(new Utf8String(name), rcAddr).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> addRCAsync(String name, Address rcAddr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return system.setRcIndex(new Utf8String(name), rcAddr).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt addSC(String name, Address scAddr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.setScIndex(new Utf8String(name), scAddr).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> addSCAsync(String name, Address scAddr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return system.setScIndex(new Utf8String(name), scAddr).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt setRC(String userAddr, String roleName, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.register(new Address(userAddr), new Utf8String(roleName)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getRC(Credentials credentials) throws Exception {
        return getRC(credentials.getAddress(), credentials);
    }

    public String getRC(String userAddr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String rcAddr = system.getRC(new Address(userAddr)).send().getValue();

                LOGGER.info("Read succeed: " + rcAddr);
                return rcAddr;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getSC(String propertyName, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String scAddr = system.getScIndex(new Utf8String(propertyName)).send().getValue();

                LOGGER.info("Read succeed: " + scAddr);
                return scAddr;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getIndex(String roleName, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String rcAddr = system.getRcIndex(new Utf8String(roleName)).send().getValue();

                LOGGER.info("Read succeed: " + rcAddr);
                return rcAddr;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public Map<String, String> getRoleAll(Credentials credentials) throws Exception {
        Map<String, String> result = new HashMap<>();
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);

        for (String role : RoleType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String rcAddr = system.getRcIndex(new Utf8String(role)).send().getValue();
                    if (!rcAddr.equals("")) {
                        result.putIfAbsent(role, rcAddr);
                    }
                    flag = true;
                } catch (NullPointerException e) {
                    LOGGER.error(e.toString());
                    count++;
                }
            }
        }

        return result;
}

    public Map<String, String> getPropertyAll(Credentials credentials) throws Exception {
        Map<String, String> result = new HashMap<>();
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String scAddr = system.getScIndex(new Utf8String(property)).send().getValue();
                    if (!scAddr.equals("")) {
                        result.putIfAbsent(property, scAddr);
                    }
                    flag = true;
                } catch (NullPointerException e) {
                    LOGGER.error(e.toString());
                    count++;
                }
            }
        }

        return result;
    }

    public String getSysAddress() {
        return sysAddress;
    }
}
