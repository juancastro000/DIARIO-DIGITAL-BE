package dev.juancastro.digitaldiary.auth.register;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.juancastro.digitaldiary.users.User;
import dev.juancastro.digitaldiary.users.UserDto;
import dev.juancastro.digitaldiary.users.UserMapper;
import dev.juancastro.digitaldiary.users.UserRepository;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, String> save(UserDto userData) {

        User newUser = UserMapper.toEntity(userData, passwordEncoder);
        userRepository.save(newUser);
        Map<String, String> response = new HashMap<>();
        response.put("the registration was successful", "Success");

        return response;
    }

}