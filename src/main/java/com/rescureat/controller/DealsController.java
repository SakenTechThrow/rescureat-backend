package com.rescureat.controller;

import com.rescureat.dto.DealNearbyResponse;
import com.rescureat.model.FoodListing;
import com.rescureat.service.FoodListingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller for the deals API.
 * Exposes GET /api/deals and GET /api/deals/{id}.
 */
@RestController
@RequestMapping("/api/deals")
public class DealsController {

    private final FoodListingService foodListingService;

    public DealsController(FoodListingService foodListingService) {
        this.foodListingService = foodListingService;
    }

    @GetMapping
    public List<FoodListing> getAllDeals() {
        return foodListingService.findAll();
    }

    /**
     * Deals within radius of (lat, lng). Must be registered before GET /{id} so "nearby" is not parsed as id.
     */
    @GetMapping("/nearby")
    public ResponseEntity<?> getDealsNearby(
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng,
            @RequestParam(value = "radiusKm", required = false, defaultValue = "5") Double radiusKm) {
        String err = validateNearbyParams(lat, lng, radiusKm);
        if (err != null) {
            return ResponseEntity.badRequest().body(Map.of("error", err));
        }
        List<DealNearbyResponse> list = foodListingService.findNearby(lat, lng, radiusKm);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodListing> getDealById(@PathVariable Long id) {
        return foodListingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createDeal(@RequestBody FoodListing foodListing) {
        String validationError = validate(foodListing);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(Map.of("error", validationError));
        }

        try {
            FoodListing created = foodListingService.create(foodListing);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeal(@PathVariable Long id) {
        if (!foodListingService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Deal with id " + id + " was not found."));
        }

        foodListingService.deleteDealAndReservations(id);
        return ResponseEntity.noContent().build();
    }

    private String validate(FoodListing foodListing) {
        if (foodListing == null) {
            return "Request body is required.";
        }
        if (isBlank(foodListing.getTitle())) {
            return "title must not be empty.";
        }
        if (isBlank(foodListing.getRestaurantName())) {
            return "restaurantName must not be empty.";
        }
        if (foodListing.getOriginalPrice() == null || foodListing.getOriginalPrice() < 0) {
            return "originalPrice must be greater than or equal to 0.";
        }
        if (foodListing.getDealPrice() == null || foodListing.getDealPrice() < 0) {
            return "dealPrice must be greater than or equal to 0.";
        }
        String locErr = validateLocationFields(foodListing);
        if (locErr != null) {
            return locErr;
        }
        return null;
    }

    private String validateLocationFields(FoodListing foodListing) {
        Double la = foodListing.getLatitude();
        Double lo = foodListing.getLongitude();
        if (la == null && lo == null) {
            return null;
        }
        if (la == null || lo == null) {
            return "latitude and longitude must both be provided together.";
        }
        if (la < -90 || la > 90) {
            return "latitude must be between -90 and 90.";
        }
        if (lo < -180 || lo > 180) {
            return "longitude must be between -180 and 180.";
        }
        return null;
    }

    private String validateNearbyParams(Double lat, Double lng, Double radiusKm) {
        if (lat == null || lng == null) {
            return "lat and lng query parameters are required.";
        }
        if (lat < -90 || lat > 90) {
            return "lat must be between -90 and 90.";
        }
        if (lng < -180 || lng > 180) {
            return "lng must be between -180 and 180.";
        }
        if (radiusKm == null || radiusKm <= 0) {
            return "radiusKm must be greater than 0.";
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
