package com.example.outfitorganizer;

public class SellItem {
    private String itemName;
    private String itemPrice;
    private String imageUri; // Change int to String

    // Updated constructor
    public SellItem(String itemName, String itemPrice, String imageUri) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.imageUri = imageUri;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getImageUri() {
        return imageUri;
    }
}
