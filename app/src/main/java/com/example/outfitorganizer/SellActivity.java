package com.example.outfitorganizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.SellItemAdapter;

public class SellActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_ITEMS = "sellItems";

    private EditText editTextItemName, editTextItemPrice;
    private ImageView imageViewItem;
    private Button buttonAddItem, buttonViewSellingItems;
    private RecyclerView recyclerView;
    private SellItemAdapter adapter;
    private Uri selectedImageUri;
    private List<SellItem> sellItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        // Initialize UI components
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemPrice = findViewById(R.id.editTextItemPrice);
        imageViewItem = findViewById(R.id.imageViewItem);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonViewSellingItems = findViewById(R.id.buttonViewSellingItems);
        recyclerView = findViewById(R.id.recyclerView);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sellItemList = loadItemsFromPreferences();
        adapter = new SellItemAdapter(sellItemList, this);
        recyclerView.setAdapter(adapter);

        // Hide RecyclerView if empty
        toggleRecyclerViewVisibility();

        // Set click listeners
        imageViewItem.setOnClickListener(v -> selectImage());
        buttonAddItem.setOnClickListener(v -> addItemToSell());
        buttonViewSellingItems.setOnClickListener(v -> startActivity(new Intent(this, YourSellingItemsActivity.class)));
    }

    // Opens image picker
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewItem.setImageURI(selectedImageUri);
        }
    }

    // Adds item to list and saves it
    private void addItemToSell() {
        String itemName = editTextItemName.getText().toString().trim();
        String itemPrice = editTextItemPrice.getText().toString().trim();

        if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(itemPrice) || selectedImageUri == null) {
            Toast.makeText(this, "Please enter item details and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        SellItem newItem = new SellItem(itemName, itemPrice, selectedImageUri.toString());
        sellItemList.add(newItem);
        saveItemToPreferences(newItem);

        adapter.notifyDataSetChanged();
        toggleRecyclerViewVisibility();

        Toast.makeText(this, "Item added for sale!", Toast.LENGTH_SHORT).show();

        // Reset input fields
        editTextItemName.setText("");
        editTextItemPrice.setText("");
        imageViewItem.setImageResource(R.drawable.default_image);
        selectedImageUri = null;
    }

    // Saves item to SharedPreferences
    private void saveItemToPreferences(SellItem item) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> itemSet = new HashSet<>(prefs.getStringSet(KEY_ITEMS, new HashSet<>()));
        itemSet.add(item.getItemName() + "###" + item.getItemPrice() + "###" + item.getImageUri());

        editor.putStringSet(KEY_ITEMS, itemSet);
        editor.apply();
    }

    // Loads items from SharedPreferences
    private List<SellItem> loadItemsFromPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> itemSet = prefs.getStringSet(KEY_ITEMS, new HashSet<>());
        List<SellItem> items = new ArrayList<>();

        for (String itemString : itemSet) {
            String[] itemData = itemString.split("###");
            if (itemData.length == 3) {
                items.add(new SellItem(itemData[0], itemData[1], itemData[2]));
            }
        }
        return items;
    }

    // Shows/hides RecyclerView based on item count
    private void toggleRecyclerViewVisibility() {
        recyclerView.setVisibility(sellItemList.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
