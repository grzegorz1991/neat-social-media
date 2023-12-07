package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.grzegorz.neat.model.message.MessageDTO;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.message.MessageService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserProfileForm;
import pl.grzegorz.neat.model.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

@Controller
public class HomeController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String landingPage() {
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String name = userDetails.getName();
        String surname = userDetails.getSurname();
        String email = userDetails.getEmail();

        UserEntity userByUsername = userService.getUserByUsername(username);
        int numberofMessages = messageService.getMessagesForUser(userByUsername).size();
        int numbweofUnreadMessages = messageService.getNumberOfUnreadMessages(userByUsername);

        List<MessageEntity> unreadList =messageService.getTop5UnreadMessages( userByUsername);

        for (MessageEntity message : unreadList) {
            LocalDateTime timestamp = message.getTimestamp();
            String relativeTime = convertToLocalDateTime(timestamp);
            message.setRelativeTime(relativeTime);
        }

        model.addAttribute("unread5MessageList", unreadList);
        model.addAttribute("unreadMessagesNumber", numbweofUnreadMessages);
        model.addAttribute("newMessagesNumber", numberofMessages);
        model.addAttribute("username", username);
        model.addAttribute("name", name);
        model.addAttribute("surname", surname);
        model.addAttribute("email", email);
        return "index";
    }

    @GetMapping("/home/settings-fragment")
    public String getSettingsFragment(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUser();
        UserProfileForm userProfileForm = new UserProfileForm();
        userProfileForm.setName(user.getName());
        userProfileForm.setSurname(user.getSurname());
        userProfileForm.setEmail(user.getEmail());
        userProfileForm.setUsername(user.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("userProfileForm", userProfileForm);
        return "/home/settings";
    }
    @GetMapping("/get-latest-unread-messages")
    public ResponseEntity<List<MessageDTO>> getLatestUnreadMessages(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUser();

        List<MessageEntity> latestUnreadMessages = messageService.getTop5UnreadMessages(user);

        for (MessageEntity message : latestUnreadMessages) {
            LocalDateTime timestamp = message.getTimestamp();
            String relativeTime = convertToLocalDateTime(timestamp);
            message.setRelativeTime(relativeTime);
        }
        // Convert MessageEntity to MessageDTO if needed
        List<MessageDTO> latestUnreadMessagesDTO = latestUnreadMessages.stream()
                .map(message -> new MessageDTO(message.getId(), message.getTitle(), message.getTimestamp(), message.getRelativeTime(), message.getSender()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(latestUnreadMessagesDTO);
    }

    @GetMapping("/home/default-fragment")
    public String getDefaultFragment() {
        return "/home/home-default";
    }

}
