package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.Result;
import pl.piomin.service.blockchain.model.TaskSwapper;
import pl.piomin.service.blockchain.service.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private final BlockchainService blockchainService;

    public MessageController(MessageService messageService, SystemService systemService,
                             UserService userService, DataService dataService,
                             BlockchainService blockchainService) {
        this.messageService = messageService;
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.blockchainService = blockchainService;
    }

    @Deprecated
    @PostMapping("/send")
    public Result send(@RequestBody Message msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msg.setTime(df.format(new Date()));
        return new Result(messageService.add(msg));
    }

    @GetMapping("/receive")
    public Message[] receive() {
        ArrayList<Message> result = messageService.get(userService.getCurrent().getAddress());
        return result == null ? new Message[0] : result.toArray(new Message[0]);
    }

    @GetMapping("/receipt")
    public Message[] getReceipt() {
        ArrayList<Message> result = messageService.getReceipt(userService.getCurrent().getAddress());
        return result == null ? new Message[0] : result.toArray(new Message[0]);
    }

    @PutMapping("/read/{index}")
    public Result read(@PathVariable int index) {
        return new Result(messageService.markRead(userService.getCurrent().getAddress(), index));
    }

    @PutMapping("/accept/{index}")
    public boolean accept(@PathVariable int index) throws Exception {
        Message msg = messageService.markAccepted(userService.getCurrent().getAddress(), index);

        String rcAddr = systemService.getRC(userService.getCurrent());
        String toAddr = systemService.getRC(msg.getPermission().getTarget(), userService.getCurrent());
        String scAddr = userService.getOwned(rcAddr, msg.getPermission().getPropertyName());

        //Handle permit
        TaskSwapper permissionTask = new TaskSwapper(msg.getPermission().getPropertyName(), msg.getType().name() + " Permit", userService.getCurrent().getAddress());
        switch (msg.getType()) {
            case Role:
                break;
            case Property:
                permissionTask.setFuture(systemService.addSCAsync(msg.getPermission().getPropertyName(), new Address(msg.getPermission().getTarget()), userService.getCurrent()));
                PropertyType.Types.add(msg.getPermission().getPropertyName());
                break;
            case Permission:
                if (msg.getPermission().getIsRead()) {
                    permissionTask.setFuture(dataService.addReaderAsync(scAddr, userService.getCurrent(), toAddr));
                }
                else {
                    permissionTask.setFuture(dataService.setWriterAsync(scAddr, userService.getCurrent(), toAddr));
                }
                break;
            default:
                return false;
        }
        blockchainService.addPending(permissionTask);

        //Handle request
        TaskSwapper requestTask = new TaskSwapper(msg.getPermission().getPropertyName(), msg.getType().name() + "Permission Request", msg.getPermission().getTarget());
        CompletableFuture<TransactionReceipt> future = msg.getRequest().sendAsync();
        requestTask.setFuture(future);
        blockchainService.addPending(requestTask);

        //Send receipt
        msg.setReceipt(future);
        msg.setRead(false);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msg.setTime(df.format(new Date()));
        msg.setTo(msg.getPermission().getTarget());
        return messageService.addReceipt(msg);
    }
}
