package com.foodwaste.food_waste.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping({"/api", ""})
@Tag(name = "Health & System", description = "Endpoints for service health monitoring and uptime checks")
public class HealthController {

    @GetMapping({"/health", "/api/health"})
    @Operation(summary = "Health check probe for Render deployment")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Food Waste Reduction System API",
                "version", "1.0.0",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
