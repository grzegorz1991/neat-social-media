package pl.grzegorz.neat;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Provide the path to your generic error page
        return "error";
    }

    @RequestMapping("/error/404")
    public String handle404Error() {
        // Provide the path to your 404 error page
        return "404";
    }

    @RequestMapping("/error/500")
    public String handle500Error() {
        // Provide the path to your 500 error page
        return "500";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
