package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.message.MessageRequest;
import pl.grzegorz.neat.model.message.MessageService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserService;

import java.util.List;

@Controller

public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping("/home/new-messages-fragment")
    public String newMessagesFragment(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        List<MessageEntity> messages = messageService.getMessagesForUser(currentUser);
        List<UserEntity> usersList = userService.getAllUsers();

        for(int i = 0; i < usersList.size(); i++){
            if(usersList.get(i).getId() == currentUser.getId()){
                usersList.remove(usersList.get(i));
            }
        }
        model.addAttribute("messages", messages);
        model.addAttribute("user", currentUser);
        model.addAttribute("users", usersList);

        return "home/newMessage";
    }

    @GetMapping("/home/messages-fragment")
    public String messagesFragment(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        List<MessageEntity> messages = messageService.getMessagesForUser(currentUser);

        model.addAttribute("messages", messages);
        model.addAttribute("user", currentUser);


        return "home/incomingMessageListFragment";
    }

    @GetMapping("/home/messages-sent-fragment")
    public String messagesSentFragment(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        List<MessageEntity> messages = messageService.getMessagesFrom(currentUser);

        model.addAttribute("messages", messages);
        model.addAttribute("user", currentUser);


        return "home/outgoingMessageListFragment";
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessageForm(@ModelAttribute MessageRequest messageRequest, Authentication authentication) {
        // Additional validation logic can be added
        List<UserEntity> usersList = userService.getAllUsers();
        UserEntity receiver = userService.getUserById(messageRequest.getReceiverId());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUser();
        messageService.sendMessage(user, receiver, messageRequest.getContent(), messageRequest.getTitle(), false);
        return ResponseEntity.ok("Message sent successfully");
    }
    @GetMapping
    public ResponseEntity<List<MessageEntity>> getMessages() {
        List<MessageEntity> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }


}
