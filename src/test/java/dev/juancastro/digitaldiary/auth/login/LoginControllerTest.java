package dev.juancastro.digitaldiary.auth.login;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class LoginControllerTest {

    @Test
    public void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest("testuser", "dGVzdHBhc3N3b3Jk");
        AuthenticationManager authManager = mock(AuthenticationManager.class);
        Authentication authResult = new UsernamePasswordAuthenticationToken("testuser", "testpassword");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authResult);
        LoginController controller = new LoginController(authManager);
        ResponseEntity<LoginResponse> responseEntity = controller.login(loginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Login exitoso", responseEntity.getBody().message());
    }

    @Test
    public void testLoginFailure() {
        LoginRequest loginRequest = new LoginRequest("testuser", "dGVzdHBhc3N3b3Jk");
        AuthenticationManager authManager = mock(AuthenticationManager.class);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Credenciales inválidas"));
        LoginController controller = new LoginController(authManager);
        ResponseEntity<LoginResponse> responseEntity = controller.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Credenciales inválidas", responseEntity.getBody().message());
    }
}