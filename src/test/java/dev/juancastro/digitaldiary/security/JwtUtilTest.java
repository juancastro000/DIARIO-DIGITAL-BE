package dev.juancastro.digitaldiary.security;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
    private static final long EXPIRATION_MS = 60_000;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET, EXPIRATION_MS);
    }

    @Test
    @DisplayName("generateToken y getUsernameFromToken deben ser consistentes")
    void testGenerateAndExtractUsername() throws JOSEException, ParseException {
        String username = "alice";
        String token    = jwtUtil.generateToken(username);
        String extracted = jwtUtil.getUsernameFromToken(token);
        assertEquals(username, extracted);
    }

    @Test
    @DisplayName("validateToken retorna true para token válido y usuario correcto")
    void testValidateTokenSuccess() throws JOSEException {
        String username = "alice";
        String token    = jwtUtil.generateToken(username);

        UserDetails userDetails = User.builder()
            .username(username)
            .password("dummy")
            .roles("USER")
            .build();

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    @DisplayName("validateToken retorna false si el username no coincide")
    void testValidateTokenWrongUser() throws JOSEException {
        String token = jwtUtil.generateToken("alice");
        UserDetails other = User.builder()
            .username("bob")
            .password("dummy")
            .roles("USER")
            .build();

        assertFalse(jwtUtil.validateToken(token, other));
    }

    @Test
    @DisplayName("validateToken retorna false si el token está expirado")
    void testExpiredToken() throws JOSEException {
        JwtUtil shortLived = new JwtUtil(SECRET, -1L);
        String token = shortLived.generateToken("alice");

        UserDetails ud = User.builder()
            .username("alice")
            .password("dummy")
            .roles("USER")
            .build();

        assertFalse(shortLived.validateToken(token, ud));
    }

    @Test
    @DisplayName("validateToken retorna false para token malformado")
    void testInvalidToken() {
        UserDetails ud = User.builder()
            .username("alice")
            .password("dummy")
            .roles("USER")
            .build();

        assertFalse(jwtUtil.validateToken("not.a.jwt.token", ud));
    }
}