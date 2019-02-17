package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.contract.Role_sol_Role;
import pl.piomin.service.blockchain.model.User;

import java.io.File;
import java.io.IOException;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final Web3j web3j;
    private Credentials current = null;

    public UserService(Web3j web3j) {
        this.web3j = web3j;
    }

    public Credentials getCurrent() {
        return current;
    }

    public boolean signIn(User user) throws IOException, CipherException {
        Resource resource = new ClassPathResource("countries.xml");
        File file = resource.getFile();
        current = WalletUtils.loadCredentials(
                user.getPassword(),
                file.getAbsolutePath());
        return true;
    }

    public TransactionReceipt setSC(Address rcAddr, int id, Address scAddr, boolean isRead) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr.toString(), web3j, current, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = rc.link(new Uint256(id), scAddr, new Bool(isRead)).send();
        return transactionReceipt;
    }

    public Address getSC(Address rcAddr, int id, boolean isRead) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr.toString(), web3j, current, GAS_PRICE, GAS_LIMIT);
        String scAddr = rc.getSC(new Uint256(id), new Bool(isRead)).send().getValue();
        return new Address(scAddr);
    }
}
