package com.betul.productmanagement.service;

import com.betul.productmanagement.DTO.AuthResponse;
import com.betul.productmanagement.DTO.LoginRequest;
import com.betul.productmanagement.DTO.RegisterRequest;
import com.betul.productmanagement.entity.User;
import com.betul.productmanagement.repository.UserRepository;
import com.betul.productmanagement.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // JWT aracı

    // Register
    public AuthResponse registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);

        userRepository.save(user);

        // Kayıt olan kullanıcıya JWT üret
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }

    // Login
    public AuthResponse loginUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                // Giriş başarılı → JWT üret
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
                return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
            }
        }
        throw new RuntimeException("Invalid credentials");
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
