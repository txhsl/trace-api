package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.contract.Data_sol_Data;
import pl.piomin.service.blockchain.contract.Role_sol_Role;
import pl.piomin.service.blockchain.contract.System_sol_System;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.TaskSwapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static pl.piomin.service.blockchain.model.CustomGasProvider.GAS_LIMIT;
import static pl.piomin.service.blockchain.model.CustomGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class UserService {

    private class Info {
        private String property;
        private String target;
        private boolean isRead;
        private String sender;

        private Info(String property, String target, boolean isRead, String sender) {
            this.property = property;
            this.target = target;
            this.isRead = isRead;
            this.sender = sender;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    public static final String[] accounts = new String[]{"0x6a2fb5e3bf37f0c3d90db4713f7ad4a3b2c24111", "0x38a5d4e63bbac1af0eba0d99ef927359ab8d7293", "0x40b00de2e7b694b494022eef90e874f5e553f996",
            "0x49e2170e0b1188f2151ac35287c743ee60ea1f6a", "0x86dec6586bfa1dfe303eafbefee843919b543fd3", "0x135b8fb39d0f06ea1f2466f7e9f39d3136431480", "0x329b81e0a2af215c7e41b32251ae4d6ff1a83e3e",
            "0x370287edd5a5e7c4b0f5e305b00fe95fc702ce47", "0x5386787c9ef76a235d27f000170abeede038a3db", "0xb41717679a04696a2aaac280d9d45ddd3760ff47", "0xcdfea5a11062fab4cf4c2fda88e32fc6f7753145"};

    private final Web3j web3j;
    private final int REQUEST_LIMIT = 10;
    private Credentials current = null;
    private Map<Info, RemoteCall<TransactionReceipt>> resetList = new HashMap<>();

    public UserService(Web3j web3j) {
        this.web3j = web3j;
    }

    private void sendTx() throws InterruptedException {
        for (Info info : resetList.keySet()) {
            Thread.sleep(2000);

            TaskSwapper permissionTask = new TaskSwapper(info.property, Message.Type.Permission.toString(), info.sender);
            permissionTask.setFuture(resetList.get(info).sendAsync());
            BlockchainService.addPending(permissionTask);
            LOGGER.info("A tx sent. Total " + resetList.size());
        }
        resetList.clear();
    }

    public Credentials getCurrent() {
        return current;
    }

    public boolean reset(String sysAddr) throws Exception {
        //link RCs and SCs
        if (signIn(accounts[1], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr,"Farm", "Farm PIC");
            resetWriterAsync(sysAddr, "Farm", "Farm PIC");
            
            resetReaderAsync(sysAddr, "Farm", "Breed");
            resetWriterAsync(sysAddr, "Farm", "Breed");
            
            resetReaderAsync(sysAddr, "Farm", "Grown Date");
            resetWriterAsync(sysAddr, "Farm", "Grown Date");
            
            resetReaderAsync(sysAddr, "Farm", "Weigt");
            resetWriterAsync(sysAddr, "Farm", "Weigt");
            
            resetReaderAsync(sysAddr, "Farm", "Quarantine PIC");
            resetWriterAsync(sysAddr, "Farm", "Quarantine PIC");
            
            resetReaderAsync(sysAddr, "Farm", "Quarantine Result");
            resetWriterAsync(sysAddr, "Farm", "Quarantine Result");
            
            resetReaderAsync(sysAddr, "Farm", "Farm Name");
            resetWriterAsync(sysAddr, "Farm", "Farm Name");
            
            resetReaderAsync(sysAddr, "Farm", "Farm Licence");
            resetWriterAsync(sysAddr, "Farm", "Farm Licence");

            resetReaderAsync(sysAddr, "Abattoir", "Quarantine Result");
            resetReaderAsync(sysAddr, "Abattoir", "Farm Name");
            resetReaderAsync(sysAddr, "Abattoir", "Farm Licence");

            resetReaderAsync(sysAddr, "Packaging", "Quarantine Result");
            resetReaderAsync(sysAddr, "Packaging", "Farm Name");
            resetReaderAsync(sysAddr, "Packaging", "Farm Licence");

            resetReaderAsync(sysAddr, "Warehousing", "Quarantine Result");
            resetReaderAsync(sysAddr, "Warehousing", "Farm Name");
            resetReaderAsync(sysAddr, "Warehousing", "Farm Licence");

            resetReaderAsync(sysAddr, "Logistics", "Quarantine Result");
            resetReaderAsync(sysAddr, "Logistics", "Farm Name");
            resetReaderAsync(sysAddr, "Logistics", "Farm Licence");

            resetReaderAsync(sysAddr, "Processor", "Quarantine Result");
            resetReaderAsync(sysAddr, "Processor", "Farm Name");
            resetReaderAsync(sysAddr, "Processor", "Farm Licence");

            resetReaderAsync(sysAddr, "Distribution ", "Quarantine Result");
            resetReaderAsync(sysAddr, "Distribution ", "Farm Name");
            resetReaderAsync(sysAddr, "Distribution ", "Farm Licence");

            resetReaderAsync(sysAddr, "Retailer", "Quarantine Result");
            resetReaderAsync(sysAddr, "Retailer", "Farm Name");
            resetReaderAsync(sysAddr, "Retailer", "Farm Licence");

            resetReaderAsync(sysAddr, "Consumer", "Breed");
            resetReaderAsync(sysAddr, "Consumer", "Grown Date");
            resetReaderAsync(sysAddr, "Consumer", "Weigt");

            resetReaderAsync(sysAddr, "Consumer", "Quarantine Result");
            resetReaderAsync(sysAddr, "Consumer", "Farm Name");
            resetReaderAsync(sysAddr, "Consumer", "Farm Licence");

            resetReaderAsync(sysAddr, "Regulator", "Farm PIC");
            resetReaderAsync(sysAddr, "Regulator", "Quarantine PIC");
            resetReaderAsync(sysAddr, "Regulator", "Quarantine Result");
            resetReaderAsync(sysAddr, "Regulator", "Farm Name");
            resetReaderAsync(sysAddr, "Regulator", "Farm Licence");
        }


        if (signIn(accounts[2], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "Abattoir", "Abattoir PIC");
            resetWriterAsync(sysAddr, "Abattoir", "Abattoir PIC");
            
            resetReaderAsync(sysAddr, "Abattoir", "Slaughter Date");
            resetWriterAsync(sysAddr, "Abattoir", "Slaughter Date");
            
            resetReaderAsync(sysAddr, "Abattoir", "Quality Inspector1");
            resetWriterAsync(sysAddr, "Abattoir", "Quality Inspector1");
            
            resetReaderAsync(sysAddr, "Abattoir", "Check Result1");
            resetWriterAsync(sysAddr, "Abattoir", "Check Result1");
            
            resetReaderAsync(sysAddr, "Abattoir", "Abattoir Name");
            resetWriterAsync(sysAddr, "Abattoir", "Abattoir Name");
            
            resetReaderAsync(sysAddr, "Abattoir", "Abattoir Licence");
            resetWriterAsync(sysAddr, "Abattoir", "Abattoir Licence");

            resetReaderAsync(sysAddr, "Packaging", "Check Result1");
            resetReaderAsync(sysAddr, "Packaging", "Abattoir Name");
            resetReaderAsync(sysAddr, "Packaging", "Abattoir Licence");

            resetReaderAsync(sysAddr, "Warehousing", "Check Result1");
            resetReaderAsync(sysAddr, "Warehousing", "Abattoir Name");
            resetReaderAsync(sysAddr, "Warehousing", "Abattoir Licence");

            resetReaderAsync(sysAddr, "Logistics", "Check Result1");
            resetReaderAsync(sysAddr, "Logistics", "Abattoir Name");
            resetReaderAsync(sysAddr, "Logistics", "Abattoir Licence");

            resetReaderAsync(sysAddr, "Processor", "Check Result1");
            resetReaderAsync(sysAddr, "Processor", "Abattoir Name");
            resetReaderAsync(sysAddr, "Processor", "Abattoir Licence");

            resetReaderAsync(sysAddr, "Distribution ", "Check Result1");
            resetReaderAsync(sysAddr, "Distribution ", "Abattoir Name");
            resetReaderAsync(sysAddr, "Distribution ", "Abattoir Licence");

            resetReaderAsync(sysAddr, "Retailer", "Check Result1");
            resetReaderAsync(sysAddr, "Retailer", "Abattoir Name");
            resetReaderAsync(sysAddr, "Retailer", "Abattoir Licence");

            resetReaderAsync(sysAddr, "Consumer", "Slaughter Date");
            resetReaderAsync(sysAddr, "Consumer", "Check Result1");
            resetReaderAsync(sysAddr, "Consumer", "Abattoir Name");
            resetReaderAsync(sysAddr, "Consumer", "Abattoir Licence");

            resetReaderAsync(sysAddr, "Regulator", "Abattoir PIC");
            resetReaderAsync(sysAddr, "Regulator", "Quality Inspector1");
            resetReaderAsync(sysAddr, "Regulator", "Check Result1");
            resetReaderAsync(sysAddr, "Regulator", "Abattoir Name");
            resetReaderAsync(sysAddr, "Regulator", "Abattoir Licence");
        }


        if (signIn(accounts[3], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "Packaging", "Packaging PIC");
            resetWriterAsync(sysAddr, "Packaging", "Packaging PIC");
            
            resetReaderAsync(sysAddr, "Packaging", "Packaging Date");
            resetWriterAsync(sysAddr, "Packaging", "Packaging Date");
            
            resetReaderAsync(sysAddr, "Packaging", "Quality Inspector2");
            resetWriterAsync(sysAddr, "Packaging", "Quality Inspector2");
            
            resetReaderAsync(sysAddr, "Packaging", "Quality Result2");
            resetWriterAsync(sysAddr, "Packaging", "Quality Result2");
            
            resetReaderAsync(sysAddr, "Packaging", "Company Name");
            resetWriterAsync(sysAddr, "Packaging", "Company Name");
            
            resetReaderAsync(sysAddr, "Packaging", "Packaging Licence");
            resetWriterAsync(sysAddr, "Packaging", "Packaging Licence");

            resetReaderAsync(sysAddr, "Warehousing", "Quality Result2");
            resetReaderAsync(sysAddr, "Warehousing", "Company Name");
            resetReaderAsync(sysAddr, "Warehousing", "Packaging Licence");

            resetReaderAsync(sysAddr, "Logistics", "Quality Result2");
            resetReaderAsync(sysAddr, "Logistics", "Company Name");
            resetReaderAsync(sysAddr, "Logistics", "Packaging Licence");

            resetReaderAsync(sysAddr, "Processor", "Quality Result2");
            resetReaderAsync(sysAddr, "Processor", "Company Name");
            resetReaderAsync(sysAddr, "Processor", "Packaging Licence");

            resetReaderAsync(sysAddr, "Distribution ", "Quality Result2");
            resetReaderAsync(sysAddr, "Distribution ", "Company Name");
            resetReaderAsync(sysAddr, "Distribution ", "Packaging Licence");

            resetReaderAsync(sysAddr, "Retailer", "Quality Result2");
            resetReaderAsync(sysAddr, "Retailer", "Company Name");
            resetReaderAsync(sysAddr, "Retailer", "Packaging Licence");

            resetReaderAsync(sysAddr, "Consumer", "Packaging Date");
            resetReaderAsync(sysAddr, "Consumer", "Quality Result2");
            resetReaderAsync(sysAddr, "Consumer", "Company Name");
            resetReaderAsync(sysAddr, "Consumer", "Packaging Licence");

            resetReaderAsync(sysAddr, "Regulator", "Packaging PIC");
            resetReaderAsync(sysAddr, "Regulator", "Quality Inspector2");
            resetReaderAsync(sysAddr, "Regulator", "Quality Result2");
            resetReaderAsync(sysAddr, "Regulator", "Company Name");
            resetReaderAsync(sysAddr, "Regulator", "Packaging Licence");
        }


        if (signIn(accounts[4], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "Warehousing", "Warehousing PIC");
            resetWriterAsync(sysAddr, "Warehousing", "Warehousing PIC");
            
            resetReaderAsync(sysAddr, "Warehousing", "Entry Date");
            resetWriterAsync(sysAddr, "Warehousing", "Entry Date");
            
            resetReaderAsync(sysAddr, "Warehousing", "Leaving Date");
            resetWriterAsync(sysAddr, "Warehousing", "Leaving Date");
            
            resetReaderAsync(sysAddr, "Warehousing", "Amount1");
            resetWriterAsync(sysAddr, "Warehousing", "Amount1");
            
            resetReaderAsync(sysAddr, "Warehousing", "Storage Company");
            resetWriterAsync(sysAddr, "Warehousing", "Storage Company");
            
            resetReaderAsync(sysAddr, "Warehousing", "Storage Licence");
            resetWriterAsync(sysAddr, "Warehousing", "Storage Licence");

            resetReaderAsync(sysAddr, "Logistics", "Amount1");
            resetReaderAsync(sysAddr, "Logistics", "Storage Company");
            resetReaderAsync(sysAddr, "Logistics", "Storage Licence");

            resetReaderAsync(sysAddr, "Processor", "Amount1");
            resetReaderAsync(sysAddr, "Processor", "Storage Company");
            resetReaderAsync(sysAddr, "Processor", "Storage Licence");

            resetReaderAsync(sysAddr, "Distribution ", "Amount1");
            resetReaderAsync(sysAddr, "Distribution ", "Storage Company");
            resetReaderAsync(sysAddr, "Distribution ", "Storage Licence");

            resetReaderAsync(sysAddr, "Retailer", "Amount1");
            resetReaderAsync(sysAddr, "Retailer", "Storage Company");
            resetReaderAsync(sysAddr, "Retailer", "Storage Licence");

            resetReaderAsync(sysAddr, "Consumer", "Entry Date");
            resetReaderAsync(sysAddr, "Consumer", "Leaving Date");
            resetReaderAsync(sysAddr, "Consumer", "Amount1");
            resetReaderAsync(sysAddr, "Consumer", "Amount1");
            resetReaderAsync(sysAddr, "Consumer", "Storage Company");
            resetReaderAsync(sysAddr, "Consumer", "Storage Licence");

            resetReaderAsync(sysAddr, "Regulator", "Warehousing PIC");
            resetReaderAsync(sysAddr, "Regulator", "Amount1");
            resetReaderAsync(sysAddr, "Regulator", "Storage Company");
            resetReaderAsync(sysAddr, "Regulator", "Storage Licence");            
        }


        if (signIn(accounts[5], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "Logistics", "Logistics PIC");
            resetWriterAsync(sysAddr, "Logistics", "Logistics PIC");
            
            resetReaderAsync(sysAddr, "Logistics", "Start Time");
            resetWriterAsync(sysAddr, "Logistics", "Start Time");
            
            resetReaderAsync(sysAddr, "Logistics", "Reach Time");
            resetWriterAsync(sysAddr, "Logistics", "Reach Time");
            
            resetReaderAsync(sysAddr, "Logistics", "Amount2");
            resetWriterAsync(sysAddr, "Logistics", "Amount2");
            
            resetReaderAsync(sysAddr, "Logistics", "Transport Distance");
            resetWriterAsync(sysAddr, "Logistics", "Transport Distance");
            
            resetReaderAsync(sysAddr, "Logistics", "Logistics Company");
            resetWriterAsync(sysAddr, "Logistics", "Logistics Company");
            
            resetReaderAsync(sysAddr, "Logistics", "Logistics Licence");
            resetWriterAsync(sysAddr, "Logistics", "Logistics Licence");
            
            resetReaderAsync(sysAddr, "Logistics", "Transport From");
            resetWriterAsync(sysAddr, "Logistics", "Transport From");
            
            resetReaderAsync(sysAddr, "Logistics", "Transport To");
            resetWriterAsync(sysAddr, "Logistics", "Transport To");            

            resetReaderAsync(sysAddr, "Processor", "Logistics Company");
            resetReaderAsync(sysAddr, "Processor", "Logistics Licence");
            resetReaderAsync(sysAddr, "Processor", "Transport From");
            resetReaderAsync(sysAddr, "Processor", "Transport To");

            resetReaderAsync(sysAddr, "Distribution ", "Logistics Company");
            resetReaderAsync(sysAddr, "Distribution ", "Logistics Licence");
            resetReaderAsync(sysAddr, "Distribution ", "Transport From");
            resetReaderAsync(sysAddr, "Distribution ", "Transport To");

            resetReaderAsync(sysAddr, "Retailer", "Logistics Company");
            resetReaderAsync(sysAddr, "Retailer", "Logistics Licence");
            resetReaderAsync(sysAddr, "Retailer", "Transport From");
            resetReaderAsync(sysAddr, "Retailer", "Transport To");

            resetReaderAsync(sysAddr, "Consumer", "Start Time");
            resetReaderAsync(sysAddr, "Consumer", "Reach Time");
            resetReaderAsync(sysAddr, "Consumer", "Amount2");
            resetReaderAsync(sysAddr, "Consumer", "Transport Distance");
            
            resetReaderAsync(sysAddr, "Consumer", "Logistics Company");
            resetReaderAsync(sysAddr, "Consumer", "Logistics Licence");
            resetReaderAsync(sysAddr, "Consumer", "Transport From");
            resetReaderAsync(sysAddr, "Consumer", "Transport To");

            resetReaderAsync(sysAddr, "Regulator", "Logistics PIC");
            resetReaderAsync(sysAddr, "Regulator", "Logistics Company");
            resetReaderAsync(sysAddr, "Regulator", "Logistics Licence");
            resetReaderAsync(sysAddr, "Regulator", "Transport From");
            resetReaderAsync(sysAddr, "Regulator", "Transport To");
        }


        if (signIn(accounts[6], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "Processor", "Sourcing PIC");
            resetWriterAsync(sysAddr, "Processor", "Sourcing PIC");
            
            resetReaderAsync(sysAddr, "Processor", "Sourcing Date");
            resetWriterAsync(sysAddr, "Processor", "Sourcing Date");
            
            resetReaderAsync(sysAddr, "Processor", "Processor PIC");
            resetWriterAsync(sysAddr, "Processor", "Processor PIC");
            
            resetReaderAsync(sysAddr, "Processor", "Production Date");
            resetWriterAsync(sysAddr, "Processor", "Production Date");
            
            resetReaderAsync(sysAddr, "Processor", "Quality Inspector3");
            resetWriterAsync(sysAddr, "Processor", "Quality Inspector3");
            
            resetReaderAsync(sysAddr, "Processor", "Quality Result3");
            resetWriterAsync(sysAddr, "Processor", "Quality Result3");
            
            resetReaderAsync(sysAddr, "Processor", "Processor Name");
            resetWriterAsync(sysAddr, "Processor", "Processor Name");
            
            resetReaderAsync(sysAddr, "Processor", "Production License");
            resetWriterAsync(sysAddr, "Processor", "Production License");

            resetReaderAsync(sysAddr, "Distribution ", "Quality Result3");
            resetReaderAsync(sysAddr, "Distribution ", "Processor Name");
            resetReaderAsync(sysAddr, "Distribution ", "Production License");

            resetReaderAsync(sysAddr, "Retailer", "Quality Result3");
            resetReaderAsync(sysAddr, "Retailer", "Processor Name");
            resetReaderAsync(sysAddr, "Retailer", "Production License");

            resetReaderAsync(sysAddr, "Consumer", "Sourcing Date");
            resetReaderAsync(sysAddr, "Consumer", "Production Date");
            resetReaderAsync(sysAddr, "Consumer", "Quality Result3");
            resetReaderAsync(sysAddr, "Consumer", "Processor Name");
            resetReaderAsync(sysAddr, "Consumer", "Production License");

            resetReaderAsync(sysAddr, "Regulator", "Sourcing PIC");
            resetReaderAsync(sysAddr, "Regulator", "Processor PIC");
            resetReaderAsync(sysAddr, "Regulator", "Quality Inspector3");
            
            resetReaderAsync(sysAddr, "Regulator", "Quality Result3");
            resetReaderAsync(sysAddr, "Regulator", "Processor Name");
            resetReaderAsync(sysAddr, "Regulator", "Production License");
        }


        if (signIn(accounts[7], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "Distribution ", "Distribution PIC");
            resetWriterAsync(sysAddr, "Distribution ", "Distribution PIC");
            
            resetReaderAsync(sysAddr, "Distribution ", "Load Time");
            resetWriterAsync(sysAddr, "Distribution ", "Load Time");
            
            resetReaderAsync(sysAddr, "Distribution ", "Delivery Time");
            resetWriterAsync(sysAddr, "Distribution ", "Delivery Time");
            
            resetReaderAsync(sysAddr, "Distribution ", "Amount3");
            resetWriterAsync(sysAddr, "Distribution ", "Amount3");
            
            resetReaderAsync(sysAddr, "Distribution ", "Distribution Distance");
            resetWriterAsync(sysAddr, "Distribution ", "Distribution Distance");
            
            resetReaderAsync(sysAddr, "Distribution ", "Distribution Company");
            resetWriterAsync(sysAddr, "Distribution ", "Distribution Company");
            
            resetReaderAsync(sysAddr, "Distribution ", "Distribution Licence");
            resetWriterAsync(sysAddr, "Distribution ", "Distribution Licence");
            
            resetReaderAsync(sysAddr, "Distribution ", "Distribution From");
            resetWriterAsync(sysAddr, "Distribution ", "Distribution From");
            
            resetReaderAsync(sysAddr, "Distribution ", "Distribution To");
            resetWriterAsync(sysAddr, "Distribution ", "Distribution To");

            resetReaderAsync(sysAddr, "Retailer", "Distribution Company");
            resetReaderAsync(sysAddr, "Retailer", "Distribution Licence");
            resetReaderAsync(sysAddr, "Retailer", "Distribution From");
            resetReaderAsync(sysAddr, "Retailer", "Distribution To");

            resetReaderAsync(sysAddr, "Consumer", "Load Time");
            resetReaderAsync(sysAddr, "Consumer", "Delivery Time");
            resetReaderAsync(sysAddr, "Consumer", "Amount3");
            resetReaderAsync(sysAddr, "Consumer", "Distribution Distance");
            
            resetReaderAsync(sysAddr, "Consumer", "Distribution Company");
            resetReaderAsync(sysAddr, "Consumer", "Distribution Licence");
            resetReaderAsync(sysAddr, "Consumer", "Distribution From");
            resetReaderAsync(sysAddr, "Consumer", "Distribution To");

            resetReaderAsync(sysAddr, "Regulator", "Distribution PIC");
            resetReaderAsync(sysAddr, "Regulator", "Distribution Company");
            resetReaderAsync(sysAddr, "Regulator", "Distribution Licence");
            resetReaderAsync(sysAddr, "Regulator", "Distribution From");
            resetReaderAsync(sysAddr, "Regulator", "Distribution To");
        }


        if (signIn(accounts[8], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "Retailer", "Purchase PIC");
            resetWriterAsync(sysAddr, "Retailer", "Purchase PIC");
            
            resetReaderAsync(sysAddr, "Retailer", "Purchase Date");
            resetWriterAsync(sysAddr, "Retailer", "Purchase Date");
            
            resetReaderAsync(sysAddr, "Retailer", "Amount4");
            resetWriterAsync(sysAddr, "Retailer", "Amount4");
            
            resetReaderAsync(sysAddr, "Retailer", "Retailer Company");
            resetWriterAsync(sysAddr, "Retailer", "Retailer Company");
            
            resetReaderAsync(sysAddr, "Retailer", "Retailer Licence");
            resetWriterAsync(sysAddr, "Retailer", "Retailer Licence");
            
            resetReaderAsync(sysAddr, "Retailer", "Price");
            resetWriterAsync(sysAddr, "Retailer", "Price");

            resetReaderAsync(sysAddr, "Consumer", "Purchase Date");
            resetReaderAsync(sysAddr, "Consumer", "Amount4");
            resetReaderAsync(sysAddr, "Consumer", "Retailer Company");
            resetReaderAsync(sysAddr, "Consumer", "Retailer Licence");
            resetReaderAsync(sysAddr, "Consumer", "Price");

            resetReaderAsync(sysAddr, "Regulator", "Purchase PIC");
            resetReaderAsync(sysAddr, "Regulator", "Retailer Company");
            resetReaderAsync(sysAddr, "Regulator", "Retailer Licence");
            resetReaderAsync(sysAddr, "Regulator", "Price");
        }
        sendTx();
        return true;
    }

    public boolean signIn(String address, String password) throws IOException, CipherException {
        Resource resource = new ClassPathResource(address);
        File file = resource.getFile();
        current = WalletUtils.loadCredentials(
                password,
                file.getAbsolutePath());
        return true;
    }

    public String getAdmin(String rcAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                String owner = rc.getAdmin().send().getValue();
                LOGGER.info("Read succeed: " + owner);
                return owner;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt assignWriter(String sysAddr, String roleName, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.assignWriter(new Utf8String(propertyName), new Utf8String(roleName)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private void resetWriterAsync(String sysAddr, String roleName, String propertyName){
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                RemoteCall<TransactionReceipt> tx = system.assignWriter(new Utf8String(propertyName), new Utf8String(roleName));
                resetList.putIfAbsent(new Info(propertyName, roleName, false, current.getAddress()), tx);
                return;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private void resetReaderAsync(String sysAddr, String roleName, String propertyName){
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                RemoteCall<TransactionReceipt> tx = system.assignReader(new Utf8String(propertyName), new Utf8String(roleName));
                resetList.putIfAbsent(new Info(propertyName, roleName, true, current.getAddress()), tx);
                return;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> assignWriterAsync(String sysAddr, String roleName, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                return system.assignWriter(new Utf8String(propertyName), new Utf8String(roleName)).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private TransactionReceipt assignReader(String sysAddr, String roleName, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.assignReader(new Utf8String(propertyName), new Utf8String(roleName)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> assignReaderAsync(String sysAddr, String roleName, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                return system.assignReader(new Utf8String(propertyName), new Utf8String(roleName)).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public boolean checkWriter(String sysAddr, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                Boolean isWriter = system.checkWriter(new Utf8String(propertyName), new Address(current.getAddress())).send().getValue();

                LOGGER.info("Check writer succeed: " + isWriter);
                return isWriter;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public Map<String, String> getOwnedAll(String rcAddr) throws Exception {
        Map<String, String> result = new HashMap<>();
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String scAddr = rc.getOwned(new Utf8String(property)).send().getValue();
                    if (!scAddr.equals("0x0000000000000000000000000000000000000000")) {
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

    public boolean checkReader(String sysAddr, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                Boolean isReader = system.checkReader(new Utf8String(propertyName), new Address(current.getAddress())).send().getValue();

                LOGGER.info("Check reader succeed: " + isReader);
                return isReader;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public Map<String, String> getManagedAll(String rcAddr) throws Exception {
        Map<String, String> result = new HashMap<>();
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String scAddr = rc.getManaged(new Utf8String(property)).send().getValue();
                    if (!scAddr.equals("0x0000000000000000000000000000000000000000")) {
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

    public Map<String, String> getAdministratedAll(String sysAddr, String userAddr) throws Exception {
        Map<String, String> result = new HashMap<>();
        System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String scAddr = system.getSC(new Utf8String(property)).send().getValue();
                    Data_sol_Data sc = Data_sol_Data.load(scAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                    if (sc.getAdmin().send().getValue().equals(userAddr)) {
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
}
