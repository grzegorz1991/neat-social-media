package pl.grzegorz.neat.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.message.MessageService;
import pl.grzegorz.neat.model.notification.NotificationService;
import pl.grzegorz.neat.model.post.PostEntity;
import pl.grzegorz.neat.model.post.PostService;
import pl.grzegorz.neat.model.post.PostUtil;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserProfileForm;
import pl.grzegorz.neat.model.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

@Controller
public class HomeController {


    private final MessageService messageService;
    private final UserService userService;

    private final NotificationService notificationService;

    private final PostService postService;

    public HomeController(MessageService messageService, UserService userService, NotificationService notificationService, PostService postService) {
        this.messageService = messageService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.postService = postService;
    }

    @GetMapping("/")
    public String landingPage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHomePage(Model model, Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String name = userDetails.getName();
        String surname = userDetails.getSurname();
        String email = userDetails.getEmail();

        UserEntity userByUsername = userService.getUserByUsername(username);
        int numberofMessages = messageService.getMessagesForUser(userByUsername).size();
        int inboxSize = messageService.getAllNonArchivedMessagesByReceiver(userByUsername).size();
        int numberOfUnreadMessages = messageService.getNumberOfUnreadMessages(userByUsername);
        int numberOfUnreadNotifications = notificationService.getUnreadNotificationsForUser(userByUsername.getId()).size();
        String avatarURL = userByUsername.getImagePath();

        List<MessageEntity> unreadList = messageService.getTop5UnreadMessages(userByUsername);

        for (MessageEntity message : unreadList) {
            LocalDateTime timestamp = message.getTimestamp();
            String relativeTime = convertToLocalDateTime(timestamp);
            message.setRelativeTime(relativeTime);
        }
        model.addAttribute("avatar", avatarURL);
        model.addAttribute("unread5MessageList", unreadList);
        model.addAttribute("unreadMessagesNumber", numberOfUnreadMessages);
        model.addAttribute("unreadNotificationsCount", numberOfUnreadNotifications);
        model.addAttribute("newMessagesNumber", inboxSize);
        model.addAttribute("username", username);
        model.addAttribute("name", name);
        model.addAttribute("surname", surname);
        model.addAttribute("email", email);

        return "index";
    }

    @GetMapping("/home/settingsdropdown-fragment")
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

    @GetMapping("/home/settings-fragment")
    public String getSettingsDashFragment() {

        return "redirect:/home/settingsdropdown-fragment";
    }

    @GetMapping("/home/home-fragment")
    public String getDefaultFragment(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        UserEntity loggedUser = userDetails.getUser();
        userService.updateUserLastSeen(loggedUser.getId());

        List<PostEntity> posts = postService.getNewPosts();
        PostUtil.setRelativeTime(posts);
        PostUtil.filterOldPosts(posts, 1440);
        model.addAttribute("posts", posts);


        return "/home/default/home-default";
    }


}
