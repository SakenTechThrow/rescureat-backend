package com.rescureat.controller;

import java.time.Instant;

/**
 * Response DTO for reservations enriched with deal details.
 */
public class ReservationResponse {

    private Long id;
    private Long dealId;
    private Long userId;
    private String userName;
    private Instant createdAt;
    private String dealTitle;
    private String restaurantName;
    private Double dealPrice;

    public ReservationResponse() {
    }

    public ReservationResponse(Long id, Long dealId, Long userId, String userName, Instant createdAt,
                               String dealTitle, String restaurantName, Double dealPrice) {
        this.id = id;
        this.dealId = dealId;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.dealTitle = dealTitle;
        this.restaurantName = restaurantName;
        this.dealPrice = dealPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Double dealPrice) {
        this.dealPrice = dealPrice;
    }
}
