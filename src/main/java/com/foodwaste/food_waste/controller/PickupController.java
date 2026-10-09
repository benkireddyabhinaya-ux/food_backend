package com.foodwaste.food_waste.controller;

import com.foodwaste.food_waste.entity.Pickup;
import com.foodwaste.food_waste.service.PickupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/pickups", "/pickups"})
@Tag(name = "Pickups & Distribution", description = "Endpoints for scheduling, tracking, and confirming food pickups")
public class PickupController {

    private final PickupService service;

    public PickupController(PickupService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Schedule a pickup for a food donation")
    public ResponseEntity<?> create(
            @RequestParam(required = false) Long donationId,
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) String pickupTime,
            @RequestParam(required = false) String driverName,
            @RequestParam(required = false) String driverContact,
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(required = false) String notes,
            @RequestBody(required = false) Map<String, Object> body) {
        try {
            Long dId = donationId;
            Long oId = organizationId;
            String time = pickupTime;
            String driver = driverName;
            String contact = driverContact;
            String vehicle = vehicleNumber;
            String noteText = notes;

            if (body != null) {
                if (dId == null && body.get("donationId") != null) {
                    dId = Long.valueOf(body.get("donationId").toString());
                }
                if (oId == null && body.get("organizationId") != null) {
                    oId = Long.valueOf(body.get("organizationId").toString());
                }
                if (time == null && body.get("pickupTime") != null) {
                    time = body.get("pickupTime").toString();
                }
                if (driver == null && body.get("driverName") != null) {
                    driver = body.get("driverName").toString();
                }
                if (contact == null && body.get("driverContact") != null) {
                    contact = body.get("driverContact").toString();
                }
                if (vehicle == null && body.get("vehicleNumber") != null) {
                    vehicle = body.get("vehicleNumber").toString();
                }
                if (noteText == null && body.get("notes") != null) {
                    noteText = body.get("notes").toString();
                }
            }

            if (dId == null || oId == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "donationId and organizationId are required"));
            }

            Pickup p = service.create(dId, oId, time, driver, contact, vehicle, noteText);
            return ResponseEntity.status(HttpStatus.CREATED).body(p);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all scheduled pickups")
    public List<Pickup> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pickup details by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.get(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get all pickups for a specific organization")
    public List<Pickup> organization(@PathVariable Long organizationId) {
        return service.byOrganization(organizationId);
    }

    @GetMapping("/donor/{donorId}")
    @Operation(summary = "Get all pickups for a specific donor")
    public List<Pickup> donor(@PathVariable Long donorId) {
        return service.byDonor(donorId);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update pickup status (e.g. IN_TRANSIT, COLLECTED, COMPLETED)")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam(required = false) String status,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String s = status;
            if (s == null && body != null) {
                s = body.get("status");
            }
            if (s == null || s.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Status is required"));
            }
            return ResponseEntity.ok(service.updateStatus(id, s));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify handoff PIN and mark pickup as COMPLETED")
    public ResponseEntity<?> verify(
            @PathVariable Long id,
            @RequestParam(required = false) String code,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String pin = code;
            if (pin == null && body != null) {
                pin = body.get("code");
            }
            if (pin == null || pin.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Verification PIN is required"));
            }
            return ResponseEntity.ok(service.verifyAndComplete(id, pin));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel or delete a pickup")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
