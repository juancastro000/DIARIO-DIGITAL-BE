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

import com.nimbusds.jose.JOSEException;

import dev.juancastro.digitaldiary.security.JwtUtil;

@RestController
@RequestMapping("${api-endpoint}/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(loginRequest.password());
            String decodedPassword = new String(decodedBytes);
            
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username(), 
                    decodedPassword
                )
            );

            System.out.println("Autenticación exitosa para: " + auth.getName());
            String token = jwtUtil.generateToken(auth.getName());
            return ResponseEntity.ok(new LoginResponse("Login exitoso", token));
            
        } catch (AuthenticationException | JOSEException ex) {
            System.err.println("Error de autenticación: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Credenciales inválidas", null));
        }
    }
}