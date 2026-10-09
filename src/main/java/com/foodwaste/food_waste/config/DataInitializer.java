package com.foodwaste.food_waste.config;

import com.foodwaste.food_waste.entity.FoodDonation;
import com.foodwaste.food_waste.entity.Pickup;
import com.foodwaste.food_waste.entity.User;
import com.foodwaste.food_waste.repository.FoodDonationRepository;
import com.foodwaste.food_waste.repository.PickupRepository;
import com.foodwaste.food_waste.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepo;
    private final FoodDonationRepository donationRepo;
    private final PickupRepository pickupRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepo,
                           FoodDonationRepository donationRepo,
                           PickupRepository pickupRepo,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.donationRepo = donationRepo;
        this.pickupRepo = pickupRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) {
            log.info("Database already initialized with users (count: {}). Skipping seed.", userRepo.count());
            return;
        }

        log.info("Seeding initial demo data for Food Waste Reduction System...");

        String defaultPass = passwordEncoder.encode("password123");

        // Create Donors
        User donor1 = new User("The Green Bistro", "donor@bistro.com", defaultPass, "DONOR", "+1 (555) 234-5678", "120 Market St, Downtown", "Restaurant");
        User donor2 = new User("Artisan Daily Bakery", "donor@bakery.com", defaultPass, "DONOR", "+1 (555) 876-5432", "45 Baker Ave, Westside", "Bakery");
        User donor3 = new User("Metro Fresh Grocers", "donor@metrofresh.com", defaultPass, "DONOR", "+1 (555) 998-1122", "880 Central Blvd, Midtown", "Supermarket");

        userRepo.save(donor1);
        userRepo.save(donor2);
        userRepo.save(donor3);

        // Create Organizations
        User org1 = new User("Hope Community Food Bank", "org@foodbank.org", defaultPass, "ORGANIZATION", "+1 (555) 345-6789", "300 Hope Way, East District", "Food Bank");
        User org2 = new User("St. Jude Care Shelter", "org@shelter.org", defaultPass, "ORGANIZATION", "+1 (555) 654-3210", "750 Shelter Rd, Southside", "Shelter");

        userRepo.save(org1);
        userRepo.save(org2);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Seed Food Donations
        FoodDonation d1 = new FoodDonation();
        d1.setFoodName("Fresh Vegetable Biryani & Lentil Dal");
        d1.setCategory("Cooked Meals");
        d1.setQuantity("40 portions");
        d1.setQuantityKg(16.0);
        d1.setDietaryType("Vegetarian");
        d1.setLocation("120 Market St, Downtown");
        d1.setCity("Metropolis");
        d1.setPickupTime(now.plusHours(2).format(fmt));
        d1.setExpiryTime(now.plusHours(6).format(fmt));
        d1.setUrgency("NORMAL");
        d1.setStatus("AVAILABLE");
        d1.setImageUrl("https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=800&auto=format&fit=crop&q=60");
        d1.setStorageInstructions("Hot insulated containers. Safe to distribute immediately.");
        d1.setNotes("Prepared fresh for an afternoon corporate luncheon, completely untouched.");
        d1.setDonor(donor1);
        donationRepo.save(d1);

        FoodDonation d2 = new FoodDonation();
        d2.setFoodName("Artisan Sourdough Loaves & Croissants");
        d2.setCategory("Bakery & Bread");
        d2.setQuantity("35 items");
        d2.setQuantityKg(9.0);
        d2.setDietaryType("Vegetarian");
        d2.setLocation("45 Baker Ave, Westside");
        d2.setCity("Metropolis");
        d2.setPickupTime(now.plusHours(4).format(fmt));
        d2.setExpiryTime(now.plusHours(18).format(fmt));
        d2.setUrgency("URGENT");
        d2.setStatus("AVAILABLE");
        d2.setImageUrl("https://images.unsplash.com/photo-1509440159596-0249088772ff?w=800&auto=format&fit=crop&q=60");
        d2.setStorageInstructions("Keep in dry packaging at room temperature.");
        d2.setNotes("Baked this morning. Perfectly fresh surplus from daily bake.");
        d2.setDonor(donor2);
        donationRepo.save(d2);

        FoodDonation d3 = new FoodDonation();
        d3.setFoodName("Organic Apples, Carrots & Spinach Crates");
        d3.setCategory("Fresh Produce");
        d3.setQuantity("25 kg");
        d3.setQuantityKg(25.0);
        d3.setDietaryType("Vegan");
        d3.setLocation("880 Central Blvd, Midtown");
        d3.setCity("Metropolis");
        d3.setPickupTime(now.plusHours(5).format(fmt));
        d3.setExpiryTime(now.plusDays(2).format(fmt));
        d3.setUrgency("NORMAL");
        d3.setStatus("AVAILABLE");
        d3.setImageUrl("https://images.unsplash.com/photo-1610348725531-843dff563e2c?w=800&auto=format&fit=crop&q=60");
        d3.setStorageInstructions("Refrigerated or cool dry display crates.");
        d3.setNotes("Crisp and fresh surplus produce ready for pantry distribution.");
        d3.setDonor(donor3);
        donationRepo.save(d3);

        // Seed a Scheduled Donation + Pickup
        FoodDonation d4 = new FoodDonation();
        d4.setFoodName("Penne Pasta Primavera & Garlic Bread");
        d4.setCategory("Cooked Meals");
        d4.setQuantity("30 meals");
        d4.setQuantityKg(12.0);
        d4.setDietaryType("Vegetarian");
        d4.setLocation("120 Market St, Downtown");
        d4.setCity("Metropolis");
        d4.setPickupTime(now.plusHours(1).format(fmt));
        d4.setExpiryTime(now.plusHours(5).format(fmt));
        d4.setUrgency("URGENT");
        d4.setStatus("SCHEDULED");
        d4.setImageUrl("https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=800&auto=format&fit=crop&q=60");
        d4.setDonor(donor1);
        d4.setClaimedBy(org1);
        donationRepo.save(d4);

        Pickup p1 = new Pickup();
        p1.setDonation(d4);
        p1.setOrganization(org1);
        p1.setPickupTime(now.plusHours(1).format(fmt));
        p1.setStatus("SCHEDULED");
        p1.setDriverName("Alex Morgan");
        p1.setDriverContact("+1 (555) 777-8899");
        p1.setVehicleNumber("VAN-402");
        p1.setNotes("Food bank refrigerated transit van dispatched.");
        p1.setVerificationCode("4821");
        pickupRepo.save(p1);

        // Seed a Completed Donation + Pickup
        FoodDonation d5 = new FoodDonation();
        d5.setFoodName("Assorted Sandwiches & Fruit Cups");
        d5.setCategory("Cooked Meals");
        d5.setQuantity("50 packages");
        d5.setQuantityKg(20.0);
        d5.setDietaryType("Vegetarian");
        d5.setLocation("45 Baker Ave, Westside");
        d5.setCity("Metropolis");
        d5.setPickupTime(now.minusHours(4).format(fmt));
        d5.setExpiryTime(now.plusHours(2).format(fmt));
        d5.setStatus("COMPLETED");
        d5.setImageUrl("https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=800&auto=format&fit=crop&q=60");
        d5.setDonor(donor2);
        d5.setClaimedBy(org2);
        donationRepo.save(d5);

        Pickup p2 = new Pickup();
        p2.setDonation(d5);
        p2.setOrganization(org2);
        p2.setPickupTime(now.minusHours(3).format(fmt));
        p2.setStatus("COMPLETED");
        p2.setDriverName("Maria Santos");
        p2.setDriverContact("+1 (555) 333-2211");
        p2.setVehicleNumber("TRK-105");
        p2.setNotes("Delivered to shelter dining hall for evening supper.");
        p2.setVerificationCode("9104");
        p2.setCompletedAt(now.minusHours(2));
        pickupRepo.save(p2);

        log.info("Successfully seeded demo data: 5 users, 5 donations, 2 pickups.");
    }
}
