package com.foodwaste.food_waste.controller;

import com.foodwaste.food_waste.entity.FoodDonation;
import com.foodwaste.food_waste.service.FoodDonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/donations", "/donations"})
@Tag(name = "Food Donations", description = "Endpoints for creating, browsing, claiming, and managing food donations")
public class FoodDonationController {

    private final FoodDonationService service;

    public FoodDonationController(FoodDonationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Post a new food donation")
    public ResponseEntity<?> create(@RequestBody FoodDonation d) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(d));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all food donations")
    public List<FoodDonation> all() {
        return service.all();
    }

    @GetMapping("/available")
    @Operation(summary = "Get all currently available food donations")
    public List<FoodDonation> available() {
        return service.available();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a specific donation")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.get(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/donor/{donorId}")
    @Operation(summary = "Get all donations created by a specific donor")
    public List<FoodDonation> donor(@PathVariable Long donorId) {
        return service.byDonor(donorId);
    }

    @GetMapping("/organization/{orgId}")
    @Operation(summary = "Get all donations claimed by a specific organization")
    public List<FoodDonation> organization(@PathVariable Long orgId) {
        return service.byOrganization(orgId);
    }

    @GetMapping("/search")
    @Operation(summary = "Search and filter food donations")
    public List<FoodDonation> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String dietaryType,
            @RequestParam(required = false) String status) {
        String q = (query != null && !query.isBlank()) ? query : name;
        return service.search(q, category, dietaryType, status);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update donation information")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FoodDonation d) {
        try {
            return ResponseEntity.ok(service.update(id, d));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/accept")
    @Operation(summary = "Legacy accept endpoint (marks donation as ACCEPTED)")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.updateStatus(id, "ACCEPTED"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/claim")
    @Operation(summary = "Claim a donation for an organization")
    public ResponseEntity<?> claim(@PathVariable Long id, @RequestParam Long organizationId) {
        try {
            return ResponseEntity.ok(service.claim(id, organizationId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update status of a donation")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            return ResponseEntity.ok(service.updateStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete donation by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
