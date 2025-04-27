package dev.juancastro.digitaldiary.auth.register;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.juancastro.digitaldiary.auth.exceptions.DuplicateUserException;
import dev.juancastro.digitaldiary.auth.exceptions.RegistrationException;
import dev.juancastro.digitaldiary.users.User;
import dev.juancastro.digitaldiary.users.UserDto;
import dev.juancastro.digitaldiary.users.UserMapper;
import dev.juancastro.digitaldiary.users.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void save(UserDto userData) {
        if (userRepository.existsByUsername(userData.username())) {
            throw new DuplicateUserException("El nombre de usuario '" + userData.username() + "' ya está en uso");
        }
        if (userRepository.existsByEmail(userData.email())) {
            throw new DuplicateUserException("El email '" + userData.email() + "' ya está registrado");
        }

        try {
            User newUser = UserMapper.toEntity(userData, passwordEncoder);
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause().getMessage().toLowerCase();
            if (msg.contains("username")) {
                throw new DuplicateUserException("El nombre de usuario ya existe", e);
            }
            if (msg.contains("email")) {
                throw new DuplicateUserException("El email ya está registrado", e);
            }
            throw new RegistrationException("Error al registrar el usuario", e);
        }
    }
}