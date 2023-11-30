package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.grzegorz.neat.model.message.MessageService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserProfileForm;
import pl.grzegorz.neat.model.user.UserService;

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


    @GetMapping("/home/default-fragment")
    public String getDefaultFragment() {
        return "/home/home-default";
    }
}
