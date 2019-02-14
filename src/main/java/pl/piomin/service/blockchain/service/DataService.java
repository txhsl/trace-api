package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.contract.System_sol_System;
import pl.piomin.service.blockchain.model.DataSwapper;
import pl.piomin.service.blockchain.model.User;

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

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

    public TransactionReceipt write(Address addr, User user, DataSwapper data) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(user.getPassword(), "/path/to/" + user.getAddress());

        System_sol_System system = System_sol_System.load(addr.toString(), web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = system.writeRC(new Uint256(PropertyType.getID(data.getPropertyName())) , new Utf8String(data.getId()), new Utf8String(data.getData())).send();

        return transactionReceipt;
    }

    public DataSwapper read(Address addr, User user, DataSwapper data) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(user.getPassword(), "/path/to/" + user.getAddress());

        System_sol_System system = System_sol_System.load(addr.toString(), web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String content = system.readRC(new Uint256(PropertyType.getID(data.getPropertyName())), new Utf8String(data.getId())).send().getValue();

        data.setData(content);
        return data;
    }
}
