package pl.grzegorz.neat.controller;

import org.springframework.data.domain.Page;
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
import pl.grzegorz.neat.model.user.UserDTO;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

@Controller
public class MessageController {

    private static final String NEW_MESSAGE_FRAGMENT = "/home/newmessage-fragment";

    private static final String REPLY_MESSAGE_FRAGMENT = "/home/replyMessage-fragment";
    private static final String INCOMING_MESSAGE_FRAGMENT = "/home/showinbox-fragment";
    private static final String OUTGOING_MESSAGE_FRAGMENT = "/home/sentmessage-fragment";
    private static final String OUTBOX_DETAILS_FRAGMENT = "home/message-outbox-details-fragment";
    private static final String INBOX_DETAILS_FRAGMENT = "home/message-inbox-details-fragment";

    private static final String ARCHIVED_DETAILS_FRAGMENT = "home/message-archived-details-fragment";
    private static final String ARCHIVED_MESSAGE_FRAGMENT = "/home/archivedmessage-fragment";

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping(NEW_MESSAGE_FRAGMENT)
    public String getNewMessageFragment(Model model, Authentication authentication) {
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

        return NEW_MESSAGE_FRAGMENT;
    }
    @GetMapping(REPLY_MESSAGE_FRAGMENT)
    public String getReplyMessageFragment(Model model, Authentication authentication, @RequestParam(name = "reply", required = false) String replyTo)
     {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        List<MessageEntity> messages = messageService.getMessagesForUser(currentUser);
        List<UserEntity> usersList = userService.getAllUsers();

        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).getId() == currentUser.getId()) {
                usersList.remove(usersList.get(i));
            }
        }

       String reciepentName = userService.getUserById(Integer.parseInt(replyTo)).getUsername();
        model.addAttribute("reciepentName", reciepentName);
        model.addAttribute("replyToRecipient", replyTo);
        model.addAttribute("messages", messages);
        model.addAttribute("user", currentUser);
        model.addAttribute("users", usersList);

        return REPLY_MESSAGE_FRAGMENT;
    }
    @GetMapping(INCOMING_MESSAGE_FRAGMENT)
    public String getMessagesInboxFragment(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        int inboxSize = messageService.getAllNonArchivedMessagesByReceiver(currentUser).size();

        Page<MessageEntity> messagesPage = messageService.getNonRecipentArchivedMessagesForUser(page, pageSize, currentUser);

        //Page<MessageEntity> messagesPage = messageService.getMessagesForUser(page, pageSize, currentUser);
        List<MessageEntity> incomingMessages = messagesPage.getContent();

        for (MessageEntity message : incomingMessages) {
            setRelativeTime(message);
        }
        model.addAttribute("inboxSize", inboxSize);
        model.addAttribute("messages", incomingMessages);
        model.addAttribute("user", currentUser);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", messagesPage.getTotalPages());

        return INCOMING_MESSAGE_FRAGMENT;
    }

    @GetMapping(OUTGOING_MESSAGE_FRAGMENT)
    public String messagesSentFragment(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        Page<MessageEntity> messagesPage = messageService.getAllNonArchivedMessagesBySender(page, pageSize, currentUser);
        List<MessageEntity> messages = messagesPage.getContent();

        for (MessageEntity message : messages) {
            setRelativeTime(message);
        }

        model.addAttribute("messages", messages);
        model.addAttribute("user", currentUser);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", messagesPage.getTotalPages());

        return OUTGOING_MESSAGE_FRAGMENT;
    }

    @GetMapping(ARCHIVED_MESSAGE_FRAGMENT)
    public String archivedMessageFragment(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();

        Page<MessageEntity> messagesPage = messageService.getArchivedMessagesByUser(page, pageSize, currentUser);
        List<MessageEntity> messages = messagesPage.getContent();

        for (MessageEntity message : messages) {
            setRelativeTime(message);
        }

        model.addAttribute("messages", messages);
        model.addAttribute("user", currentUser);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", messagesPage.getTotalPages());


        return ARCHIVED_MESSAGE_FRAGMENT;
    }


    @GetMapping(OUTBOX_DETAILS_FRAGMENT)
    public String getOutboxMessageDetails(Model model, Authentication authentication, @RequestParam(defaultValue = "0") int messageId) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        MessageEntity messageEntity = messageService.getMessage(messageId);

        LocalDateTime timestamp = messageEntity.getTimestamp();
        String relativeTime = convertToLocalDateTime(timestamp);
        messageEntity.setRelativeTime(relativeTime);

        model.addAttribute("messageEntity", messageEntity);
        model.addAttribute("UserEntity", currentUser);
        return OUTBOX_DETAILS_FRAGMENT;
    }

    @GetMapping(INBOX_DETAILS_FRAGMENT)
    public String getInboxMessageDetails(Model model, Authentication authentication, @RequestParam(defaultValue = "0") int messageId) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        MessageEntity messageEntity = messageService.getMessage(messageId);

        if (!messageEntity.isMessageRead()) {
            messageService.markMessageAsRead(messageId);
        }
        LocalDateTime timestamp = messageEntity.getTimestamp();
        String relativeTime = convertToLocalDateTime(timestamp);
        messageEntity.setRelativeTime(relativeTime);

        model.addAttribute("messageEntity", messageEntity);
        model.addAttribute("UserEntity", currentUser);
        return INBOX_DETAILS_FRAGMENT;
    }

    @GetMapping(ARCHIVED_DETAILS_FRAGMENT)
    public String getArchivedMessageDetails(Model model, Authentication authentication, @RequestParam(defaultValue = "0") int messageId) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        MessageEntity messageEntity = messageService.getMessage(messageId);

        if (!messageEntity.isMessageRead()) {
            messageService.markMessageAsRead(messageId);
        }

        LocalDateTime timestamp = messageEntity.getTimestamp();
        String relativeTime = convertToLocalDateTime(timestamp);
        messageEntity.setRelativeTime(relativeTime);

        model.addAttribute("messageEntity", messageEntity);
        model.addAttribute("UserEntity", currentUser);
        return ARCHIVED_DETAILS_FRAGMENT;
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


            response.put("success", true);
            response.put("message", "Message sent successfully");
            response.put("redirectUrl", "/home");

        } catch (Exception e) {

            response.put("success", false);
            response.put("message", "Failed to send message");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/home/archive-message/")
    public ResponseEntity<String> handleArchiveMessageRequest(@RequestBody ArchiveMessageDTO archiveMessageDTO) {
        long messageId = archiveMessageDTO.getMessageId();
        String archiveTarget = archiveMessageDTO.getArchiveTarget();

        if ("sender".equalsIgnoreCase(archiveTarget)) {
            messageService.archiveMessageBySender(messageId);
        } else if ("receiver".equalsIgnoreCase(archiveTarget)) {
            messageService.archiveMessageByReceiver(messageId);
        } else {
            // Handle invalid archiveTarget value
            return ResponseEntity.badRequest().body("Invalid archiveTarget value");
        }

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
    private void setRelativeTime(MessageEntity messageEntity) {
        LocalDateTime timestamp = messageEntity.getTimestamp();
        String relativeTime = convertToLocalDateTime(timestamp);
        messageEntity.setRelativeTime(relativeTime);
    }

    @GetMapping("/get-user-list")
    public ResponseEntity<List<UserDTO>> getUserList() {
        List<UserDTO> userList = userService.getAllUsersDTO(); // Adjust the method based on your service layer
        System.out.println(userList);
        return ResponseEntity.ok(userList);
    }
}
