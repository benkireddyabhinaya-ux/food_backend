package com.foodwaste.food_waste.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickups")
public class Pickup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donation_id", nullable = false, unique = true)
    private FoodDonation donation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    private User organization;

    @NotBlank(message = "Pickup time is required")
    @Column(nullable = false)
    private String pickupTime;

    @Column(nullable = false)
    private String status = "SCHEDULED"; // "SCHEDULED", "IN_TRANSIT", "COLLECTED", "COMPLETED", "CANCELLED"

    private String driverName;

    private String driverContact;

    private String vehicleNumber;

    @Column(length = 1000)
    private String notes;

    @Column(length = 10)
    private String verificationCode; // Verification PIN for handoff confirmation

    private LocalDateTime completedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "SCHEDULED";
        }
        if (verificationCode == null || verificationCode.isBlank()) {
            // Generate a random 4-digit PIN for pickup verification
            verificationCode = String.valueOf((int) (Math.random() * 9000) + 1000);
        }
    }

    public Pickup() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FoodDonation getDonation() { return donation; }
    public void setDonation(FoodDonation donation) { this.donation = donation; }

    public User getOrganization() { return organization; }
    public void setOrganization(User organization) { this.organization = organization; }

    public String getPickupTime() { return pickupTime; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverContact() { return driverContact; }
    public void setDriverContact(String driverContact) { this.driverContact = driverContact; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
