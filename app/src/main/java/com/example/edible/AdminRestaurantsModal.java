package com.example.edible;

public class AdminRestaurantsModal {

    private String restaurantName;
    private String adminRestaurantImageUrl;

    public AdminRestaurantsModal() {
    }

    public AdminRestaurantsModal(String restaurantName, String adminRestaurantImageUrl) {
        this.restaurantName = restaurantName;
        this.adminRestaurantImageUrl = adminRestaurantImageUrl;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAdminRestaurantImageUrl() {
        return adminRestaurantImageUrl;
    }

    public void setAdminRestaurantImageUrl(String adminRestaurantImageUrl) {
        this.adminRestaurantImageUrl = adminRestaurantImageUrl;
    }




}
