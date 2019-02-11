package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import pl.piomin.service.blockchain.model.data.StoragerData;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class StoragerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoragerService.class);

    private final Web3j web3j;

    public StoragerService(Web3j web3j) {
        this.web3j = web3j;
    }

    public boolean add(String addr, StoragerData data) {
        return false;
    }

    public boolean update(String addr, StoragerData data) {
        return false;
    }

    public StoragerData get(String addr, int id) {
        return null;
    }
}
