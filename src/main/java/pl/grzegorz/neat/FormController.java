package pl.grzegorz.neat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/submit-form")
public class FormController {

    @PostMapping
    public ResponseEntity<String> submitForm(@RequestBody(required = false) String requiredParam) {
        if (requiredParam == null || requiredParam.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: Missing required parameter");
        } else {
            // Process the request if validation passes
            // ...

            return ResponseEntity.ok("Form submitted successfully");
        }
    }
}
