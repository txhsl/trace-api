package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.model.*;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class RecController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private String sysAddress;

    public RecController(SystemService systemService, UserService userService, DataService dataService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;

        this.sysAddress = systemService.recover();
    }

    @PostMapping("/system/reset")
    public Result reset() throws Exception {
        sysAddress =  systemService.reset(userService.getCurrent());
        return new Result(userService.reset(sysAddress));
    }

    @PostMapping("/system/addRole")
    public TransactionReceipt addRole(@RequestBody String roleName) throws Exception {
        String rcAddr = userService.addRole();
        RoleType.Types.add(roleName);
        return systemService.addRC(sysAddress, roleName, new Address(rcAddr), userService.getCurrent());
    }

    @PostMapping("/user/signIn")
    public Result signIn(@RequestBody UserSwapper user) throws IOException, CipherException {
        return new Result(userService.signIn(user.getAddress(), user.getPassword()));
    }

    @PostMapping("/user/signUp")
    @Deprecated
    public UserSwapper signUp(@RequestBody UserSwapper user) throws Exception {
        user.setAddress(userService.signUp(user.getPassword()));
        return user;
    }

    @PostMapping("/user/addProperty")
    public TransactionReceipt addProperty(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        String scAddr = dataService.addProperty(userService.getCurrent());
        PropertyType.Types.add(permission.getPropertyName());

        return userService.setOwned(rcAddr.toString(), permission.getPropertyName(), new Address(scAddr));
    }

    @PostMapping("/user/assign")
    public TransactionReceipt assign(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(sysAddress, permission.getTarget(), userService.getCurrent());
        return userService.setManaged(targetRC.toString(), permission.getPropertyName(), scAddr);
    }

    @PostMapping("/data/write")
    public TransactionReceipt writeData(@RequestBody DataSwapper data) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), data.getPropertyName());
        return dataService.write(scAddr.toString() ,userService.getCurrent(), data.getId(), data.getData());
    }

    @PostMapping("/data/writeMultiple")
    public TransactionReceipt[] writeMultipleData(@RequestBody DataMultipleSwapper data) throws Exception {
        List<TransactionReceipt> result = new ArrayList<>();
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());

        for (String property: data.getData().keySet()) {
            Map<String, String> single = data.getData().get(property);
            Address scAddr = userService.getOwned(rcAddr.toString(), property);

            for (String id : single.keySet()) {
                result.add(dataService.write(scAddr.toString(), userService.getCurrent(), id, single.get(id)));
            }
        }
        return result.toArray(new TransactionReceipt[result.size()]);
    }

    @GetMapping("/data/read")
    public DataSwapper readData(@RequestBody DataSwapper data) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getManaged(rcAddr.toString(), data.getPropertyName());
        String content = dataService.read(scAddr.toString(), userService.getCurrent(), data.getId());
        data.setData(content);
        return data;
    }

    @GetMapping("/data/readMultiple")
    public DataMultipleSwapper readMultipleData(@RequestBody DataMultipleSwapper data) throws Exception {
        Map<String, Map<String, String>> resultMulti = new HashMap<>();
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());

        for (String property : data.getPropertyNames()) {
            Address scAddr = userService.getManaged(rcAddr.toString(), property);
            Map<String, String> result =  new HashMap<>();
            for (String id : data.getIds()) {
                result.put(id, dataService.read(scAddr.toString(), userService.getCurrent(), id));
            }
            resultMulti.put(property, result);
        }
        data.setData(resultMulti);
        return data;
    }

}
