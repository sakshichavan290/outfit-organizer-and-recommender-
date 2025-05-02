package com.example.outfitorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class OutfitMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_main);

        findViewById(R.id.buttonViewOutfits).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OutfitMainActivity.this, ViewOutfitsActivity.class));
            }
        });

        findViewById(R.id.buttonCreateOutfit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OutfitMainActivity.this, CreateOutfitActivity.class));
            }
        });
    }
}
