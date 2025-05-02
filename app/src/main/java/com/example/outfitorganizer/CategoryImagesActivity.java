package com.example.outfitorganizer;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

import adapter.ImageAdapter;

public class CategoryImagesActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageAdapter imageAdapter;
    private static final String IMAGE_DIRECTORY = "/VirtualClosetImages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_images);

        gridView = findViewById(R.id.categoryGridView);

        String selectedCategory = getIntent().getStringExtra("selectedCategory");

        ArrayList<String> images = loadImagesFromCategory(selectedCategory);
        imageAdapter = new ImageAdapter(this, images);
        gridView.setAdapter(imageAdapter);
    }

    private ArrayList<String> loadImagesFromCategory(String category) {
        ArrayList<String> imagePaths = new ArrayList<>();
        File categoryFolder = new File(getExternalFilesDir(null) + IMAGE_DIRECTORY + "/" + category);

        if (categoryFolder.exists() && categoryFolder.isDirectory()) {
            File[] files = categoryFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    imagePaths.add(file.getAbsolutePath());
                }
            }
        }
        return imagePaths;
    }
}
