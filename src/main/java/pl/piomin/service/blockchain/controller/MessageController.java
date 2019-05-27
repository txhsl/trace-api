package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
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
        Message msg = messageService.get(userService.getCurrent().getAddress(), index);

        //Handle permit
        TaskSwapper permissionTask = new TaskSwapper(msg.getPermission().getPropertyName(), msg.getType().name() + "通过", userService.getCurrent().getAddress());
        switch (msg.getType()) {
            case 角色:
                permissionTask.setFuture(systemService.addRCAsync(msg.getPermission().getPropertyName(), new Address(msg.getPermission().getTarget()), userService.getCurrent()));
                RoleType.Types.add(msg.getPermission().getPropertyName());
                break;
            case 属性:
                permissionTask.setFuture(systemService.addSCAsync(msg.getPermission().getPropertyName(), new Address(msg.getPermission().getTarget()), userService.getCurrent()));
                PropertyType.Types.add(msg.getPermission().getPropertyName());
                break;
            case 权限:
                String rcAddr = systemService.getRC(userService.getCurrent());
                String toAddr = systemService.getRC(msg.getPermission().getTarget(), userService.getCurrent());
                String scAddr = userService.getOwned(rcAddr, msg.getPermission().getPropertyName());
                if (msg.getPermission().getIsRead()) {
                    permissionTask.setFuture(dataService.addReaderAsync(scAddr, userService.getCurrent(), toAddr));
                }
                else {
                    permissionTask.setFuture(dataService.setWriterAsync(scAddr, userService.getCurrent(), toAddr));
                }
                break;
            case 注册:
                permissionTask.setFuture(systemService.setRCAsync(msg.getPermission().getTarget(), msg.getPermission().getTarget(), userService.getCurrent()));
                break;
            default:
                return false;
        }
        BlockchainService.addPending(permissionTask);

        //Handle request
        if(msg.getRequest() != null) {
            TaskSwapper requestTask = new TaskSwapper(msg.getPermission().getPropertyName(), msg.getType().name() + "申请", msg.getPermission().getTarget());
            CompletableFuture<TransactionReceipt> future = msg.getRequest().sendAsync();
            requestTask.setFuture(future);
            BlockchainService.addPending(requestTask);
            msg.setReceipt(future);
        }

        if(messageService.markAccepted(userService.getCurrent().getAddress(), index)) {
            //Send receipt
            msg.setRead(false);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            msg.setTime(df.format(new Date()));
            msg.setTo(msg.getPermission().getTarget());
            return messageService.addReceipt(msg);
        }
        return false;
    }
}
