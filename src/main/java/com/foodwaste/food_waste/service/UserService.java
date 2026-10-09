package com.foodwaste.food_waste.service;

import com.foodwaste.food_waste.dto.AuthResponse;
import com.foodwaste.food_waste.entity.User;
import com.foodwaste.food_waste.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(User u) {
        if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (repo.findByEmail(u.getEmail().trim().toLowerCase()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (u.getRole() == null || u.getRole().isBlank()) {
            u.setRole("DONOR");
        }
        if (u.getPassword() == null || u.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        u.setEmail(u.getEmail().trim().toLowerCase());
        // Securely hash the password with BCrypt
        u.setPassword(passwordEncoder.encode(u.getPassword()));

        User saved = repo.save(u);
        return new AuthResponse(saved, "Registration successful");
    }

    public AuthResponse login(String email, String rawPassword) {
        if (email == null || rawPassword == null) {
            throw new RuntimeException("Email and password are required");
        }
        User u = repo.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Support BCrypt hashed passwords, with fallback to plain text for backwards compatibility
        boolean matches = false;
        try {
            matches = passwordEncoder.matches(rawPassword, u.getPassword());
        } catch (Exception ignored) {}

        if (!matches && u.getPassword().equals(rawPassword)) {
            matches = true;
            // Upgrade password to BCrypt hash
            u.setPassword(passwordEncoder.encode(rawPassword));
            repo.save(u);
        }

        if (!matches) {
            throw new RuntimeException("Invalid email or password");
        }

        return new AuthResponse(u, "Login successful");
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateProfile(Long id, User update) {
        User existing = getById(id);
        if (update.getName() != null && !update.getName().isBlank()) {
            existing.setName(update.getName());
        }
        if (update.getPhone() != null) {
            existing.setPhone(update.getPhone());
        }
        if (update.getAddress() != null) {
            existing.setAddress(update.getAddress());
        }
        if (update.getOrganizationType() != null) {
            existing.setOrganizationType(update.getOrganizationType());
        }
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
