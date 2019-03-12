package pl.piomin.service.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import pl.piomin.service.blockchain.service.BlockchainService;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class BlockchainApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainService.class);

    private final Web3j web3j;

    public BlockchainApp(Web3j web3j) {
        this.web3j = web3j;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlockchainApp.class, args);
    }

    @PostConstruct
    public void listen() {

        web3j.transactionFlowable().subscribe(tx -> {

            LOGGER.info("New tx: id={}, block={}, from={}, to={}, value={}", tx.getHash(), tx.getBlockHash(), tx.getFrom(), tx.getTo(), tx.getValue().intValue());

            try {

                EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(tx.getFrom(), DefaultBlockParameterName.LATEST).send();
                LOGGER.info("Tx count: {}", transactionCount.getTransactionCount().intValue());

            } catch (IOException e) {
                LOGGER.error("Error getting transactions", e);
            }

        });

        LOGGER.info("Subscribed");

    }

}
