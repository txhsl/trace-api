package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.Result;
import pl.piomin.service.blockchain.service.DataService;
import pl.piomin.service.blockchain.service.MessageService;
import pl.piomin.service.blockchain.service.SystemService;
import pl.piomin.service.blockchain.service.UserService;

import java.util.ArrayList;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;

    public MessageController(MessageService messageService, SystemService systemService,
                             UserService userService, DataService dataService) {
        this.messageService = messageService;
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
    }

    @Deprecated
    @PostMapping("/send")
    public Result send(@RequestBody Message msg) {
        return new Result(messageService.add(msg));
    }

    @GetMapping("/receive")
    public Message[] receive() {
        ArrayList<Message> result = messageService.get(userService.getCurrent().getAddress());
        return result.toArray(new Message[0]);
    }

    @PutMapping("/read/{index}")
    public Result read(@PathVariable int index) {
        return new Result(messageService.markRead(userService.getCurrent().getAddress(), index));
    }

    @PutMapping("/accept/{index}")
    public TransactionReceipt accept(@PathVariable int index) throws Exception {
        Message msg = messageService.markAccepted(userService.getCurrent().getAddress(), index);
        TransactionReceipt receipt;

        Address rcAddr = systemService.getRC(userService.getCurrent());
        Address toAddr = systemService.getRC(msg.getPermission().getTarget(), userService.getCurrent());
        Address scAddr = userService.getOwned(rcAddr.toString(), msg.getPermission().getPropertyName());
        if (msg.getPermission().getIsRead()) {
            receipt = dataService.addReader(scAddr.toString(), userService.getCurrent(), toAddr.toString());
        }
        else {
            receipt = dataService.setWriter(scAddr.toString(), userService.getCurrent(), toAddr.toString());
        }

        msg.setReceipt(msg.getRequest().send());
        msg.setRead(false);
        msg.setTo(msg.getPermission().getTarget());
        messageService.add(msg);
        return receipt;
    }
}
