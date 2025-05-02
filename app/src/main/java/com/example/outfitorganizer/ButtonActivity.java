package com.example.outfitorganizer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ButtonActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;
    private static final int PERMISSION_REQUEST_CODE = 103;

    private ImageView imageView;
    private Spinner categorySpinner;
    private String selectedCategory = "";
    private boolean imageSelected = false;
    private Bitmap selectedBitmap;
    private String savedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        imageView = findViewById(R.id.imageView);
        categorySpinner = findViewById(R.id.categorySpinner);

        findViewById(R.id.camerabtn).setOnClickListener(v -> checkCameraPermission());
        findViewById(R.id.gallerybtn).setOnClickListener(v -> openGallery());
        findViewById(R.id.saveButton).setOnClickListener(v -> saveCategoryImage());

        categorySpinner.setEnabled(false); // Disable spinner until an image is selected
        setupCategorySpinner();
    }

    private void setupCategorySpinner() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "";
            }
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                    selectedBitmap = (Bitmap) data.getExtras().get("data");
                } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                    Uri selectedImageUri = data.getData();
                    selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                }

                if (selectedBitmap != null) {
                    imageView.setImageBitmap(selectedBitmap);
                    imageSelected = true;
                    categorySpinner.setEnabled(true);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveCategoryImage() {
        if (!imageSelected || selectedCategory.isEmpty()) {
            Toast.makeText(this, "Select an image and category", Toast.LENGTH_SHORT).show();
            return;
        }

        File directory = new File(getExternalFilesDir(null) + "/VirtualClosetImages/" + selectedCategory);
        if (!directory.exists() && !directory.mkdirs()) {
            Toast.makeText(this, "Error creating directory", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = new File(directory, System.currentTimeMillis() + ".jpg");
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            savedImagePath = imageFile.getAbsolutePath();

            SharedPreferences prefs = getSharedPreferences("ClosetPreferences", MODE_PRIVATE);
            Set<String> imagePaths = new HashSet<>(prefs.getStringSet(selectedCategory + "_images", new HashSet<>()));
            imagePaths.add(savedImagePath);

            prefs.edit().putStringSet(selectedCategory + "_images", imagePaths).apply();

            Intent intent = new Intent(this, Virtualcloset.class);
            intent.putExtra("fragmentToOpen", selectedCategory + "Fragment");
            intent.putExtra("newImagePath", savedImagePath);
            startActivity(intent);
            finish();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }
}
