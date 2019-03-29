package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.Result;
import pl.piomin.service.blockchain.service.MessageService;

import java.util.ArrayList;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public Result send(@RequestBody Message msg) {
        return new Result(messageService.add(msg));
    }

    @GetMapping("/receive/{address}")
    public Message[] receive(@PathVariable String address) {
        ArrayList<Message> result = messageService.get(address);
        return result.toArray(new Message[0]);
    }

    @PutMapping("/read/{address}/{index}")
    public Result read(@PathVariable String address, @PathVariable int index) {
        return new Result(messageService.markRead(address, index));
    }

    @PutMapping("/accept/{address}/{index}")
    public Result accept(@PathVariable String address, @PathVariable int index) {
        return new Result(messageService.markAccepted(address, index));
    }
}
