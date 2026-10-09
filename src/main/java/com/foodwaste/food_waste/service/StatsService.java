package com.foodwaste.food_waste.service;

import com.foodwaste.food_waste.dto.ImpactStatsDto;
import com.foodwaste.food_waste.repository.FoodDonationRepository;
import com.foodwaste.food_waste.repository.PickupRepository;
import com.foodwaste.food_waste.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class StatsService {

    private final FoodDonationRepository donationRepo;
    private final PickupRepository pickupRepo;
    private final UserRepository userRepo;

    public StatsService(FoodDonationRepository donationRepo, PickupRepository pickupRepo, UserRepository userRepo) {
        this.donationRepo = donationRepo;
        this.pickupRepo = pickupRepo;
        this.userRepo = userRepo;
    }

    public ImpactStatsDto getImpactSummary() {
        ImpactStatsDto stats = new ImpactStatsDto();

        long totalDonations = donationRepo.count();
        long activeDonations = donationRepo.countByStatus("AVAILABLE") + donationRepo.countByStatus("CLAIMED") + donationRepo.countByStatus("SCHEDULED");
        long completedPickups = pickupRepo.countByStatus("COMPLETED");

        Double completedKg = donationRepo.sumCompletedQuantityKg();
        if (completedKg == null || completedKg == 0.0) {
            // Also consider all non-cancelled donations if newly started
            Double totalKg = donationRepo.sumTotalQuantityKg();
            completedKg = (totalKg != null) ? totalKg * 0.6 : 0.0;
        }

        // Calculations:
        // 1 kg of food = ~2.2 meals
        // 1 kg of food saved prevents 2.5 kg of CO2 equivalent emissions
        double kgSaved = BigDecimal.valueOf(completedKg).setScale(1, RoundingMode.HALF_UP).doubleValue();
        long mealsSaved = Math.round(kgSaved * 2.2);
        double co2Saved = BigDecimal.valueOf(kgSaved * 2.5).setScale(1, RoundingMode.HALF_UP).doubleValue();

        stats.setTotalDonations(totalDonations);
        stats.setActiveDonations(activeDonations);
        stats.setCompletedPickups(completedPickups);
        stats.setTotalKgFoodSaved(kgSaved);
        stats.setTotalMealsSaved(mealsSaved);
        stats.setTotalCo2PreventedKg(co2Saved);
        stats.setTotalDonors(userRepo.countByRole("DONOR"));
        stats.setTotalOrganizations(userRepo.countByRole("ORGANIZATION"));

        return stats;
    }
}
