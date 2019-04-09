package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.Transaction;
import pl.piomin.service.blockchain.model.BlockchainTransaction;
import pl.piomin.service.blockchain.model.ContractSwapper;
import pl.piomin.service.blockchain.model.HeightStatusSwapper;
import pl.piomin.service.blockchain.model.TaskSwapper;
import pl.piomin.service.blockchain.service.BlockchainService;
import pl.piomin.service.blockchain.service.UserService;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/transaction")
public class BlockchainController {

    private final BlockchainService blockchainService;
    private final UserService userService;

    public BlockchainController(BlockchainService blockchainService, UserService userService) {
        this.blockchainService = blockchainService;
        this.userService = userService;
    }

    @GetMapping("/completed")
    public ArrayList<TaskSwapper> getCompleted() {
        return blockchainService.getCompleted();
    }

    @GetMapping("/completed/address")
    public int[] queryByRole() {
        return blockchainService.getCompleted(UserService.accounts);
    }

    @GetMapping("/completed/height")
    public HeightStatusSwapper queryByHeight() throws IOException {
        int height = blockchainService.getHight();
        return new HeightStatusSwapper(height, blockchainService.getCompleted(height));
    }

    @GetMapping("/pending")
    public ArrayList<TaskSwapper> getPending() {
        return blockchainService.getPending();
    }

    @GetMapping("/error")
    public ArrayList<TaskSwapper> getError() {
        return blockchainService.getErrorTx();
    }

    @PostMapping("/execute")
    public BlockchainTransaction execute(@RequestBody BlockchainTransaction transaction) throws IOException {
        return blockchainService.process(transaction);
    }

    @GetMapping("/balance")
    public double getBalance() throws IOException {
        return blockchainService.getBalance(userService.getCurrent().getAddress());
    }

    @GetMapping("/height")
    public int getHight() throws IOException {
        return blockchainService.getHight();
    }

    @GetMapping("/userHistory")
    public ArrayList<Transaction> userHistory() throws InterruptedException {
        return blockchainService.getUserHistory(userService.getCurrent().getAddress());
    }

    @GetMapping("/contractHistory")
    public ArrayList<Transaction> contractHistory(@RequestBody ContractSwapper contract) throws InterruptedException {
        return blockchainService.getContractHistory(contract.getAddress());
    }
}
