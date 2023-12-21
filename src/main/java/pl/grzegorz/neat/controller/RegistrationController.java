package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.grzegorz.neat.model.notification.NotificationService;
import pl.grzegorz.neat.model.user.UserRegistrationForm;
import pl.grzegorz.neat.model.user.UserService;

@Controller
public class RegistrationController {


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final NotificationService notificationService;

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder, NotificationService notificationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Add an empty UserRegistrationForm to the model
        model.addAttribute("registrationForm", new UserRegistrationForm());
        return "register";
    }

    @GetMapping("/register/terms")
    public String landingPage() {
        return "termsAndConditions";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registrationForm") UserRegistrationForm registrationForm) {

        // Check if the username is already in use
        if (userService.existsByUsername(registrationForm.getUsername())) {

            System.out.println("usename error");
            return "redirect:/register?error=username";
        }

        // Check if the email is already in use
        if (userService.existsByEmail(registrationForm.getEmail())) {
            // Set an error message or add a validation error to be displayed on the form
            // You might want to redirect back to the registration page with an error message
            System.out.println("email error");
            return "redirect:/register?error=email";
        }



        String encode = passwordEncoder.encode(registrationForm.getPassword());
        userService.registerUser(registrationForm.getUsername(), registrationForm.getEmail(),
                encode, registrationForm.getName(), registrationForm.getSurname());
        userService.getUserByUsername(registrationForm.getUsername());
        notificationService.createGreetingNotification(userService.getUserByUsername(registrationForm.getUsername()));


        return "redirect:/login";
    }
}
