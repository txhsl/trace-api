package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import pl.piomin.service.blockchain.model.data.RetailData;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class RetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetailService.class);

    private final Web3j web3j;

    public RetailService(Web3j web3j) {
        this.web3j = web3j;
    }

    public boolean add(String addr, RetailData data) {
        return false;
    }

    public boolean update(String addr, RetailData data) {
        return false;
    }
}
