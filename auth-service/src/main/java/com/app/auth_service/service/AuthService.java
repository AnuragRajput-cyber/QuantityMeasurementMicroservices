package com.app.auth_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.auth_service.dto.AuthResponse;
import com.app.auth_service.dto.LoginRequest;
import com.app.auth_service.dto.RegisterRequest;
import com.app.auth_service.dto.UserProfileResponse;
import com.app.auth_service.entity.User;
import com.app.auth_service.exception.BadRequestException;
import com.app.auth_service.repository.UserRepository;
import com.app.auth_service.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email().trim().toLowerCase())) {
            throw new BadRequestException("User already exists with this email");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setProvider("LOCAL");

        User savedUser = userRepository.save(user);
        return new AuthResponse(jwtUtil.generateToken(savedUser));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (user.getPassword() == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return new AuthResponse(jwtUtil.generateToken(user));
    }

    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return new UserProfileResponse(
                user.getEmail(),
                user.getName(),
                user.getPicture(),
                user.getProvider()
        );
    }
}
