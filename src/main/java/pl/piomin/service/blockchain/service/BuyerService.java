package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import pl.piomin.service.blockchain.model.data.BuyerData;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class BuyerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerService.class);

    private final Web3j web3j;

    public BuyerService(Web3j web3j) {
        this.web3j = web3j;
    }

    public boolean add(String addr, BuyerData data) {
        return false;
    }

    public boolean update(String addr, BuyerData data) {
        return false;
    }
}
