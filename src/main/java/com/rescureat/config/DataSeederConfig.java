package com.rescureat.config;

import com.rescureat.model.FoodListing;
import com.rescureat.repository.FoodListingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds starter deals for a fresh database.
 */
@Configuration
public class DataSeederConfig {

    @Bean
    CommandLineRunner seedDeals(FoodListingRepository foodListingRepository) {
        return args -> {
            if (foodListingRepository.count() > 0) {
                return;
            }

            foodListingRepository.save(new FoodListing(null, "Lunch Special",
                    "Fresh salad and sandwich combo", "Green Bites", 12.99, 5.99,
                    43.2372, 76.9288, "Bostandyk", "KBTU"));
            foodListingRepository.save(new FoodListing(null, "Evening Pastries",
                    "Assorted pastries - 50% off", "Bakery Corner", 8.50, 4.25,
                    43.2520, 76.9215, "Almaly", "Narxoz"));
            foodListingRepository.save(new FoodListing(null, "Dinner Box",
                    "Surprise dinner box, enough for 2", "Kitchen Hub", 24.00, 9.99,
                    43.2410, 76.9150, "Bostandyk", "SDU"));
        };
    }
}
