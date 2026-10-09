package com.foodwaste.food_waste.service;

import com.foodwaste.food_waste.entity.FoodDonation;
import com.foodwaste.food_waste.entity.User;
import com.foodwaste.food_waste.repository.FoodDonationRepository;
import com.foodwaste.food_waste.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodDonationService {

    private final FoodDonationRepository dr;
    private final UserRepository ur;

    public FoodDonationService(FoodDonationRepository dr, UserRepository ur) {
        this.dr = dr;
        this.ur = ur;
    }

    public FoodDonation create(FoodDonation d) {
        if (d.getDonor() == null || d.getDonor().getId() == null) {
            throw new RuntimeException("Donor is required");
        }
        User u = ur.findById(d.getDonor().getId())
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        d.setDonor(u);
        d.setStatus("AVAILABLE");

        // Sensible default for quantityKg if not specified
        if (d.getQuantityKg() == null || d.getQuantityKg() <= 0) {
            d.setQuantityKg(estimateWeight(d.getQuantity()));
        }

        return dr.save(d);
    }

    private double estimateWeight(String qtyStr) {
        if (qtyStr == null) return 5.0;
        try {
            // Attempt to extract digits from string
            String digits = qtyStr.replaceAll("[^0-9.]", "").trim();
            if (!digits.isEmpty()) {
                double val = Double.parseDouble(digits);
                if (qtyStr.toLowerCase().contains("kg")) {
                    return val;
                } else if (qtyStr.toLowerCase().contains("meal") || qtyStr.toLowerCase().contains("plate")) {
                    return Math.max(1.0, val * 0.4); // ~400g per meal
                } else {
                    return Math.max(1.0, val);
                }
            }
        } catch (Exception ignored) {}
        return 5.0;
    }

    public List<FoodDonation> all() {
        return dr.findAllByOrderByCreatedAtDesc();
    }

    public List<FoodDonation> available() {
        return dr.findByStatusOrderByCreatedAtDesc("AVAILABLE");
    }

    public List<FoodDonation> byDonor(Long id) {
        return dr.findByDonorIdOrderByCreatedAtDesc(id);
    }

    public List<FoodDonation> byOrganization(Long id) {
        return dr.findByClaimedByIdOrderByCreatedAtDesc(id);
    }

    public FoodDonation get(Long id) {
        return dr.findById(id).orElseThrow(() -> new RuntimeException("Donation not found"));
    }

    public List<FoodDonation> search(String query, String category, String dietaryType, String status) {
        String q = (query != null && !query.isBlank()) ? query.trim() : null;
        String cat = (category != null && !category.isBlank() && !"ALL".equalsIgnoreCase(category)) ? category.trim() : null;
        String diet = (dietaryType != null && !dietaryType.isBlank() && !"ALL".equalsIgnoreCase(dietaryType)) ? dietaryType.trim() : null;
        String st = (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) ? status.trim() : null;

        return dr.filterDonations(q, cat, diet, st);
    }

    public FoodDonation claim(Long id, Long orgId) {
        FoodDonation d = get(id);
        if (!"AVAILABLE".equalsIgnoreCase(d.getStatus())) {
            throw new RuntimeException("Donation is not available for claim (Current status: " + d.getStatus() + ")");
        }
        User org = ur.findById(orgId).orElseThrow(() -> new RuntimeException("Organization not found"));
        if (!"ORGANIZATION".equalsIgnoreCase(org.getRole())) {
            throw new RuntimeException("User must be an organization to claim donations");
        }
        d.setClaimedBy(org);
        d.setStatus("CLAIMED");
        return dr.save(d);
    }

    public FoodDonation update(Long id, FoodDonation x) {
        FoodDonation d = get(id);
        if (x.getFoodName() != null) d.setFoodName(x.getFoodName());
        if (x.getQuantity() != null) {
            d.setQuantity(x.getQuantity());
            if (x.getQuantityKg() != null && x.getQuantityKg() > 0) {
                d.setQuantityKg(x.getQuantityKg());
            } else {
                d.setQuantityKg(estimateWeight(x.getQuantity()));
            }
        }
        if (x.getLocation() != null) d.setLocation(x.getLocation());
        if (x.getCity() != null) d.setCity(x.getCity());
        if (x.getCategory() != null) d.setCategory(x.getCategory());
        if (x.getDietaryType() != null) d.setDietaryType(x.getDietaryType());
        if (x.getPickupTime() != null) d.setPickupTime(x.getPickupTime());
        if (x.getExpiryTime() != null) d.setExpiryTime(x.getExpiryTime());
        if (x.getUrgency() != null) d.setUrgency(x.getUrgency());
        if (x.getImageUrl() != null) d.setImageUrl(x.getImageUrl());
        if (x.getStorageInstructions() != null) d.setStorageInstructions(x.getStorageInstructions());
        if (x.getNotes() != null) d.setNotes(x.getNotes());
        return dr.save(d);
    }

    public FoodDonation updateStatus(Long id, String status) {
        FoodDonation d = get(id);
        d.setStatus(status.toUpperCase());
        return dr.save(d);
    }

    public void delete(Long id) {
        dr.deleteById(id);
    }
}
