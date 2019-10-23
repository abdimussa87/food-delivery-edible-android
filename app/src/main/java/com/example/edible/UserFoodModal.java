package com.example.edible;

public class UserFoodModal extends DocumentId {

    private String foodImageUri,foodName,foodContent,foodPrice;

    public UserFoodModal(){}

    public UserFoodModal(String foodImageUri, String foodName, String foodContent, String foodPrice) {
        this.foodImageUri = foodImageUri;
        this.foodName = foodName;
        this.foodContent = foodContent;
        this.foodPrice = foodPrice;
    }

    public String getFoodImageUri() {
        return foodImageUri;
    }

    public void setFoodImageUri(String foodImageUri) {
        this.foodImageUri = foodImageUri;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodContent() {
        return foodContent;
    }

    public void setFoodContent(String foodContent) {
        this.foodContent = foodContent;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }
}
