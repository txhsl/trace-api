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
import pl.piomin.service.blockchain.model.DataSwapper;

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

    public TransactionReceipt write(Address addr, Credentials credentials, DataSwapper data) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr.toString(), web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = sc.writeDB(new Utf8String(data.getId()), new Utf8String(data.getData())).send();

        return transactionReceipt;
    }

    public DataSwapper read(Address addr, Credentials credentials, DataSwapper data) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr.toString(), web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String content = sc.readDB(new Utf8String(data.getId())).send().getValue();

        data.setData(content);
        return data;
    }
}
