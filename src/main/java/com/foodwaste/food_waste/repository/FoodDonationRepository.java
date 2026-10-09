package com.foodwaste.food_waste.repository;

import com.foodwaste.food_waste.entity.FoodDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodDonationRepository extends JpaRepository<FoodDonation, Long> {

    List<FoodDonation> findByStatus(String status);

    List<FoodDonation> findByStatusOrderByCreatedAtDesc(String status);

    List<FoodDonation> findAllByOrderByCreatedAtDesc();

    List<FoodDonation> findByDonorIdOrderByCreatedAtDesc(Long donorId);

    List<FoodDonation> findByClaimedByIdOrderByCreatedAtDesc(Long claimedById);

    List<FoodDonation> findByFoodNameContainingIgnoreCase(String name);

    List<FoodDonation> findByLocationContainingIgnoreCase(String location);

    long countByStatus(String status);

    @Query("SELECT f FROM FoodDonation f WHERE " +
           "(:query IS NULL OR LOWER(f.foodName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.location) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.category) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:category IS NULL OR f.category = :category) AND " +
           "(:dietaryType IS NULL OR f.dietaryType = :dietaryType) AND " +
           "(:status IS NULL OR f.status = :status) " +
           "ORDER BY f.createdAt DESC")
    List<FoodDonation> filterDonations(
            @Param("query") String query,
            @Param("category") String category,
            @Param("dietaryType") String dietaryType,
            @Param("status") String status
    );

    @Query("SELECT COALESCE(SUM(f.quantityKg), 0.0) FROM FoodDonation f WHERE f.status = 'COMPLETED'")
    Double sumCompletedQuantityKg();

    @Query("SELECT COALESCE(SUM(f.quantityKg), 0.0) FROM FoodDonation f")
    Double sumTotalQuantityKg();
}
