package com.rescureat.service;

import com.rescureat.model.Reservation;
import com.rescureat.model.User;
import com.rescureat.repository.ReservationRepository;
import com.rescureat.repository.UserRepository;
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

    public List<Reservation> findAll() {
        return reservationRepository.findAllWithUser();
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
}
