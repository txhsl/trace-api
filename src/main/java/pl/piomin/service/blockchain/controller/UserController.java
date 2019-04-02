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
    @PostMapping("/addProperty")
    public TransactionReceipt addProperty(@RequestBody PermissionSwapper permission) throws Exception {
        String rcAddr = systemService.getRC(userService.getCurrent());
        String scAddr = dataService.addProperty(userService.getCurrent());
        PropertyType.Types.add(permission.getPropertyName());
        TransactionReceipt receipt = systemService.addSC(permission.getPropertyName(), new Address(scAddr), userService.getCurrent());

        return userService.setOwned(rcAddr, permission.getPropertyName(), new Address(scAddr));
    }

    @PostMapping("/requestReader")
    public Result requestReader(@RequestBody PermissionSwapper permission) throws Exception {
        String scAddr = systemService.getSC(permission.getPropertyName(), userService.getCurrent());
        String targetRC = systemService.getRC(permission.getTarget(), userService.getCurrent());

        permission.setIsRead(true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(permission, userService.setManagedAsync(targetRC, permission.getPropertyName(), new Address(scAddr)),
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
        Message msg = new Message(permission, userService.setOwnedAsync(targetRC, permission.getPropertyName(),
                new Address(scAddr)),dataService.getOwner(scAddr, userService.getCurrent()), df.format(new Date()));
        messageService.add(msg);
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
