package dev.juancastro.digitaldiary.auth.register;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.juancastro.digitaldiary.auth.exceptions.DuplicateUserException;
import dev.juancastro.digitaldiary.users.User;
import dev.juancastro.digitaldiary.users.UserDto;
import dev.juancastro.digitaldiary.users.UserRepository;

public class RegisterServiceTest {

    @Test
    public void testRegisterSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        RegisterService registerService = new RegisterService(userRepository, passwordEncoder);

        String rawPassword = "testpassword";
        String base64Password = Base64.getEncoder().encodeToString(rawPassword.getBytes(StandardCharsets.UTF_8));
        UserDto userDto = new UserDto("testuser", "test@example.com", base64Password);

        registerService.save(userDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());

        assertTrue(passwordEncoder.matches(rawPassword, savedUser.getPassword()));
    }
    @Test
    public void testDuplicateUsername() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        RegisterService registerService = new RegisterService(userRepository, passwordEncoder);
        UserDto userDto = new UserDto("testuser", "test@example.com", "testpassword");

        DuplicateUserException ex = assertThrows(DuplicateUserException.class, () -> {
            registerService.save(userDto);
        });

        assertEquals("El nombre de usuario 'testuser' ya está en uso", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testDuplicateEmail() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        RegisterService registerService = new RegisterService(userRepository, passwordEncoder);
        UserDto userDto = new UserDto("testuser", "test@example.com", "testpassword");

        DuplicateUserException ex = assertThrows(DuplicateUserException.class, () -> {
            registerService.save(userDto);
        });

        assertEquals("El email 'test@example.com' ya está registrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }
}