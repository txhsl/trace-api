package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import pl.piomin.service.blockchain.model.User;

import java.io.IOException;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final Web3j web3j;

    public UserService(Web3j web3j) {
        this.web3j = web3j;
    }

    public boolean register(User user) throws IOException {
        return false;
    }
}
