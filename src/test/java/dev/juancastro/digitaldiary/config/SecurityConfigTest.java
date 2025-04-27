package dev.juancastro.digitaldiary.config;
import dev.juancastro.digitaldiary.security.JpaUserDetailsService;
import dev.juancastro.digitaldiary.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = "api-endpoint=/api")
class SecurityConfigTest {

    @MockBean
    private JpaUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void passwordEncoderIsBCrypt() {
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder,
                "PasswordEncoder should be a BCryptPasswordEncoder");
    }

    @Test
    void corsConfigurationIsCorrect() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/any");
        CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(request);
        assertNotNull(config, "CORS configuration should not be null");

        List<String> origins = config.getAllowedOrigins();
        assertEquals(1, origins.size());
        assertEquals("http://localhost:5173", origins.get(0));

        List<String> methods = config.getAllowedMethods();
        assertTrue(methods.contains("GET"));
        assertTrue(methods.contains("POST"));
        assertTrue(methods.contains("PUT"));
        assertTrue(methods.contains("DELETE"));
        assertTrue(methods.contains("OPTIONS"));

        List<String> headers = config.getAllowedHeaders();
        assertTrue(headers.contains("Authorization"));
        assertTrue(headers.contains("Content-Type"));

        assertTrue(config.getAllowCredentials(), "CORS should allow credentials");
    }

    @Test
    void securityFilterChainBeanExists() {
        assertNotNull(securityFilterChain, "SecurityFilterChain bean should be configured");
    }

    @Test
    void authenticationManagerBeanExists() {
        assertNotNull(authenticationManager, "AuthenticationManager bean should be configured");
    }
}
