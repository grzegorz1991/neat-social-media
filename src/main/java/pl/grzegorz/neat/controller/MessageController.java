package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.grzegorz.neat.model.message.ArchiveMessageDTO;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.message.MessageRequest;
import pl.grzegorz.neat.model.message.MessageService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/home/newmessage-fragment")
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

    @GetMapping("/home/showinbox-fragment")
    public String getMessagesInboxFragment(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        Page<MessageEntity> messagesPage = messageService.getNonRecipentArchivedMessagesForUser(page, pageSize, currentUser);

        //Page<MessageEntity> messagesPage = messageService.getMessagesForUser(page, pageSize, currentUser);
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

    @GetMapping("/home/inboxmessage-fragment")
    public String getMessagesInboxDashFragment() {
        return "redirect:/home/showinbox-fragment";
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

    @GetMapping("/home/sentmessage-fragment")
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

    @PostMapping("/send-message")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Map<String, Object>> sendMessageForm(@ModelAttribute MessageRequest messageRequest, Authentication authentication, RedirectAttributes redirectAttributes) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserEntity receiver = userService.getUserById(messageRequest.getReceiverId());
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserEntity user = userDetails.getUser();
            messageService.sendMessage(user, receiver, messageRequest.getContent(), messageRequest.getTitle(), false);

            System.out.println("successfully sent message");
            response.put("success", true);
            response.put("message", "Message sent successfully");
            response.put("redirectUrl", "/home");

        } catch (Exception e) {
            System.out.println("something went wrong with message");
            response.put("success", false);
            response.put("message", "Failed to send message");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/home/archive-message/")
    public ResponseEntity<String> handleArchiveMessageRequest(@RequestBody ArchiveMessageDTO archiveMessageDTO) {
        long messageId = archiveMessageDTO.getMessageId();
        messageService.archiveMessageByReceipent(messageId);
        return ResponseEntity.ok("Message archived successfully");
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
