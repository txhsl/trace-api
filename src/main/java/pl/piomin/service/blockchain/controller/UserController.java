package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.PermissionSwapper;
import pl.piomin.service.blockchain.model.Result;
import pl.piomin.service.blockchain.model.UserSwapper;
import pl.piomin.service.blockchain.service.DataService;
import pl.piomin.service.blockchain.service.MessageService;
import pl.piomin.service.blockchain.service.SystemService;
import pl.piomin.service.blockchain.service.UserService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private final MessageService messageService;

    public UserController(SystemService systemService, UserService userService,
                          DataService dataService, MessageService messageService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.messageService = messageService;
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

    @PostMapping("/requestReader")
    public Result requestReader(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());

        permission.setIsRead(true);
        Message msg = new Message(permission);
        msg.setRequest(userService.setManagedAsync(targetRC.toString(), permission.getPropertyName(), scAddr));
        msg.setTo(dataService.getOwner(scAddr.toString(), userService.getCurrent()));
        messageService.add(msg);
        return new Result(true);
    }

    @PostMapping("/requestWriter")
    public Result requestWriter(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());

        permission.setIsRead(false);
        Message msg = new Message(permission);
        msg.setRequest(userService.setOwnedAsync(targetRC.toString(), permission.getPropertyName(), scAddr));
        msg.setTo(dataService.getOwner(scAddr.toString(), userService.getCurrent()));
        messageService.add(msg);
        return new Result(true);
    }

    //SC owner
    @Deprecated
    @PostMapping("/permitReader")
    public TransactionReceipt permitReader(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());
        return dataService.addReader(scAddr.toString(), userService.getCurrent(), targetRC.toString());
    }
    //SC owner
    @Deprecated
    @PostMapping("/permitWriter")
    public TransactionReceipt permitWriter(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());
        return dataService.setWriter(scAddr.toString(), userService.getCurrent(), targetRC.toString());
    }

    //For query
    @GetMapping("/getManaged")
    public Map<String, String> getManaged() throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        return userService.getManagedAll(rcAddr.toString());
    }

    @GetMapping("/getOwned")
    public Map<String, String> getOwned() throws Exception {
        Address rcAddr = systemService.getRC(userService.getCurrent());
        return userService.getOwnedAll(rcAddr.toString());
    }
}
