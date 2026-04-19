package com.rescureat.service;

import com.rescureat.model.Reservation;
import com.rescureat.model.User;
import com.rescureat.repository.ReservationRepository;
import com.rescureat.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for reservation operations.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> findForCurrentUser() {
        User user = requireCurrentUser();
        return reservationRepository.findAllWithUserByUserId(user.getId());
    }

    public Reservation create(Long dealId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Reservation reservation = new Reservation();
        reservation.setDealId(dealId);
        reservation.setUser(user);
        reservation.setCreatedAt(null);
        return reservationRepository.save(reservation);
    }

    private User requireCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalStateException("Unauthenticated user.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found."));
    }
}
