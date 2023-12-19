package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.message.MessageService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserDTO;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static pl.grzegorz.neat.util.RelativeTimeConverter.convertToLocalDateTime;

@Controller
public class AcquaintanceController {
    private static final String NEW_ACQUAINTANCE_FRAGMENT = "/home/newacquaintance-fragment";

    private static final String SEE_ACQUAINTANCE_FRAGMENT = "/home/seeacquaintance-fragment";

    private static final String SEE_ACQUAINTANCE_PROFILE_DETAILS_FRAGMENT = "/home/acquintanceprofile-details-fragment.html";

    private final UserService userService;

    @Autowired
    public AcquaintanceController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping(SEE_ACQUAINTANCE_PROFILE_DETAILS_FRAGMENT)
    public String gerAcquintanceProfileDetails(@RequestParam(defaultValue = "0") int userId, Model model, Authentication authentication){


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userDetails.getUser();
        UserEntity acquintance = userService.getUserById(userId);


        model.addAttribute("acquaintance", acquintance);
        model.addAttribute("userId", userId);
        return SEE_ACQUAINTANCE_PROFILE_DETAILS_FRAGMENT;
    }

    @GetMapping(NEW_ACQUAINTANCE_FRAGMENT)
    public String getNewAcquaintanceFragment(Model model, Authentication authentication) {
        List<UserEntity> userList = userService.getAllUsers();
        //Remove your own user from the list
        setRelativeTime(userList);


        model.addAttribute("users", userList);
        return NEW_ACQUAINTANCE_FRAGMENT;
    }

    @GetMapping(SEE_ACQUAINTANCE_FRAGMENT)
    public String getSeeAcquaintanceFragment(Model model, Authentication authentication) {

        return SEE_ACQUAINTANCE_FRAGMENT;
    }

    private void setRelativeTime(List<UserEntity> users) {
        for (UserEntity user : users) {
            String relativeTime = "never";

            if (user.getLastSeen() != null) {
                LocalDateTime timestamp = user.getLastSeen();
                relativeTime = convertToLocalDateTime(timestamp);

            }
            user.setRelativeTime(relativeTime);
        }
    }
}
