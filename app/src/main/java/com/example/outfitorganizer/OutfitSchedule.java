package com.example.outfitorganizer;

public class OutfitSchedule {

    private String outfitName;
    private String outfitImageUrl;

    public OutfitSchedule() {
        // Default constructor required for Firebase
    }

    public OutfitSchedule(String outfitName, String outfitImageUrl) {
        this.outfitName = outfitName;
        this.outfitImageUrl = outfitImageUrl;
    }

    public String getOutfitName() {
        return outfitName;
    }

    public void setOutfitName(String outfitName) {
        this.outfitName = outfitName;
    }

    public String getOutfitImageUrl() {
        return outfitImageUrl;
    }

    public void setOutfitImageUrl(String outfitImageUrl) {
        this.outfitImageUrl = outfitImageUrl;
    }
}
