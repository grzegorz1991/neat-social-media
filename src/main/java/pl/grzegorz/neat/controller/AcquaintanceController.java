package pl.grzegorz.neat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.grzegorz.neat.model.message.MessageEntity;
import pl.grzegorz.neat.model.message.MessageService;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserDTO;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserService;

import java.util.List;

@Controller
public class AcquaintanceController {
    private static final String NEW_ACQUAINTANCE_FRAGMENT = "/home/newacquaintance-fragment";

    private static final String SEE_ACQUAINTANCE_FRAGMENT = "/home/seeacquaintance-fragment";

    private final UserService userService;

    public AcquaintanceController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping(NEW_ACQUAINTANCE_FRAGMENT)
    public String getNewAcquaintanceFragment(Model model, Authentication authentication) {
        List<UserEntity> userList = userService.getAllUsers();
        //Remove your own user from the list
        model.addAttribute("users", userList);
        return NEW_ACQUAINTANCE_FRAGMENT;
    }
    @GetMapping(SEE_ACQUAINTANCE_FRAGMENT)
    public String getSeeAcquaintanceFragment(Model model, Authentication authentication) {

        return SEE_ACQUAINTANCE_FRAGMENT;
    }

}
