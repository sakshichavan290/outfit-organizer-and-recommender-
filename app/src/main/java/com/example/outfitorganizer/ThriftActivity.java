package com.example.outfitorganizer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.DressAdapter;

public class ThriftActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELL_ITEM = 101;

    RecyclerView recyclerView;
    DressAdapter adapter;
    List<Dress> dressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrift);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Handle back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize dress list
        dressList = new ArrayList<>();
        populateDressList();

        // Load user-uploaded thrift items
        loadUserThriftItems();

        // Set up the adapter
        adapter = new DressAdapter(this, dressList);
        recyclerView.setAdapter(adapter);

        // Handle click actions for Add to Cart, Wishlist, and Buy Now
        adapter.setOnDressActionListener((dress, action) -> {
            if (action.equals("addToCart")) {
                DataManager.addToCart(dress);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            } else if (action.equals("addToWishlist")) {
                DataManager.addToWishlist(dress);
                Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show();
            } else if (action.equals("buyNow")) {
                Intent buyNowIntent = new Intent(this, BuyNowActivity.class);
                buyNowIntent.putExtra("dressImage", dress.getImageResId());
                buyNowIntent.putExtra("dressTitle", dress.getTitle());
                buyNowIntent.putExtra("dressPrice", dress.getPrice());
                startActivity(buyNowIntent);
            }
        });
    }

    // Populate the default dress list
    private void populateDressList() {
        dressList.add(new Dress(R.drawable.dress1, "Tiered Fit & Flare Maxi Dress", "₹849", "Rs. 1482 OFF!"));
        dressList.add(new Dress(R.drawable.dress2, "Tie & Dye Bodycon Maxi Dress", "₹849", "Rs. 1482 OFF!"));
        dressList.add(new Dress(R.drawable.dress3, "Shoulder Straps A-Line Dress", "₹1,199", "Rs. 1500 OFF!"));
        dressList.add(new Dress(R.drawable.dress4, "Printed Tiered Maxi Dress", "₹849", "Rs. 1482 OFF!"));
        dressList.add(new Dress(R.drawable.dress5, "Blue Top", "₹200", "Rs. 50 OFF!"));
        dressList.add(new Dress(R.drawable.dress6, "Blue Top", "₹200", "Rs. 50 OFF!"));

        addMoreItems();
    }

    // Method to dynamically add more items
    @SuppressLint("NotifyDataSetChanged")
    private void addMoreItems() {
        dressList.add(new Dress(R.drawable.dress7, "Floral Dress", "₹1,099", "Rs. 500 OFF!"));
        dressList.add(new Dress(R.drawable.dress8, "Linen Dress", "₹899", "Rs. 400 OFF!"));

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // Load user-uploaded thrift items from SharedPreferences
    private void loadUserThriftItems() {
        SharedPreferences prefs = getSharedPreferences("ThriftStorePrefs", MODE_PRIVATE);
        Set<String> savedItems = prefs.getStringSet("sellItems", new HashSet<>());

        for (String item : savedItems) {
            String[] itemData = item.split("::");
            if (itemData.length == 3) {
                dressList.add(new Dress(itemData[2], itemData[0], itemData[1], ""));

            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // Handle receiving new thrift items from SellActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELL_ITEM && resultCode == RESULT_OK && data != null) {
            String itemName = data.getStringExtra("itemName");
            String itemPrice = data.getStringExtra("itemPrice");
            String imageUri = data.getStringExtra("imageUri");

            if (itemName != null && itemPrice != null && imageUri != null) {
                dressList.add(new Dress(imageUri.toString(), itemName, itemPrice, ""));
                adapter.notifyDataSetChanged();

                // Save to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("ThriftStorePrefs", MODE_PRIVATE);
                Set<String> savedItems = prefs.getStringSet("sellItems", new HashSet<>());
                savedItems.add(itemName + "::" + itemPrice + "::" + imageUri);
                prefs.edit().putStringSet("sellItems", savedItems).apply();
            }
        }
    }

    // Inflate the menu for the Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_menu, menu);
        return true;
    }

    // Handle menu item clicks using if-else
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sell) {
            Intent sellIntent = new Intent(this, SellActivity.class);
            startActivityForResult(sellIntent, REQUEST_CODE_SELL_ITEM);
            return true;
        } else if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_wishlist) {
            startActivity(new Intent(this, WishlistActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
