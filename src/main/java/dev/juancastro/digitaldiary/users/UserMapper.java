package dev.juancastro.digitaldiary.users;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {
    public static User toEntity(UserDto dto, PasswordEncoder encoder) {

        byte[] decodedBytes = Base64.getDecoder().decode(dto.password());
        String passwordDecoded = new String(decodedBytes, StandardCharsets.UTF_8);
        
        String passwordEncoded = encoder.encode(passwordDecoded);
        
        return new User(dto.username(), passwordEncoded, dto.email());
    }
}
