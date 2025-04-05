package dev.juancastro.digitaldiary.auth.register;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.juancastro.digitaldiary.users.User;
import dev.juancastro.digitaldiary.users.UserDto;
import dev.juancastro.digitaldiary.users.UserRepository;

public class RegisterServiceTest {

    @Test
    public void testRegisterSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        RegisterService registerService = new RegisterService(userRepository);
        UserDto userDto = new UserDto("testuser", "dGVzdHBhc3N3b3Jk");

        Map<String, String> response = registerService.save(userDto);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("testuser", savedUser.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        assertTrue(encoder.matches("testpassword", savedUser.getPassword()));
        assertEquals("Success", response.get("message"));
    }
}