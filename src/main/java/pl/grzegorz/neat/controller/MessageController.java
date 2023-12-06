package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.message.MessageRequest;
import pl.grzegorz.neat.model.message.MessageService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping("/home/new-message-fragment")
    public String newMessagesFragment(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        List<MessageEntity> messages = messageService.getMessagesForUser(currentUser);
        List<UserEntity> usersList = userService.getAllUsers();

        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).getId() == currentUser.getId()) {
                usersList.remove(usersList.get(i));
            }
        }
        model.addAttribute("messages", messages);
        model.addAttribute("user", currentUser);
        model.addAttribute("users", usersList);

        return "home/newMessage";
    }

    @GetMapping("/home/messages-inbox-fragment")
    public String getMessagesInboxFragment(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        Page<MessageEntity> messagesPage = messageService.getMessagesForUser(page, pageSize, currentUser);
        List<MessageEntity> incomingMessages = messagesPage.getContent();

        for (MessageEntity message : incomingMessages) {
            LocalDateTime timestamp = message.getTimestamp();
            String relativeTime = convertToLocalDateTime(timestamp);
            message.setRelativeTime(relativeTime);
        }

        model.addAttribute("messages", incomingMessages);
        model.addAttribute("user", currentUser);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", messagesPage.getTotalPages());


        return "home/incomingMessageListFragment";
    }
    @GetMapping("/retrive-messages")
    public Page<MessageEntity> getMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize) {

        System.out.println("Get messages");

        return messageService.getMessages(page, pageSize);
    }

    @GetMapping("home/messages-outbox-details-fragment")
    public String getOutboxMessageDetails(Model model, Authentication authentication, @RequestParam(defaultValue = "0") int messageId) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        MessageEntity messageEntity = messageService.getMessage(messageId);

        LocalDateTime timestamp = messageEntity.getTimestamp();
        String relativeTime = convertToLocalDateTime(timestamp);
        messageEntity.setRelativeTime(relativeTime);

        model.addAttribute("messageEntity", messageEntity);
        model.addAttribute("UserEntity", currentUser);
        return "home/message-outbox-details-fragment";
    }

    @GetMapping("home/messages-inbox-details-fragment")
    public String getInboxMessageDetails(Model model, Authentication authentication, @RequestParam(defaultValue = "0") int messageId) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        MessageEntity messageEntity = messageService.getMessage(messageId);

        if (!messageEntity.isMessageRead()) {
            messageService.markMessageAsRead(messageId);
            System.out.println("Message " + messageId + " marked as read");
        }


        LocalDateTime timestamp = messageEntity.getTimestamp();
        String relativeTime = convertToLocalDateTime(timestamp);
        messageEntity.setRelativeTime(relativeTime);


        model.addAttribute("messageEntity", messageEntity);
        model.addAttribute("UserEntity", currentUser);
        return "home/message-inbox-details-fragment";
    }

    @GetMapping("/home/messages-outbox-fragment")
    public String messagesSentFragment(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        Page<MessageEntity> messagesPage = messageService.getMessagesFromUser(page, pageSize, currentUser);
        List<MessageEntity> messages = messagesPage.getContent();

        for (MessageEntity message : messages) {
            LocalDateTime timestamp = message.getTimestamp();
            String relativeTime = convertToLocalDateTime(timestamp);
            message.setRelativeTime(relativeTime);
        }

        model.addAttribute("messages", messages);
        model.addAttribute("user", currentUser);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", messagesPage.getTotalPages());

        return "home/outgoingMessageListFragment";
    }

//    @PostMapping("/send-message")
//    public ResponseEntity<?> sendMessageForm(@ModelAttribute MessageRequest messageRequest, Authentication authentication) {
//        // Additional validation logic can be added
//        List<UserEntity> usersList = userService.getAllUsers();
//        UserEntity receiver = userService.getUserById(messageRequest.getReceiverId());
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        UserEntity user = userDetails.getUser();
//        messageService.sendMessage(user, receiver, messageRequest.getContent(), messageRequest.getTitle(), false);
//        return ResponseEntity.ok("Message sent successfully");
//    }

    @PostMapping("/send-message")
    public String sendMessageForm(@ModelAttribute MessageRequest messageRequest, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {

            UserEntity receiver = userService.getUserById(messageRequest.getReceiverId());
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserEntity user = userDetails.getUser();

            messageService.sendMessage(user, receiver, messageRequest.getContent(), messageRequest.getTitle(), false);

            // Pass the success message as a model attribute
            redirectAttributes.addFlashAttribute("successMessage", "Message sent successfully");
        } catch (Exception e) {

        }

        return "redirect:/home";
    }


    @GetMapping
    public ResponseEntity<List<MessageEntity>> getMessages() {
        List<MessageEntity> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/get-unread-messages-count")
    @ResponseBody
    public ResponseEntity<Integer> getUnreadMessagesCount(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        int unreadMessagesCount = messageService.getNumberOfUnreadMessages(currentUser);

        return ResponseEntity.ok(unreadMessagesCount);
    }

}
