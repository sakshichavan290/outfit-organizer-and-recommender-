package com.example.outfitorganizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfilePhoto;
    private Button btnChangePhoto, btnSaveUsername, btnLogout;
    private EditText etUsername;
    private Switch switchDarkMode;

    private SharedPreferences sharedPreferences;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnSaveUsername = findViewById(R.id.btnSaveUsername);
        btnLogout = findViewById(R.id.btnLogout);
        etUsername = findViewById(R.id.etUsername);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Load and set saved username
        String savedUsername = sharedPreferences.getString("username", "");
        etUsername.setText(savedUsername);

        // Load and set saved profile photo
        String photoUriString = sharedPreferences.getString("photoUri", null);
        if (photoUriString != null) {
            try {
                Uri uri = Uri.parse(photoUriString);
                ivProfilePhoto.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Load and set dark mode setting
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkMode);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Profile photo picker
        btnChangePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Save username
        btnSaveUsername.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (!username.isEmpty()) {
                sharedPreferences.edit().putString("username", username).apply();
                Toast.makeText(this, "Username saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        // Dark mode toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate(); // apply change immediately
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(SettingsActivity.this, login.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            try {
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);

                ivProfilePhoto.setImageURI(selectedImageUri);
                sharedPreferences.edit().putString("photoUri", selectedImageUri.toString()).apply();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
