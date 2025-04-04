package dev.juancastro.digitaldiary.auth.register;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dev.juancastro.digitaldiary.users.User;
import dev.juancastro.digitaldiary.users.UserDto;
import dev.juancastro.digitaldiary.users.UserRepository;

@Service
public class RegisterService {

    private final UserRepository userRepository;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> save(UserDto userData) {

        Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(userData.password());
        String passwordDecoded = new String(decodedBytes);
        System.out.println("Registro: Contraseña decodificada: " + passwordDecoded);

        System.out.println("<------------ " + passwordDecoded);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordEncoded = encoder.encode(passwordDecoded);

        User newUser = new User(userData.username(), passwordEncoded);
        userRepository.save(newUser);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");

        return response;
    }

}