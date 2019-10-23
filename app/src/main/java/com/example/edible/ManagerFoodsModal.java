package com.example.edible;

public class ManagerFoodsModal {
    private String foodName,foodImageUri;

    public ManagerFoodsModal() {
    }

    public ManagerFoodsModal(String foodName, String foodImageUri) {
        this.foodName = foodName;
        this.foodImageUri = foodImageUri;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }



    public String getFoodImageUri() {
        return foodImageUri;
    }

    public void setFoodImageUri(String foodImageUri) {
        this.foodImageUri = foodImageUri;
    }
}
