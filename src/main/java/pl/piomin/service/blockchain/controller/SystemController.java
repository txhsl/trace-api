package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.model.Result;
import pl.piomin.service.blockchain.model.RoleSwapper;
import pl.piomin.service.blockchain.service.*;

@RestController
@RequestMapping("/system")
public class SystemController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;

    public SystemController(SystemService systemService, UserService userService, DataService dataService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
    }

    //System owner
    @PostMapping("/reset")
    public Result reset() throws Exception {
        systemService.reset(userService.getCurrent());
        String[] roleaAddrs = userService.resetContract(systemService.getSysAddress());
        String[] dataAddrs = dataService.resetContract(roleaAddrs);
        boolean rr = userService.resetPermission(roleaAddrs, dataAddrs);
        boolean rs = dataService.resetPermission(roleaAddrs, dataAddrs);
        return new Result(rr && rs);
    }
    //Normal
    @PostMapping("/requestRole")
    public RoleSwapper requestRole(@RequestBody RoleSwapper role) throws Exception {
        role.setAddress(userService.addRole());
        role.setPermitted(false);
        return role;
    }
    //System owner
    @PostMapping("/permitRole")
    public TransactionReceipt permitRole(@RequestBody RoleSwapper role) throws Exception {
        RoleType.Types.add(role.getName());
        return systemService.addRC(role.getName(), new Address(role.getAddress()), userService.getCurrent());
    }
}
