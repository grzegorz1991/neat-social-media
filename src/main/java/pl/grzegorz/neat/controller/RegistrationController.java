package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserRegistrationForm;
import pl.grzegorz.neat.model.user.UserService;

import java.util.Collections;

@Controller
public class RegistrationController {


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Add an empty UserRegistrationForm to the model
        model.addAttribute("registrationForm", new UserRegistrationForm());
        return "register"; // Return the name of your registration HTML template
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registrationForm") UserRegistrationForm registrationForm) {
        // Process the registration form and save the user
        userService.registerUser(registrationForm.getUsername(), registrationForm.getEmail(),
                passwordEncoder.encode(registrationForm.getPassword()));

        // After successful registration, redirect to the login page
        return "redirect:/login";
    }
}
