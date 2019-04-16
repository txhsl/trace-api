package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import pl.piomin.service.blockchain.model.DataMultipleSwapper;
import pl.piomin.service.blockchain.model.DataSwapper;
import pl.piomin.service.blockchain.model.FileSwapper;
import pl.piomin.service.blockchain.model.TaskSwapper;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/data")
public class DataController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private final FileService fileService;
    private final BlockchainService blockchainService;

    public DataController(SystemService systemService, UserService userService, DataService dataService,
                         FileService fileService, BlockchainService blockchainService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.fileService = fileService;
        this.blockchainService = blockchainService;
    }

    //Writer
    @PostMapping("/write")
    public String writeData(@RequestBody DataSwapper data) throws Exception {
        //Check permission
        String rcAddr = systemService.getRC(userService.getCurrent());
        String scAddr = userService.getOwned(rcAddr, data.getPropertyName());
        if (!dataService.checkWriter(scAddr, new Address(rcAddr), userService.getCurrent())) {
            return null;
        }
        String fileNo = dataService.getFileNum(scAddr, data.getId(), userService.getCurrent());

        //Try cache
        String cachedName = fileService.getFileName(data.getPropertyName());
        String hash = cachedName == null ? "" : dataService.read(scAddr, userService.getCurrent(), cachedName);
        TaskSwapper task = fileService.record(data.getPropertyName(), fileNo, data.getId(), data.getData(), hash);

        //Record hash
        if (task != null) {
            task.setTaskSender(userService.getCurrent().getAddress());
            task.setFuture(dataService.writeAsync(scAddr, userService.getCurrent(), task.getTaskName(), task.getTaskContent()));
            blockchainService.addPending(task);
        }
        return fileNo;
    }

    @PostMapping("/writeMultiple")
    public String[] writeMultipleData(@RequestBody DataMultipleSwapper data) throws Exception {
        List<String> result = new ArrayList<>();
        String rcAddr = systemService.getRC(userService.getCurrent());

        for (String property: data.getData().keySet()) {
            Map<String, DataSwapper> single = data.getData().get(property);
            String scAddr = userService.getOwned(rcAddr, property);

            String[] ids = single.keySet().toArray(new String[0]);
            Arrays.sort(ids);

            if (dataService.checkWriter(scAddr, new Address(rcAddr), userService.getCurrent())) {
                for (String id : ids) {
                    String fileNo = dataService.getFileNum(scAddr, id, userService.getCurrent());

                    //Try cache
                    TaskSwapper task = fileService.record(property, fileNo, id, single.get(id).getData());

                    //Record hash
                    if (task != null) {
                        task.setTaskSender(userService.getCurrent().getAddress());
                        task.setFuture(dataService.writeAsync(scAddr, userService.getCurrent(), task.getTaskName(), task.getTaskContent()));
                        blockchainService.addPending(task);
                    }
                    result.add(fileNo);
                }
            }
        }
        return result.toArray(new String[0]);
    }
    //Reader
    @PostMapping("/read")
    public DataSwapper readData(@RequestBody DataSwapper data) throws Exception {
        //Check permission
        String rcAddr = systemService.getRC(userService.getCurrent());
        String scAddr = userService.getManaged(rcAddr, data.getPropertyName());
        if (!dataService.checkReader(scAddr, new Address(rcAddr), userService.getCurrent())) {
            return data;
        }

        //Try the cache
        String result = fileService.query(data.getPropertyName(), data.getId());

        //Try IPFS
        if (result == null) {
            //Try cache
            String hash = null;
            ArrayList<TaskSwapper> pending = blockchainService.getPending();
            for (TaskSwapper tx : pending) {
                if (tx.getTaskName().equals(dataService.getFileNum(scAddr, data.getId(), userService.getCurrent()))) {
                    hash = tx.getTaskContent();
                    data.setStatus("Pending");
                }
            }

            //Try Eth
            if (hash == null) {
                hash = dataService.read(scAddr, userService.getCurrent(), data.getId());
                data.setStatus("Confirmed");
            }

            FileSwapper file = fileService.input(fileService.download(hash));
            if (fileService.checkFile(file.getFileName(), dataService.getFileNum(scAddr, data.getId(), userService.getCurrent()))) {
                result = file.getContent(data.getId());
                data.setData(result);
                if (result == null) {
                    data.setStatus("Not found");
                }
            }
            else {
                throw new IOException();
            }
        }
        else {
            data.setStatus("Cached");
            data.setData(result);
        }
        return data;
    }

    @PostMapping("/readMultiple")
    public DataMultipleSwapper readMultipleData(@RequestBody DataMultipleSwapper data) throws Exception {
        Map<String, Map<String, DataSwapper>> resultMulti = new HashMap<>();
        String rcAddr = systemService.getRC(userService.getCurrent());

        for (String property : data.getPropertyNames()) {
            String scAddr = userService.getManaged(rcAddr, property);
            Map<String, DataSwapper> result =  new HashMap<>();
            if (dataService.checkReader(scAddr, new Address(rcAddr), userService.getCurrent())) {
                for (String id : data.getIds()) {
                    //Try the cache
                    String temp = fileService.query(property, id);

                    //Try IPFS
                    if (temp == null) {
                        //Try cache
                        String hash = null;
                        String status = null;
                        ArrayList<TaskSwapper> pending = blockchainService.getPending();
                        for (TaskSwapper tx : pending) {
                            if (tx.getTaskName().equals(dataService.getFileNum(scAddr, id, userService.getCurrent()))) {
                                hash = tx.getTaskContent();
                                status = "Pending";
                            }
                        }

                        //Try Eth
                        if (hash == null) {
                            hash = dataService.read(scAddr, userService.getCurrent(), id);
                            status = "Confirmed";
                        }

                        FileSwapper file = fileService.input(fileService.download(hash));
                        if (fileService.checkFile(file.getFileName(), dataService.getFileNum(scAddr, id, userService.getCurrent()))) {
                            temp = file.getContent(id);
                            DataSwapper swapper = new DataSwapper(id, property, temp);
                            if(temp == null) {
                                swapper.setStatus("Not found");
                            }
                            else {
                                swapper.setStatus(status);
                            }
                            result.put(id, swapper);
                        }
                        else {
                            throw new IOException();
                        }
                    }
                    else {
                        DataSwapper swapper = new DataSwapper(id, property, temp);
                        swapper.setStatus("Cached");
                        result.put(id, swapper);
                    }
                }
            }

            resultMulti.put(property, result);
        }
        data.setData(resultMulti);
        return data;
    }
}
