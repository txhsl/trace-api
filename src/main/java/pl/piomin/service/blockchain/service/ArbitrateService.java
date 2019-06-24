package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.contract.System_sol_System;
import pl.piomin.service.blockchain.model.Report;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/5/27
 * @description: none
 */
@Service
public class ArbitrateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArbitrateService.class);

    private final Web3j web3j;
    private final int REQUEST_LIMIT = 10;

    private Map<String, ArrayList<Report>> reportbox = new HashMap<>();

    public ArbitrateService(Web3j web3j) {
        this.web3j = web3j;
    }

    public boolean report(Report report) {
        if (!reportbox.keySet().contains(report.getTo())) {
            reportbox.put(report.getTo(), new ArrayList<>());
        }
        reportbox.get(report.getTo()).add(report);
        LOGGER.info("Report added, to: " + report.getTo());
        return true;
    }

    public CompletableFuture<TransactionReceipt> arbitrate(String sysAddr, int index, boolean agree, Credentials credentials) throws Exception {
        Report report = reportbox.get(credentials.getAddress()).get(index);
        reportbox.get(credentials.getAddress()).remove(index);
        if (agree) {
            return transferFrom(sysAddr, report.getTarget(), report.getFrom(), report.getAmount(), credentials);
        }
        else {
            return transferFrom(sysAddr, report.getFrom(), report.getTarget(), report.getAmount(), credentials);
        }
    }

    public ArrayList<Report> get(String address) {
        return reportbox.get(address);
    }

    public Report get(String address, int index) {
        return reportbox.get(address).get(index);
    }

    private CompletableFuture<TransactionReceipt> transferFrom(String sysAddr, String from, String to, int amount, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return system.transferFrom(new Address(from), new Address(to), new Uint256(BigInteger.valueOf(amount))).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public int getLevel(String sysAddr, String addr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                int level = system.getLevel(new Address(addr)).send().getValue().intValue();
                LOGGER.info("Level read: " + level);
                return level;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }
}
