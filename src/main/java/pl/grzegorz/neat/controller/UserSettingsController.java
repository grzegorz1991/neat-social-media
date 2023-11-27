package pl.grzegorz.neat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserSettingsController {


        @GetMapping("/path-to/settings-fragment")
        public String getSettingsFragment() {
            return "settings"; // This should match the name of your Thymeleaf fragment
        }



    @RequestMapping("/settings")
    public String showUserSettingsForm(){
        return "settings";
    }
}
