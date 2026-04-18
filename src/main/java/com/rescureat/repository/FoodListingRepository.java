package com.rescureat.repository;

import com.rescureat.model.FoodListing;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for deal entities.
 */
public interface FoodListingRepository extends JpaRepository<FoodListing, Long> {
}
