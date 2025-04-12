package dev.juancastro.digitaldiary.auth.register;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juancastro.digitaldiary.users.UserDto;

@RestController
@RequestMapping(path="${api-endpoint}/auth")
public class RegisterController {

    private final RegisterService service;

    public RegisterController(RegisterService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody UserDto newUser) {

        Map<String, String> response = service.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}