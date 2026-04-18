package com.rescureat.controller;

/**
 * Request body for creating a reservation (authenticated user is taken from the JWT).
 */
public class ReservationCreateRequest {

    private Long dealId;

    public ReservationCreateRequest() {
    }

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }
}
