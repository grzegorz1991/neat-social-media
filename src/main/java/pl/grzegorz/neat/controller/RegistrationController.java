package pl.grzegorz.neat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.grzegorz.neat.model.user.UserEntity;
import pl.grzegorz.neat.model.user.UserService;

import java.util.Collections;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserEntity user){
        // Additional validation logic will be added
        userService.createUser(user, Collections.singletonList("ROLE_USER"));
        return ResponseEntity.ok("User registered successfully");

    }

}
