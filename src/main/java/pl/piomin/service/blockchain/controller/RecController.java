package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.model.DataSwapper;
import pl.piomin.service.blockchain.model.User;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;


@RestController
public class RecController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private String sysAddress = null;

    public RecController(SystemService systemService, UserService userService, DataService dataService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;

        this.sysAddress = systemService.recover();
    }

    @PostMapping("/system/reset")
    public boolean reset() throws Exception {
        sysAddress =  systemService.reset(userService.getCurrent());
        return userService.reset(sysAddress);
    }

    @PostMapping("/user/signIn")
    public boolean signIn(@RequestBody User user) throws IOException, CipherException {
        return userService.signIn(user);
    }

    @PostMapping("/data/write")
    public TransactionReceipt writeData(@RequestBody DataSwapper data) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getSC(rcAddr.toString(), PropertyType.getID(data.getPropertyName()), false);
        return dataService.write(scAddr.toString() ,userService.getCurrent(), data);
    }

    @GetMapping("/data/read")
    public DataSwapper readData(@RequestBody DataSwapper data) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getSC(rcAddr.toString(), PropertyType.getID(data.getPropertyName()), true);
        return dataService.read(scAddr.toString(), userService.getCurrent(), data);
    }
}
