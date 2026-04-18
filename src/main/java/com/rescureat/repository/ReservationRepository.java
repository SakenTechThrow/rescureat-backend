package com.rescureat.repository;

import com.rescureat.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN FETCH r.user")
    List<Reservation> findAllWithUser();

    void deleteByDealId(Long dealId);
}
