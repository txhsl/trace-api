package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.Transaction;
import pl.piomin.service.blockchain.model.*;
import pl.piomin.service.blockchain.service.BlockchainService;
import pl.piomin.service.blockchain.service.SystemService;
import pl.piomin.service.blockchain.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class BlockchainController {

    private final BlockchainService blockchainService;
    private final SystemService systemService;
    private final UserService userService;

    public BlockchainController(BlockchainService blockchainService, SystemService systemService, UserService userService) {
        this.blockchainService = blockchainService;
        this.systemService = systemService;
        this.userService = userService;
    }

    @GetMapping("/completed")
    public ArrayList<TaskSwapper> getCompleted() {
        return BlockchainService.getCompleted();
    }

    @GetMapping("/completed/address")
    public int[] queryByRole() {
        return BlockchainService.getCompleted(UserService.accounts);
    }

    @GetMapping("/completed/height")
    public HeightStatusSwapper queryByHeight() throws IOException {
        int height = blockchainService.getHight();
        return new HeightStatusSwapper(height, BlockchainService.getCompleted(height));
    }

    @GetMapping("/pending")
    public ArrayList<TaskSwapper> getPending() {
        return BlockchainService.getPending();
    }

    @GetMapping("/error")
    public ArrayList<TaskSwapper> getError() {
        return BlockchainService.getErrorTx();
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

    @GetMapping("/userHistory/{address}")
    public ArrayList<Transaction> userHistory(@PathVariable String address) throws InterruptedException {
        return blockchainService.getFromHistory(address);
    }

    @GetMapping("/contractHistory/{address}")
    public ArrayList<Transaction> contractHistory(@PathVariable String address) throws InterruptedException {
        return blockchainService.getToHistory(address);
    }

    @PostMapping("/subscribe")
    public Result subscribe(@RequestBody ContractSwapper contract) throws InterruptedException {
        return new Result(blockchainService.subscribeContract(systemService.getSysAddress(), contract.getAddress()));
    }

    @PostMapping("/unsubscribe")
    public Result unsubscribe(@RequestBody ContractSwapper contract) throws InterruptedException {
        return new Result(blockchainService.unsubscribeContract(contract.getAddress()));
    }

    @GetMapping("/subscribe")
    public Map<String, ArrayList<Transaction>> subscribe(){
        return blockchainService.getSubscribe();
    }
}
