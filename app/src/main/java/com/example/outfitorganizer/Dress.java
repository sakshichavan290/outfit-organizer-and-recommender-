package com.example.outfitorganizer;

public class Dress {
    private int imageResId = -1;  // Default to an invalid resource ID
    private String imageUri;  // Store image URI as String
    private String title;
    private String price;
    private String discount;

    // Constructor for drawable resource images
    public Dress(int imageResId, String title, String price, String discount) {
        this.imageResId = imageResId;
        this.imageUri = null;
        this.title = title;
        this.price = price;
        this.discount = discount;
    }

    // Constructor for URI images (Stored as String)
    public Dress(String imageUri, String title, String price, String discount) {
        this.imageUri = imageUri;
        this.imageResId = -1; // Ensure resource ID is invalid when using URI
        this.title = title;
        this.price = price;
        this.discount = discount;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public boolean hasImageUri() {
        return imageUri != null;
    }
}
