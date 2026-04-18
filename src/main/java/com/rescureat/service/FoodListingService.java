package com.rescureat.service;

import com.rescureat.dto.DealNearbyResponse;
import com.rescureat.model.FoodListing;
import com.rescureat.repository.FoodListingRepository;
import com.rescureat.repository.ReservationRepository;
import com.rescureat.util.Haversine;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Service for food deal listings.
 */
@Service
public class FoodListingService {

    private final FoodListingRepository foodListingRepository;
    private final ReservationRepository reservationRepository;

    public FoodListingService(FoodListingRepository foodListingRepository,
                              ReservationRepository reservationRepository) {
        this.foodListingRepository = foodListingRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Returns all food deal listings.
     */
    public List<FoodListing> findAll() {
        return foodListingRepository.findAll();
    }

    /**
     * Deals within {@code radiusKm} of (lat, lng), sorted by distance. Skips deals without coordinates.
     */
    public List<DealNearbyResponse> findNearby(double lat, double lng, double radiusKm) {
        return foodListingRepository.findAll().stream()
                .filter(d -> d.getLatitude() != null && d.getLongitude() != null)
                .map(d -> {
                    double km = Haversine.distanceKm(lat, lng, d.getLatitude(), d.getLongitude());
                    return DealNearbyResponse.from(d, km);
                })
                .filter(r -> r.getDistanceKm() <= radiusKm)
                .sorted(Comparator.comparingDouble(DealNearbyResponse::getDistanceKm))
                .toList();
    }

    /**
     * Returns a single deal by id, or empty if not found.
     */
    public Optional<FoodListing> findById(Long id) {
        return foodListingRepository.findById(id);
    }

    public FoodListing create(FoodListing foodListing) {
        foodListing.setId(null);
        return foodListingRepository.save(foodListing);
    }

    public boolean existsById(Long id) {
        return foodListingRepository.existsById(id);
    }

    @Transactional
    public void deleteDealAndReservations(Long dealId) {
        reservationRepository.deleteByDealId(dealId);
        foodListingRepository.deleteById(dealId);
    }
}
