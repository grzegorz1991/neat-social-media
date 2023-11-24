package pl.grzegorz.neat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landingPage() {
        return "index"; // Thymeleaf will resolve this to "src/main/resources/templates/index.html"
    }

    @GetMapping("/api/public/")
    public String publicPage(){
        return "index"; //in case if we want to workaround spring security (backdoor)
    }
}
