package com.example.edible;

public class MainActivityModal extends DocumentId {

    private String adminRestaurantImageUrl,restaurantDescription,restaurantLocation, restaurantName;

    public MainActivityModal() {
    }

    public MainActivityModal(String adminRestaurantImageUrl, String restaurantDescription, String restaurantLocation, String restaurantName) {
        this.adminRestaurantImageUrl = adminRestaurantImageUrl;
        this.restaurantDescription = restaurantDescription;
        this.restaurantLocation = restaurantLocation;
        this.restaurantName = restaurantName;
    }

    public String getAdminRestaurantImageUrl() {
        return adminRestaurantImageUrl;
    }

    public void setAdminRestaurantImageUrl(String adminRestaurantImageUrl) {
        this.adminRestaurantImageUrl = adminRestaurantImageUrl;
    }

    public String getRestaurantDescription() {
        return restaurantDescription;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        this.restaurantDescription = restaurantDescription;
    }

    public String getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(String restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
