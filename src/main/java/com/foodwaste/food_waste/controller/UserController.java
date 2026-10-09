package com.foodwaste.food_waste.controller;

import com.foodwaste.food_waste.dto.AuthResponse;
import com.foodwaste.food_waste.entity.User;
import com.foodwaste.food_waste.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/users", "/users"})
@Tag(name = "Users & Authentication", description = "Endpoints for user management, registration, and login")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user (Donor or Organization)")
    public ResponseEntity<?> register(@RequestBody User u) {
        try {
            AuthResponse res = service.register(u);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and retrieve profile")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            AuthResponse res = service.login(body.get("email"), body.get("password"));
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "List all registered users")
    public List<User> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user profile by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User u) {
        try {
            return ResponseEntity.ok(service.updateProfile(id, u));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
