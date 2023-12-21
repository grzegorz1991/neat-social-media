package pl.grzegorz.neat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserProfileForm;
import pl.grzegorz.neat.model.user.UserService;
import org.springframework.util.StringUtils;


@Controller
public class UserProfileController {


    private UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @ModelAttribute("userProfileForm")
    public UserProfileForm getUserProfileForm() {
        return new UserProfileForm();
    }


    @PostMapping("/profile/update")
    public String updateUserProfile(@ModelAttribute UserProfileForm userProfileForm, Authentication authentication, RedirectAttributes redirectAttributes,Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUser();
        // Update user details based on the form data
        user.setName(userProfileForm.getName());
        user.setSurname(userProfileForm.getSurname());
        user.setEmail(userProfileForm.getEmail());
        // Save the updated user
        userService.updateUser(user);
        return "redirect:/home";
    }

}
