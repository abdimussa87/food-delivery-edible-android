package com.example.edible;

public class OrderModal extends DocumentId {

    private String foodImageUri,foodName,foodPrice,processed;

    public OrderModal() {
    }


    public OrderModal(String foodImageUri, String foodName, String foodPrice, String processed) {
        this.foodImageUri = foodImageUri;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.processed =processed;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
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

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }
}
