package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.message.MessageRequest;
import pl.grzegorz.neat.model.message.MessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest) {
        // Additional validation logic can be added
        messageService.sendMessage(messageRequest.getSender(), messageRequest.getReceiver(), messageRequest.getContent());
        return ResponseEntity.ok("Message sent successfully");
    }

    @GetMapping
    public ResponseEntity<List<MessageEntity>> getMessages() {
        List<MessageEntity> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }


}
