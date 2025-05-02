
package com.example.outfitorganizer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import adapter.DressAdapter;

public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = findViewById(R.id.recyclerViewWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get wishlist items from DataManager
        adapter = new DressAdapter(this, DataManager.getWishlistItems());
        recyclerView.setAdapter(adapter);
    }
}
