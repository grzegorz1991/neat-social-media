package pl.grzegorz.neat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landingPage() {
        return "index"; // Thymeleaf will resolve this to "src/main/resources/templates/index.html"
    }
}
