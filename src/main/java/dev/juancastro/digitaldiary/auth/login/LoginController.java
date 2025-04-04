package dev.juancastro.digitaldiary.auth.login;



import java.util.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-endpoint}/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Intento de login con usuario: " + loginRequest.username());
            
            byte[] decodedBytes = Base64.getDecoder().decode(loginRequest.password());
            String decodedPassword = new String(decodedBytes);
            System.out.println("Login: Contraseña decodificada: " + decodedPassword);
            
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username(), 
                    decodedPassword
                )
            );

            System.out.println("Autenticación exitosa para: " + auth.getName());
            return ResponseEntity.ok(new LoginResponse("Login exitoso"));
            
        } catch (AuthenticationException ex) {
            System.err.println("Error de autenticación: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Credenciales inválidas"));
        }
    }
}