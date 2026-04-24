package com.rescureat.service;

import com.rescureat.dto.AuthResponse;
import com.rescureat.dto.LoginRequest;
import com.rescureat.dto.RegisterRequest;
import com.rescureat.model.User;
import com.rescureat.repository.UserRepository;
import com.rescureat.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS_ERROR = "Invalid credentials";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        validateRegisterRequest(request);

        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);
        return AuthResponse.of(token, saved);
    }

    public AuthResponse login(LoginRequest request) {
        validateLoginRequest(request);

        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException(INVALID_CREDENTIALS_ERROR));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new SecurityException(INVALID_CREDENTIALS_ERROR);
        }

        String token = jwtService.generateToken(user);
        return AuthResponse.of(token, user);
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("name is required.");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("email is required.");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (request.getRole() == null) {
            throw new IllegalArgumentException("role is required.");
        }
    }

    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("email is required.");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("password is required.");
        }
    }
}
