package com.rescureat.controller;

import com.rescureat.model.FoodListing;
import com.rescureat.model.Reservation;
import com.rescureat.security.AppUserDetails;
import com.rescureat.service.FoodListingService;
import com.rescureat.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for reservation endpoints.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/reservations")
public class ReservationsController {

    private final ReservationService reservationService;
    private final FoodListingService foodListingService;

    public ReservationsController(ReservationService reservationService,
                                  FoodListingService foodListingService) {
        this.reservationService = reservationService;
        this.foodListingService = foodListingService;
    }

    @PostMapping
    public ResponseEntity<?> createReservation(
            @AuthenticationPrincipal AppUserDetails principal,
            @RequestBody ReservationCreateRequest request) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        String validationError = validate(request);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(Map.of("error", validationError));
        }

        if (!foodListingService.existsById(request.getDealId())) {
            return ResponseEntity.badRequest().body(Map.of("error", "dealId does not exist."));
        }

        Reservation created = reservationService.create(request.getDealId(), principal.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<ReservationResponse> getAllReservations() {
        return reservationService.findForCurrentUser().stream()
                .map(reservation -> {
                    Optional<FoodListing> deal = foodListingService.findById(reservation.getDealId());
                    Long userId = reservation.getUser() != null ? reservation.getUser().getId() : null;
                    String userName = reservation.getUser() != null ? reservation.getUser().getName() : null;
                    return new ReservationResponse(
                            reservation.getId(),
                            reservation.getDealId(),
                            userId,
                            userName,
                            reservation.getCreatedAt(),
                            deal.map(FoodListing::getTitle).orElse(null),
                            deal.map(FoodListing::getRestaurantName).orElse(null),
                            deal.map(FoodListing::getDealPrice).orElse(null)
                    );
                })
                .toList();
    }

    private String validate(ReservationCreateRequest request) {
        if (request == null) {
            return "Request body is required.";
        }
        if (request.getDealId() == null) {
            return "dealId is required.";
        }
        return null;
    }
}
