package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.model.*;
import pl.piomin.service.blockchain.service.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private final MessageService messageService;

    public SystemController(SystemService systemService, UserService userService,
                            DataService dataService, MessageService messageService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.messageService = messageService;
    }

    //System owner
    @PostMapping("/reset")
    public Result reset() throws Exception {
        systemService.reset(userService.getCurrent());
        String[] roleaAddrs = userService.resetContract(systemService.getSysAddress());
        String[] dataAddrs = dataService.resetContract(roleaAddrs, systemService.getSysAddress());
        boolean rr = userService.resetPermission(roleaAddrs, dataAddrs);
        boolean rs = dataService.resetPermission(roleaAddrs, dataAddrs);
        return new Result(rr && rs);
    }
    @PostMapping("/register")
    public TransactionReceipt register(@RequestBody UserSwapper user) throws Exception {
        return systemService.setRC(user.getAddress(), user.getAddress(), userService.getCurrent());
    }
    //Normal
    @PostMapping("/requestRole")
    public Result requestRole(@RequestBody RoleSwapper role) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(new PermissionSwapper(role.getName(), role.hasAddress() ? role.getAddress() : userService.addRole()), Message.Type.Role, null,
                UserService.accounts[0], df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }
    //System owner
    @PostMapping("/permitRole")
    public TransactionReceipt permitRole(@RequestBody RoleSwapper role) throws Exception {
        RoleType.Types.add(role.getName());
        return systemService.addRC(role.getName(), new Address(role.getAddress()), userService.getCurrent());
    }

    //For query
    @GetMapping("/getRoles")
    public Map<String, String> getRoles() throws Exception {
        return systemService.getRoleAll(userService.getCurrent());
    }

    @GetMapping("/getProperties")
    public Map<String, String> getProperties() throws Exception {
        return systemService.getPropertyAll(userService.getCurrent());
    }

    @GetMapping("/getRoleNames")
    public String[] getRoleNames() {
        return RoleType.Types.toArray(new String[0]);
    }

    @GetMapping("/getPropertyNames")
    public String[] getPropertyNames() {
        return PropertyType.Types.toArray(new String[0]);
    }
}
