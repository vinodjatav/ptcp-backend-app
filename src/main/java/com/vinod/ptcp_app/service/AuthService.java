package com.vinod.ptcp_app.service;

import com.vinod.ptcp_app.entity.User;
import com.vinod.ptcp_app.repository.UserRepository;
import com.vinod.ptcp_app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public String authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword())) {
            User user = userOptional.get();
            return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name()); // 🔹 Convert Role Enum to String
        }
        return null;
    }

    public User registerUser(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Hash password
        user.setRole(User.Role.valueOf(role.toUpperCase())); // Convert role string to ENUM

        return userRepository.save(user);
    }

}
