package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.Report;
import pl.piomin.service.blockchain.model.Result;
import pl.piomin.service.blockchain.service.ArbitrateService;
import pl.piomin.service.blockchain.service.MessageService;
import pl.piomin.service.blockchain.service.SystemService;
import pl.piomin.service.blockchain.service.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class ArbitrateController {

    private final SystemService systemService;
    private final MessageService messageService;
    private final UserService userService;
    private final ArbitrateService arbitrateService;

    public ArbitrateController(SystemService systemService, MessageService messageService,
                               UserService userService, ArbitrateService arbitrateService) {
        this.systemService = systemService;
        this.messageService = messageService;
        this.userService = userService;
        this.arbitrateService = arbitrateService;
    }

    @PostMapping("/report")
    public Result report(@RequestBody Report report) {
        return new Result(arbitrateService.report(report));
    }

    @GetMapping("/receive")
    public Report[] receive() {
        ArrayList<Report> result = arbitrateService.get(userService.getCurrent().getAddress());
        return result == null ? new Report[0] : result.toArray(new Report[0]);
    }

    @PutMapping("/agree/{index}")
    public Result agree(@PathVariable int index) throws Exception {
        Report report = arbitrateService.get(userService.getCurrent().getAddress(), index);
        CompletableFuture<TransactionReceipt> receipt = arbitrateService.arbitrate(systemService.getSysAddress(), index, true, userService.getCurrent());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messageService.addReceipt(new Message(null, Message.Type.检举, null, report.getReporter(), df.format(new Date())));
        messageService.addReceipt(new Message(null, Message.Type.检举, null, report.getTarget(), df.format(new Date())));
        return new Result(true);
    }

    @PutMapping("/disagree/{index}")
    public Result disagree(@PathVariable int index) throws Exception {
        Report report = arbitrateService.get(userService.getCurrent().getAddress(), index);
        CompletableFuture<TransactionReceipt> receipt = arbitrateService.arbitrate(systemService.getSysAddress(), index, false, userService.getCurrent());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messageService.addReceipt(new Message(null, Message.Type.检举, null, report.getReporter(), df.format(new Date())));
        messageService.addReceipt(new Message(null, Message.Type.检举, null, report.getTarget(), df.format(new Date())));
        return new Result(true);
    }
}
