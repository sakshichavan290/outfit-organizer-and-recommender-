package com.example.outfitorganizer;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    // Static lists to store cart, wishlist, and selling items
    private static List<Dress> cartItems = new ArrayList<>();
    private static List<Dress> wishlistItems = new ArrayList<>();
    private static List<Dress> sellingItems = new ArrayList<>();  // Added selling items list

    // Method to add a dress to the cart
    public static void addToCart(Dress dress) {
        if (!cartItems.contains(dress)) {
            cartItems.add(dress);
        }
    }

    // Method to add a dress to the wishlist
    public static void addToWishlist(Dress dress) {
        if (!wishlistItems.contains(dress)) {
            wishlistItems.add(dress);
        }
    }

    // Method to add a dress to the selling list
    public static void addSellingItem(Dress dress) {
        if (!sellingItems.contains(dress)) {
            sellingItems.add(dress);
        }
    }

    // Method to get all items in the cart
    public static List<Dress> getCartItems() {
        return cartItems;
    }

    // Method to get all items in the wishlist
    public static List<Dress> getWishlistItems() {
        return wishlistItems;
    }

    // Method to get all selling items
    public static List<Dress> getSellingItems() {
        return sellingItems;
    }

    // Method to remove a dress from the selling list
    public static void removeSellingItem(Dress dress) {
        sellingItems.remove(dress);
    }
}
