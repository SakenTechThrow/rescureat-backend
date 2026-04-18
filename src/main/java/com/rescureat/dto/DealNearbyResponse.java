package com.rescureat.dto;

import com.rescureat.model.FoodListing;

/**
 * A deal plus distance from a query point (for GET /api/deals/nearby).
 */
public class DealNearbyResponse {

    private Long id;
    private String title;
    private String description;
    private String restaurantName;
    private Double originalPrice;
    private Double dealPrice;
    private Double latitude;
    private Double longitude;
    private String district;
    private String nearUniversity;
    private double distanceKm;

    public static DealNearbyResponse from(FoodListing deal, double distanceKm) {
        DealNearbyResponse r = new DealNearbyResponse();
        r.id = deal.getId();
        r.title = deal.getTitle();
        r.description = deal.getDescription();
        r.restaurantName = deal.getRestaurantName();
        r.originalPrice = deal.getOriginalPrice();
        r.dealPrice = deal.getDealPrice();
        r.latitude = deal.getLatitude();
        r.longitude = deal.getLongitude();
        r.district = deal.getDistrict();
        r.nearUniversity = deal.getNearUniversity();
        r.distanceKm = Math.round(distanceKm * 1000.0) / 1000.0;
        return r;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public Double getDealPrice() {
        return dealPrice;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDistrict() {
        return district;
    }

    public String getNearUniversity() {
        return nearUniversity;
    }

    public double getDistanceKm() {
        return distanceKm;
    }
}
