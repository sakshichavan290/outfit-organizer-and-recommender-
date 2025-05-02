package com.example.outfitorganizer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BuyNowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);

        ImageView dressImage = findViewById(R.id.buyNowDressImage);
        TextView dressTitle = findViewById(R.id.buyNowDressTitle);
        TextView dressPrice = findViewById(R.id.buyNowDressPrice);
        Button purchaseButton = findViewById(R.id.purchaseButton);

        // Get data from intent
        int imageResId = getIntent().getIntExtra("dressImage", 0);
        String title = getIntent().getStringExtra("dressTitle");
        String price = getIntent().getStringExtra("dressPrice");

        // Set data to views
        dressImage.setImageResource(imageResId);
        dressTitle.setText(title);
        dressPrice.setText(price);

        // Handle purchase button click
        purchaseButton.setOnClickListener(v -> {
            Toast.makeText(BuyNowActivity.this, "Purchase successful!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity after purchase
        });
    }
}
