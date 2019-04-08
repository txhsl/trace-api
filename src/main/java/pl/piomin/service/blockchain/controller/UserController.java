package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import pl.piomin.service.blockchain.model.*;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private final MessageService messageService;
    private final BlockchainService blockchainService;

    public UserController(SystemService systemService, UserService userService,
                          DataService dataService, MessageService messageService,
                          BlockchainService blockchainService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.messageService = messageService;
        this.blockchainService = blockchainService;
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
    @PostMapping("/requestRole")
    public Result requestRole(@RequestBody PermissionSwapper permission) throws Exception {
        String rcAddr = userService.addRole();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(permission, Message.Type.Role, null, UserService.accounts[0], df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }

    @PostMapping("/requestProperty")
    public Result requestProperty(@RequestBody PermissionSwapper permission) throws Exception {
        String rcAddr = systemService.getRC(userService.getCurrent());
        String scAddr = permission.hasAddress() ? permission.getAddress() : dataService.addProperty(userService.getCurrent());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(permission, Message.Type.Property, userService.setOwnedAsync(rcAddr, permission.getPropertyName(), new Address(scAddr)),
                UserService.accounts[0], df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }

    @PostMapping("/requestReader")
    public Result requestReader(@RequestBody PermissionSwapper permission) throws Exception {
        String scAddr = systemService.getSC(permission.getPropertyName(), userService.getCurrent());
        String targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());

        permission.setIsRead(true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(permission, Message.Type.Permission, userService.setManagedAsync(targetRC, permission.getPropertyName(), new Address(scAddr)),
                dataService.getOwner(scAddr, userService.getCurrent()), df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }

    @PostMapping("/requestWriter")
    public Result requestWriter(@RequestBody PermissionSwapper permission) throws Exception {
        String scAddr = systemService.getSC(permission.getPropertyName(), userService.getCurrent());
        String targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());

        permission.setIsRead(false);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(permission, Message.Type.Permission, userService.setOwnedAsync(targetRC, permission.getPropertyName(),
                new Address(scAddr)),dataService.getOwner(scAddr, userService.getCurrent()), df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }

    //SC owner
    @PostMapping("/permitReader")
    public Result permitReader(@RequestBody PermissionSwapper permission) throws Exception {
        String rcAddr = systemService.getRC(userService.getCurrent());
        String scAddr = userService.getOwned(rcAddr, permission.getPropertyName());

        TaskSwapper task = new TaskSwapper(permission.getPropertyName(), "Permission Permit", userService.getCurrent().getAddress());
        task.setFuture(dataService.addReaderAsync(scAddr, userService.getCurrent(), permission.getTarget()));
        blockchainService.addPending(task);
        return new Result(true);
    }
    //SC owner
    @PostMapping("/permitWriter")
    public Result permitWriter(@RequestBody PermissionSwapper permission) throws Exception {
        String rcAddr = systemService.getRC(userService.getCurrent());
        String scAddr = userService.getOwned(rcAddr, permission.getPropertyName());

        TaskSwapper task = new TaskSwapper(permission.getPropertyName(), "Permission Permit", userService.getCurrent().getAddress());
        task.setFuture(dataService.setWriterAsync(scAddr, userService.getCurrent(), permission.getTarget()));
        blockchainService.addPending(task);
        return new Result(true);
    }

    //For query
    @GetMapping("/getManaged")
    public Map<String, String> getManaged() throws Exception {
        String rcAddr = systemService.getRC(userService.getCurrent());
        return userService.getManagedAll(rcAddr.toString());
    }

    @GetMapping("/getOwned")
    public Map<String, String> getOwned() throws Exception {
        String rcAddr = systemService.getRC(userService.getCurrent());
        return userService.getOwnedAll(rcAddr.toString());
    }
}
