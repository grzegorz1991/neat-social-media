package pl.grzegorz.neat.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UnderConstructionController {

    @GetMapping("/construction")
    public String landingPage() {
        System.out.println("construction");
        return "underConstr";
    }

}
