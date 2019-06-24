package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.contract.Data_sol_Data;
import pl.piomin.service.blockchain.contract.System_sol_System;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class DataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    private final Web3j web3j;
    private final int REQUEST_LIMIT = 10;

    public DataService(Web3j web3j) {
        this.web3j = web3j;
    }

    public String getFileNum(String addr, String id, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String result = sc.getFileNum(new Utf8String(id)).send().getValue();
                LOGGER.info("File number calculated: " + result + ", data id: " + id);
                return result;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getAdmin(String addr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return sc.getAdmin().send().getValue();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt write(String sysAddr, String propertyName, Credentials credentials, String fileNo, String hash) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.writeData(new Utf8String(propertyName), new Utf8String(fileNo), new Utf8String(hash)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> writeAsync(String sysAddr, String propertyName, Credentials credentials, String fileNo, String hash) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> future = system.writeData(new Utf8String(propertyName), new Utf8String(fileNo), new Utf8String(hash)).sendAsync();

                LOGGER.info("Transaction sent: " + future.toString());
                return future;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String read(String sysAddr, String propertyName, Credentials credentials, String id) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String hash = system.readData(new Utf8String(propertyName), new Utf8String(id)).send().getValue();
                LOGGER.info("Read succeed: " + hash);
                return hash;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private Credentials signIn(String address, String password) throws IOException, CipherException {
        Resource resource = new ClassPathResource(address);
        File file = resource.getFile();
        return  WalletUtils.loadCredentials(
                password,
                file.getAbsolutePath());
    }
}
