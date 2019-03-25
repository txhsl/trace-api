package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.model.PermissionSwapper;
import pl.piomin.service.blockchain.model.Result;
import pl.piomin.service.blockchain.model.UserSwapper;
import pl.piomin.service.blockchain.service.DataService;
import pl.piomin.service.blockchain.service.SystemService;
import pl.piomin.service.blockchain.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;

    public UserController(SystemService systemService, UserService userService, DataService dataService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
    }

    //Normal
    @PostMapping("/signIn")
    public Result signIn(@RequestBody UserSwapper user) throws IOException, CipherException {
        return new Result(userService.signIn(user.getAddress(), user.getPassword()));
    }

    @PostMapping("/signUp")
    @Deprecated
    public UserSwapper signUp(@RequestBody UserSwapper user) throws Exception {
        user.setAddress(userService.signUp(user.getPassword()));
        return user;
    }
    //RC owner
    @PostMapping("/addProperty")
    public TransactionReceipt addProperty(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        String scAddr = dataService.addProperty(userService.getCurrent());
        PropertyType.Types.add(permission.getPropertyName());

        return userService.setOwned(rcAddr.toString(), permission.getPropertyName(), new Address(scAddr));
    }
    //RC owner
    @PostMapping("/requestReader")
    public TransactionReceipt requestReader(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());
        return userService.setManaged(targetRC.toString(), permission.getPropertyName(), scAddr);
    }
    //RC owner
    @PostMapping("/requestWriter")
    public TransactionReceipt requestWriter(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());
        return userService.setOwned(targetRC.toString(), permission.getPropertyName(), scAddr);
    }
    //SC owner
    @PostMapping("/permitReader")
    public TransactionReceipt permitReader(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());
        return dataService.addReader(scAddr.toString(), userService.getCurrent(), targetRC.toString());
    }
    //SC owner
    @PostMapping("/permitWriter")
    public TransactionReceipt permitWriter(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());
        return dataService.setWriter(scAddr.toString(), userService.getCurrent(), targetRC.toString());
    }
}
