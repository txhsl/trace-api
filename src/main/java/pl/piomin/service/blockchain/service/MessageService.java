package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.piomin.service.blockchain.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: HuShili
 * @date: 2019/3/29
 * @description: none
 */

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private Map<String, ArrayList<Message>> mailbox = new HashMap<>();
    private Map<String, ArrayList<Message>> receipts = new HashMap<>();

    public MessageService() {}

    public boolean add(Message msg) {
        if (!mailbox.keySet().contains(msg.getTo())) {
            mailbox.put(msg.getTo(), new ArrayList<>());
        }
        mailbox.get(msg.getTo()).add(msg);
        LOGGER.info("Message added, to: " + msg.getTo());
        return true;
    }

    public ArrayList<Message> get(String address) {
        return mailbox.get(address);
    }

    public Message get(String address, int index) {
        return mailbox.get(address).get(index);
    }

    public boolean markRead(String address, int index) {
        Message msg = mailbox.get(address).get(index);
        if (msg.isRead()) {
            return false;
        }
        else {
            msg.setRead(true);
            return true;
        }
    }

    public boolean markAccepted(String address, int index) {
        Message msg = mailbox.get(address).get(index);
        if (msg.isAccepted()) {
            return false;
        }
        else {
            msg.setAccepted(true);
            return true;
        }
    }

    public boolean addReceipt(Message msg) {
        if (!receipts.keySet().contains(msg.getTo())) {
            receipts.put(msg.getTo(), new ArrayList<>());
        }
        receipts.get(msg.getTo()).add(msg);
        LOGGER.info("Receipt added, to: " + msg.getTo());
        return true;
    }

    public ArrayList<Message> getReceipt(String address) {
        return receipts.get(address);
    }
}
