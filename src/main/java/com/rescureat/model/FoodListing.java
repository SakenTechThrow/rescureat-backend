package com.rescureat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a food deal listing (surplus food offered at a discount).
 * Used as the domain entity for the deals API.
 */
@Entity
@Table(name = "food_listings")
public class FoodListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String restaurantName;
    private Double originalPrice;
    private Double dealPrice;

    /** WGS84 latitude in degrees. */
    private Double latitude;

    /** WGS84 longitude in degrees. */
    private Double longitude;

    /** City district label (e.g. Almaly, Bostandyk). */
    private String district;

    /** Nearby university label (e.g. SDU, KBTU). */
    private String nearUniversity;

    public FoodListing() {
    }

    public FoodListing(Long id, String title, String description, String restaurantName,
                       Double originalPrice, Double dealPrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restaurantName = restaurantName;
        this.originalPrice = originalPrice;
        this.dealPrice = dealPrice;
    }

    public FoodListing(Long id, String title, String description, String restaurantName,
                       Double originalPrice, Double dealPrice,
                       Double latitude, Double longitude, String district, String nearUniversity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.restaurantName = restaurantName;
        this.originalPrice = originalPrice;
        this.dealPrice = dealPrice;
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
        this.nearUniversity = nearUniversity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNearUniversity() {
        return nearUniversity;
    }

    public void setNearUniversity(String nearUniversity) {
        this.nearUniversity = nearUniversity;
    }
}
