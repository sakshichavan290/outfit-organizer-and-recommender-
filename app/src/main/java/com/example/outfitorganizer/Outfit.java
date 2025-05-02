package com.example.outfitorganizer;

public class Outfit {
    private String id;
    private String name;
    private String shirtImage;
    private String bottomImage;
    private String shoesImage;
    private String accessoriesImage;

    // Default constructor
    public Outfit() {
        // Required for Firebase
    }

    // Constructor with parameters
    public Outfit(String name, String shirtImage, String bottomImage, String shoesImage, String accessoriesImage) {
        this.name = name;
        this.shirtImage = shirtImage;
        this.bottomImage = bottomImage;
        this.shoesImage = shoesImage;
        this.accessoriesImage = accessoriesImage;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShirtImage() {
        return shirtImage;
    }

    public void setShirtImage(String shirtImage) {
        this.shirtImage = shirtImage;
    }

    public String getBottomImage() {
        return bottomImage;
    }

    public void setBottomImage(String bottomImage) {
        this.bottomImage = bottomImage;
    }

    public String getShoesImage() {
        return shoesImage;
    }

    public void setShoesImage(String shoesImage) {
        this.shoesImage = shoesImage;
    }

    public String getAccessoriesImage() {
        return accessoriesImage;
    }

    public void setAccessoriesImage(String accessoriesImage) {
        this.accessoriesImage = accessoriesImage;
    }
}
