package com.example.outfitorganizer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import adapter.DressAdapter;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get cart items from DataManager
        adapter = new DressAdapter(this, DataManager.getCartItems());
        recyclerView.setAdapter(adapter);
    }
}
