package com.foodwaste.food_waste.service;

import com.foodwaste.food_waste.entity.FoodDonation;
import com.foodwaste.food_waste.entity.Pickup;
import com.foodwaste.food_waste.entity.User;
import com.foodwaste.food_waste.repository.FoodDonationRepository;
import com.foodwaste.food_waste.repository.PickupRepository;
import com.foodwaste.food_waste.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PickupService {

    private final PickupRepository pr;
    private final FoodDonationRepository dr;
    private final UserRepository ur;

    public PickupService(PickupRepository pr, FoodDonationRepository dr, UserRepository ur) {
        this.pr = pr;
        this.dr = dr;
        this.ur = ur;
    }

    public Pickup create(Long donationId, Long orgId, String time, String driverName, String driverContact, String vehicleNumber, String notes) {
        FoodDonation d = dr.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        User o = ur.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        if (!"ORGANIZATION".equalsIgnoreCase(o.getRole())) {
            throw new RuntimeException("Selected user is not an organization");
        }

        d.setStatus("SCHEDULED");
        d.setClaimedBy(o);
        dr.save(d);

        Pickup p = new Pickup();
        p.setDonation(d);
        p.setOrganization(o);
        p.setPickupTime(time != null && !time.isBlank() ? time : d.getPickupTime());
        p.setStatus("SCHEDULED");
        p.setDriverName(driverName != null && !driverName.isBlank() ? driverName : o.getName());
        p.setDriverContact(driverContact != null ? driverContact : o.getPhone());
        p.setVehicleNumber(vehicleNumber);
        p.setNotes(notes);

        return pr.save(p);
    }

    public List<Pickup> all() {
        return pr.findAll();
    }

    public List<Pickup> byOrganization(Long id) {
        return pr.findByOrganizationIdOrderByCreatedAtDesc(id);
    }

    public List<Pickup> byDonor(Long donorId) {
        return pr.findByDonationDonorIdOrderByCreatedAtDesc(donorId);
    }

    public Pickup get(Long id) {
        return pr.findById(id).orElseThrow(() -> new RuntimeException("Pickup not found"));
    }

    public Pickup updateStatus(Long id, String status) {
        Pickup p = get(id);
        String upperStatus = status.toUpperCase();
        p.setStatus(upperStatus);

        if ("COMPLETED".equalsIgnoreCase(upperStatus)) {
            p.setCompletedAt(LocalDateTime.now());
            p.getDonation().setStatus("COMPLETED");
            dr.save(p.getDonation());
        } else if ("COLLECTED".equalsIgnoreCase(upperStatus)) {
            p.getDonation().setStatus("COLLECTED");
            dr.save(p.getDonation());
        } else if ("IN_TRANSIT".equalsIgnoreCase(upperStatus)) {
            p.getDonation().setStatus("IN_TRANSIT");
            dr.save(p.getDonation());
        }

        return pr.save(p);
    }

    public Pickup verifyAndComplete(Long id, String code) {
        Pickup p = get(id);
        if (p.getVerificationCode() != null && !p.getVerificationCode().equals(code.trim())) {
            throw new RuntimeException("Invalid verification PIN");
        }
        return updateStatus(id, "COMPLETED");
    }

    public void delete(Long id) {
        Pickup p = get(id);
        if (p.getDonation() != null) {
            p.getDonation().setStatus("AVAILABLE");
            p.getDonation().setClaimedBy(null);
            dr.save(p.getDonation());
        }
        pr.deleteById(id);
    }
}
