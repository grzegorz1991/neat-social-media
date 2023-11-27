package pl.grzegorz.neat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.grzegorz.neat.model.user.CustomUserDetails;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserProfileForm;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landingPage() {
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication) {
        // Retrieve the authenticated user's details
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Get the name and surname from the user details
        String username = userDetails.getUsername();
        String name = userDetails.getName();
        String surname = userDetails.getSurname();
        String email = userDetails.getEmail();
        // Add the attributes to the model
        model.addAttribute("username", username);
        model.addAttribute("name", name);
        model.addAttribute("surname", surname);
        model.addAttribute("email", email);


        return "index";
    }

    @GetMapping("/home/settings-fragment")
    public String getSettingsFragment(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUser(); // Assuming getUser() is a method in CustomUserDetails returning UserEntity

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
