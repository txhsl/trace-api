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
    private final FileService fileService;
    private String sysAddress;

    public RecController(SystemService systemService, UserService userService, DataService dataService, FileService fileService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.fileService = fileService;

        this.sysAddress = systemService.recover();
    }
    //System owner
    @PostMapping("/system/reset")
    public Result reset() throws Exception {
        sysAddress =  systemService.reset(userService.getCurrent());
        String[] roleaAddrs = userService.resetContract(sysAddress);
        String[] dataAddrs = dataService.resetContract(roleaAddrs);
        boolean rr = userService.resetPermission(roleaAddrs, dataAddrs);
        boolean rs = dataService.resetPermission(roleaAddrs, dataAddrs);
        return new Result(rr && rs);
    }
    //Normal
    @PostMapping("/system/requestRole")
    public RoleSwapper requestRole(@RequestBody RoleSwapper role) throws Exception {
        role.setAddress(userService.addRole());
        role.setPermitted(false);
        return role;
    }
    //System owner
    @PostMapping("/system/permitRole")
    public TransactionReceipt permitRole(@RequestBody RoleSwapper role) throws Exception {
        RoleType.Types.add(role.getName());
        return systemService.addRC(sysAddress, role.getName(), new Address(role.getAddress()), userService.getCurrent());
    }
    //Normal
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
    //RC owner
    @PostMapping("/user/addProperty")
    public TransactionReceipt addProperty(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        String scAddr = dataService.addProperty(userService.getCurrent());
        PropertyType.Types.add(permission.getPropertyName());

        return userService.setOwned(rcAddr.toString(), permission.getPropertyName(), new Address(scAddr));
    }
    //RC owner
    @PostMapping("/user/requestReader")
    public TransactionReceipt requestReader(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(sysAddress, permission.getTarget(), userService.getCurrent());
        return userService.setManaged(targetRC.toString(), permission.getPropertyName(), scAddr);
    }
    //RC owner
    @PostMapping("/user/requestWriter")
    public TransactionReceipt requestWriter(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(sysAddress, permission.getTarget(), userService.getCurrent());
        return userService.setOwned(targetRC.toString(), permission.getPropertyName(), scAddr);
    }
    //SC owner
    @PostMapping("/user/permitReader")
    public TransactionReceipt permitReader(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(sysAddress, permission.getTarget(), userService.getCurrent());
        return dataService.addReader(scAddr.toString(), userService.getCurrent(), targetRC.toString());
    }
    //SC owner
    @PostMapping("/user/permitWriter")
    public TransactionReceipt permitWriter(@RequestBody PermissionSwapper permission) throws Exception {
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), permission.getPropertyName());
        Address targetRC = systemService.getRC(sysAddress, permission.getTarget(), userService.getCurrent());
        return dataService.setWriter(scAddr.toString(), userService.getCurrent(), targetRC.toString());
    }
    //Writer
    @PostMapping("/data/write")
    public String writeData(@RequestBody DataSwapper data) throws Exception {
        //Check permission
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), data.getPropertyName());
        if (!dataService.checkWriter(scAddr.toString(), rcAddr, userService.getCurrent())) {
            return null;
        }
        String fileNo = dataService.getFileNum(scAddr.toString(), data.getId(), userService.getCurrent());

        //Try cache
        IPFSSwapper receipt = fileService.record(data.getPropertyName(), fileNo, data.getId(), data.getData());

        //Record hash
        if (receipt != null) {
            dataService.write(scAddr.toString(), userService.getCurrent(), receipt.getFileName(), receipt.getFileHash());
        }
        return fileNo;
    }

    @PostMapping("/data/writeMultiple")
    public String[] writeMultipleData(@RequestBody DataMultipleSwapper data) throws Exception {
        List<String> result = new ArrayList<>();
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());

        for (String property: data.getData().keySet()) {
            Map<String, String> single = data.getData().get(property);
            Address scAddr = userService.getOwned(rcAddr.toString(), property);
            if (dataService.checkWriter(scAddr.toString(), rcAddr, userService.getCurrent())) {
                for (String id : single.keySet()) {
                    String fileNo = dataService.getFileNum(scAddr.toString(), id, userService.getCurrent());

                    //Try cache
                    IPFSSwapper receipt = fileService.record(property, fileNo, id, single.get(id));

                    //Record hash
                    if (receipt != null) {
                        dataService.write(scAddr.toString(), userService.getCurrent(), receipt.getFileName(), receipt.getFileHash());
                    }
                    result.add(fileNo);
                }
            }
        }
        return result.toArray(new String[0]);
    }
    //Reader
    @GetMapping("/data/read")
    public DataSwapper readData(@RequestBody DataSwapper data) throws Exception {
        //Check permission
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());
        Address scAddr = userService.getManaged(rcAddr.toString(), data.getPropertyName());
        if (!dataService.checkReader(scAddr.toString(), rcAddr, userService.getCurrent())) {
            return data;
        }

        //Try the cache
        String result = fileService.query(data.getPropertyName(), data.getId());

        //Try IPFS
        if (result == null) {
            String hash = dataService.read(scAddr.toString(), userService.getCurrent(), data.getId());

            FileSwapper file = fileService.input(fileService.download(hash));
            if (fileService.checkFile(file.getFileName(), dataService.getFileNum(scAddr.toString(), data.getId(), userService.getCurrent()))) {
                data.setData(file.getContent(data.getId()));
            }
            else {
                throw new IOException();
            }
        }
        else {
            data.setData(result);
        }
        return data;
    }

    @GetMapping("/data/readMultiple")
    public DataMultipleSwapper readMultipleData(@RequestBody DataMultipleSwapper data) throws Exception {
        Map<String, Map<String, String>> resultMulti = new HashMap<>();
        Address rcAddr = systemService.getRC(sysAddress, userService.getCurrent());

        for (String property : data.getPropertyNames()) {
            Address scAddr = userService.getManaged(rcAddr.toString(), property);
            Map<String, String> result =  new HashMap<>();

            if (dataService.checkReader(scAddr.toString(), rcAddr, userService.getCurrent())) {
                for (String id : data.getIds()) {
                    result.put(id, dataService.read(scAddr.toString(), userService.getCurrent(), id));
                }
            }

            resultMulti.put(property, result);
        }
        data.setData(resultMulti);
        return data;
    }

}
