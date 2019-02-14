package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.model.DataSwapper;
import pl.piomin.service.blockchain.model.User;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;

@RestController
public class RecController {

    private final UserService userService;
    private final DataService dataService;
    private final Address sysAddress = null;

    public RecController(UserService userService, DataService dataService) {
        this.userService = userService;
        this.dataService = dataService;
    }

    @PostMapping("/user/register")
    public boolean registerUser(@RequestBody User user) throws IOException {
        return userService.register(user);
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
