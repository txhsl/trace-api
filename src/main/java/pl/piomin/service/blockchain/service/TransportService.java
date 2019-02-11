package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import pl.piomin.service.blockchain.model.data.TransportData;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class TransportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportService.class);

    private final Web3j web3j;

    public TransportService(Web3j web3j) {
        this.web3j = web3j;
    }

    public boolean add(String addr, TransportData data) {
        return false;
    }

    public boolean update(String addr, TransportData data) {
        return false;
    }

    public TransportData get(String addr, int id) {
        return null;
    }
}
