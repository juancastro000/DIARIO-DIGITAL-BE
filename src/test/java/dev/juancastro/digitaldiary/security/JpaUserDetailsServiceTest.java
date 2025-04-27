package dev.juancastro.digitaldiary.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dev.juancastro.digitaldiary.users.User;
import dev.juancastro.digitaldiary.users.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JpaUserDetailsService userDetailsService;

    @Test
    @DisplayName("loadUserByUsername returns UserDetails when user exists")
    void testLoadUserByUsernameSuccess() {
        User mockUser = new User(1L, "alice", "secretPwd", "alice@example.com");
        when(userRepository.findByUsername("alice"))
            .thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("alice");

        assertNotNull(userDetails);
        assertEquals("alice", userDetails.getUsername());
        assertEquals("secretPwd", userDetails.getPassword());
        assertTrue(
            userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")),
            "Expected ROLE_USER authority"
        );

        verify(userRepository).findByUsername("alice");
    }

    @Test
    @DisplayName("loadUserByUsername throws when user not found")
    void testLoadUserByUsernameNotFound() {

        when(userRepository.findByUsername("bob"))
            .thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername("bob")
        );
        assertTrue(
            ex.getMessage().contains("bob"),
            "Exception message should mention the missing username"
        );

        verify(userRepository).findByUsername("bob");
    }
}