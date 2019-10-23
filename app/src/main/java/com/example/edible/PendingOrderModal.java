package com.example.edible;

public class PendingOrderModal extends DocumentId {
    private String foodName, foodPrice, orderedBy,ordererUsername;

    public PendingOrderModal(){}

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

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public String getOrdererUsername() {
        return ordererUsername;
    }

    public void setOrdererUsername(String ordererUsername) {
        this.ordererUsername = ordererUsername;
    }
}
