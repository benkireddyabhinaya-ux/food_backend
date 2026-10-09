package com.foodwaste.food_waste.repository;

import com.foodwaste.food_waste.entity.Pickup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PickupRepository extends JpaRepository<Pickup, Long> {

    List<Pickup> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);

    List<Pickup> findByDonationDonorIdOrderByCreatedAtDesc(Long donorId);

    Optional<Pickup> findByDonationId(Long donationId);

    long countByStatus(String status);
}
