package com.example.outfitorganizer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
public class Virtualcloset extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtualcloset);

        FloatingActionButton floatingButton = findViewById(R.id.floatingbtn);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Handle Floating Button Click (Opens ButtonActivity)
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Virtualcloset.this, ButtonActivity.class);
                startActivity(intent);
            }
        });

        // Handle Bottom Navigation Clicks (Replaced switch with if-else)
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_shirt) {
                    selectedFragment = new shirtFragment();
                } else if (item.getItemId() == R.id.nav_bottom) {
                    selectedFragment = new bottomFragment();
                } else if (item.getItemId() == R.id.nav_shoes) {
                    selectedFragment = new shoesfragment();
                } else if (item.getItemId() == R.id.nav_accessories) {
                    selectedFragment = new accessoriesFragment();
                }

                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();
                }
                return true;
            }
        });

        // Open a specific fragment if passed through intent
        Intent intent = getIntent();
        if (intent != null) {
            String fragmentToOpen = intent.getStringExtra("fragmentToOpen");

            Fragment fragment = null;
            if ("ShirtFragment".equals(fragmentToOpen)) {
                fragment = new shirtFragment();
            } else if ("BottomFragment".equals(fragmentToOpen)) {
                fragment = new bottomFragment();
            } else if ("ShoesFragment".equals(fragmentToOpen)) {
                fragment = new shoesfragment();
            } else if ("AccessoriesFragment".equals(fragmentToOpen)) {
                fragment = new accessoriesFragment();
            }

            if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        }
    }
}