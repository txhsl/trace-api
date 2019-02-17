package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.model.DataSwapper;
import pl.piomin.service.blockchain.model.User;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;


@RestController
public class RecController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private Address sysAddress = null;

    public RecController(SystemService systemService, UserService userService, DataService dataService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
    }

    @PostMapping("/system/reset")
    public boolean reset() throws Exception {
        sysAddress =  systemService.reset();
        return true;
    }

    @PostMapping("/user/signIn")
    public boolean signIn(@RequestBody User user) throws IOException, CipherException {
        return userService.signIn(user);
    }

    @PostMapping("/data/write")
    public TransactionReceipt writeData(@RequestHeader User user, @RequestBody DataSwapper data) throws Exception {
        return dataService.write(sysAddress ,user, data);
    }

    @GetMapping("/data/read")
    public DataSwapper readData(@RequestHeader User user, @RequestBody DataSwapper data) throws Exception {
        return dataService.read(sysAddress, user, data);
    }
}
