package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.contract.Data_sol_Data;

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

    public DataService(Web3j web3j) {
        this.web3j = web3j;
    }

    public String addProperty(Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT).send();
        LOGGER.info("Data Contract deployed: " + sc.getContractAddress());
        return sc.getContractAddress();
    }

    public String getFileNum(String addr, String id, Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String result = sc.getFileNum(new Utf8String(id)).send().getValue();
        LOGGER.info("File number calculated: " + ", data id: " + id);
        return result;
    }

    public TransactionReceipt addPermission(String addr, Credentials credentials, String userAddr) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = sc.addPermitted(new Address(userAddr)).send();

        LOGGER.info("AddPermission: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public boolean checkPermission(Address rcAddr, String addr, Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        return sc.checkPermitted(rcAddr).send().getValue();
    }

    public boolean checkOwner(String addr, Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        return sc.checkOwner(new Address(credentials.getAddress())).send().getValue();
    }

    public TransactionReceipt write(String addr, Credentials credentials, String fileNo, String hash) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = sc.writeDB(new Utf8String(fileNo), new Utf8String(hash)).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public String read(String addr, Credentials credentials, String id) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String hash = sc.readDB(new Utf8String(id)).send().getValue();
        LOGGER.info("Read succeed: " + hash);
        return hash;
    }
}
