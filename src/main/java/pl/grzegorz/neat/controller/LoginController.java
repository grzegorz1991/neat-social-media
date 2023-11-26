package pl.grzegorz.neat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        System.out.println("entered /login endpoint");
        return "login";
    }

    @GetMapping("/success")
    public String loginSuccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("Logged " + username);
        return "redirect:/"; // Redirect to the home page after successful login
    }
    @GetMapping("/logout")
    public String logout() {
        // You can perform additional actions upon logout if needed

        return "redirect:/login?logout"; // Redirect to the login page after logout with a logout parameter
    }
}
