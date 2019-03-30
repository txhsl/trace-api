package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import pl.piomin.service.blockchain.model.DataMultipleSwapper;
import pl.piomin.service.blockchain.model.DataSwapper;
import pl.piomin.service.blockchain.model.FileSwapper;
import pl.piomin.service.blockchain.model.IPFSSwapper;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), data.getPropertyName());
        if (!dataService.checkWriter(scAddr.toString(), rcAddr, userService.getCurrent())) {
            return null;
        }
        String fileNo = dataService.getFileNum(scAddr.toString(), data.getId(), userService.getCurrent());

        //Try cache
        IPFSSwapper receipt = fileService.record(data.getPropertyName(), fileNo, data.getId(), data.getData());

        //Record hash
        if (receipt != null) {
            receipt.setFuture(dataService.writeAsync(scAddr.toString(), userService.getCurrent(), receipt.getFileName(), receipt.getFileHash()));
            blockchainService.addPending(receipt);
        }
        return fileNo;
    }

    @PostMapping("/writeMultiple")
    public String[] writeMultipleData(@RequestBody DataMultipleSwapper data) throws Exception {
        List<String> result = new ArrayList<>();
        Address rcAddr = systemService.getRC(userService.getCurrent());

        for (String property: data.getData().keySet()) {
            Map<String, DataSwapper> single = data.getData().get(property);
            Address scAddr = userService.getOwned(rcAddr.toString(), property);
            if (dataService.checkWriter(scAddr.toString(), rcAddr, userService.getCurrent())) {
                for (String id : single.keySet()) {
                    String fileNo = dataService.getFileNum(scAddr.toString(), id, userService.getCurrent());

                    //Try cache
                    IPFSSwapper receipt = fileService.record(property, fileNo, id, single.get(id).getData());

                    //Record hash
                    if (receipt != null) {
                        receipt.setFuture(dataService.writeAsync(scAddr.toString(), userService.getCurrent(), receipt.getFileName(), receipt.getFileHash()));
                        blockchainService.addPending(receipt);
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
        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address scAddr = userService.getManaged(rcAddr.toString(), data.getPropertyName());
        if (!dataService.checkReader(scAddr.toString(), rcAddr, userService.getCurrent())) {
            return data;
        }

        //Try the cache
        String result = fileService.query(data.getPropertyName(), data.getId());

        //Try IPFS
        if (result == null) {
            //Try cache
            String hash = null;
            ArrayList<IPFSSwapper> pending = blockchainService.getPending();
            for (IPFSSwapper tx : pending) {
                if (tx.getFileName().equals(dataService.getFileNum(scAddr.toString(), data.getId(), userService.getCurrent()))) {
                    hash = tx.getFileHash();
                    data.setStatus("pending");
                }
            }

            //Try Eth
            if (hash == null) {
                hash = dataService.read(scAddr.toString(), userService.getCurrent(), data.getId());
                data.setStatus("confirmed");
            }

            FileSwapper file = fileService.input(fileService.download(hash));
            if (fileService.checkFile(file.getFileName(), dataService.getFileNum(scAddr.toString(), data.getId(), userService.getCurrent()))) {
                data.setData(file.getContent(data.getId()));
            }
            else {
                throw new IOException();
            }
        }
        else {
            data.setStatus("cached");
            data.setData(result);
        }
        return data;
    }

    @PostMapping("/readMultiple")
    public DataMultipleSwapper readMultipleData(@RequestBody DataMultipleSwapper data) throws Exception {
        Map<String, Map<String, DataSwapper>> resultMulti = new HashMap<>();
        Address rcAddr = systemService.getRC(userService.getCurrent());

        for (String property : data.getPropertyNames()) {
            Address scAddr = userService.getManaged(rcAddr.toString(), property);
            Map<String, DataSwapper> result =  new HashMap<>();
            if (dataService.checkReader(scAddr.toString(), rcAddr, userService.getCurrent())) {
                for (String id : data.getIds()) {
                    //Try the cache
                    String temp = fileService.query(property, id);

                    //Try IPFS
                    if (temp == null) {
                        //Try cache
                        String hash = null;
                        String status = null;
                        ArrayList<IPFSSwapper> pending = blockchainService.getPending();
                        for (IPFSSwapper tx : pending) {
                            if (tx.getFileName().equals(dataService.getFileNum(scAddr.toString(), id, userService.getCurrent()))) {
                                hash = tx.getFileHash();
                                status = "pending";
                            }
                        }

                        //Try Eth
                        if (hash == null) {
                            hash = dataService.read(scAddr.toString(), userService.getCurrent(), id);
                            status = "confirmed";
                        }

                        FileSwapper file = fileService.input(fileService.download(hash));
                        if (fileService.checkFile(file.getFileName(), dataService.getFileNum(scAddr.toString(), id, userService.getCurrent()))) {
                            DataSwapper swapper = new DataSwapper(id, property, file.getContent(id));
                            swapper.setStatus(status);
                            result.put(id, swapper);
                        }
                        else {
                            throw new IOException();
                        }
                    }
                    else {
                        DataSwapper swapper = new DataSwapper(id, property, temp);
                        swapper.setStatus("cached");
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
