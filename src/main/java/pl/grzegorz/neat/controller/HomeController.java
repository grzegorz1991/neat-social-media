package pl.grzegorz.neat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.grzegorz.neat.model.user.CustomUserDetails;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landingPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        model.addAttribute("username", username);

        return "index";
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
    public String getSettingsFragment() {
        return "/home/settings";
    }

    @GetMapping("/home/default-fragment")
    public String getDefaultFragment() {
        return "/home/home-default";
    }
}
