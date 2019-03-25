package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import pl.piomin.service.blockchain.model.BlockchainTransaction;
import pl.piomin.service.blockchain.model.IPFSSwapper;
import pl.piomin.service.blockchain.service.BlockchainService;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/transaction")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/getCompleted")
    public ArrayList<IPFSSwapper> getCompleted() {
        return blockchainService.getCompleted();
    }

    @GetMapping("/getPending")
    public ArrayList<IPFSSwapper> getPending() {
        return blockchainService.getPending();
    }

    @PostMapping("/execute")
    public BlockchainTransaction execute(@RequestBody BlockchainTransaction transaction) throws IOException {
        return blockchainService.process(transaction);
    }
}
