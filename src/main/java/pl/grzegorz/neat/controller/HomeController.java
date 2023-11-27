package pl.grzegorz.neat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        String username = authentication.getName();
        model.addAttribute("username", username);
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
