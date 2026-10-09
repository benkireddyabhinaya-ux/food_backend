package com.foodwaste.food_waste.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_donations")
public class FoodDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Food name is required")
    @Column(nullable = false)
    private String foodName;

    private String category; // e.g. "Cooked Meals", "Bakery & Bread", "Fresh Produce", "Dairy & Packaged", "Groceries"

    @NotBlank(message = "Quantity is required")
    @Column(nullable = false)
    private String quantity; // e.g. "30 meals", "15 kg", "4 boxes"

    private Double quantityKg = 5.0; // Estimated weight in kg for carbon/impact math

    private String dietaryType; // "Vegetarian", "Vegan", "Non-Vegetarian", "Halal"

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    private String city;

    @NotBlank(message = "Pickup time is required")
    @Column(nullable = false)
    private String pickupTime;

    private String expiryTime;

    private String urgency; // "NORMAL", "URGENT", "CRITICAL"

    @Column(nullable = false)
    private String status = "AVAILABLE"; // "AVAILABLE", "CLAIMED", "SCHEDULED", "COLLECTED", "COMPLETED", "CANCELLED"

    @Column(length = 1000)
    private String imageUrl;

    private String storageInstructions; // e.g. "Keep refrigerated", "Keep warm", "Room temperature"

    @Column(length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "claimed_by_id")
    private User claimedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "AVAILABLE";
        }
        if (quantityKg == null || quantityKg <= 0) {
            quantityKg = 5.0;
        }
        if (category == null || category.isBlank()) {
            category = "Cooked Meals";
        }
        if (dietaryType == null || dietaryType.isBlank()) {
            dietaryType = "Vegetarian";
        }
        if (urgency == null || urgency.isBlank()) {
            urgency = "NORMAL";
        }
    }

    public FoodDonation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public Double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(Double quantityKg) { this.quantityKg = quantityKg; }

    public String getDietaryType() { return dietaryType; }
    public void setDietaryType(String dietaryType) { this.dietaryType = dietaryType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPickupTime() { return pickupTime; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }

    public String getExpiryTime() { return expiryTime; }
    public void setExpiryTime(String expiryTime) { this.expiryTime = expiryTime; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getStorageInstructions() { return storageInstructions; }
    public void setStorageInstructions(String storageInstructions) { this.storageInstructions = storageInstructions; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getDonor() { return donor; }
    public void setDonor(User donor) { this.donor = donor; }

    public User getClaimedBy() { return claimedBy; }
    public void setClaimedBy(User claimedBy) { this.claimedBy = claimedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
