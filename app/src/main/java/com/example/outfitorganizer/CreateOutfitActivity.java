package com.example.outfitorganizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateOutfitActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_SHIRT = 1;
    private static final int PICK_IMAGE_BOTTOM = 2;

    private EditText editTextOutfitName;
    private ImageView imageViewShirt, imageViewBottom;
    private Uri selectedShirtUri, selectedBottomUri;

    private DatabaseReference outfitRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_outfit);

        editTextOutfitName = findViewById(R.id.editTextOutfitName);
        imageViewShirt = findViewById(R.id.imageViewShirt);
        imageViewBottom = findViewById(R.id.imageViewBottom);

        auth = FirebaseAuth.getInstance();
        outfitRef = FirebaseDatabase.getInstance().getReference("Outfits").child(auth.getUid());

        findViewById(R.id.buttonSelectShirt).setOnClickListener(v -> selectImage(PICK_IMAGE_SHIRT));
        findViewById(R.id.buttonSelectBottom).setOnClickListener(v -> selectImage(PICK_IMAGE_BOTTOM));
        findViewById(R.id.buttonSaveOutfit).setOnClickListener(v -> saveOutfit());
    }

    private void selectImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            if (requestCode == PICK_IMAGE_SHIRT) {
                selectedShirtUri = imageUri;
                imageViewShirt.setImageURI(imageUri);
            } else if (requestCode == PICK_IMAGE_BOTTOM) {
                selectedBottomUri = imageUri;
                imageViewBottom.setImageURI(imageUri);
            }
        }
    }

    private void saveOutfit() {
        String outfitName = editTextOutfitName.getText().toString().trim();

        if (TextUtils.isEmpty(outfitName) || selectedShirtUri == null || selectedBottomUri == null) {
            Toast.makeText(this, "Please select both images and enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        String outfitId = outfitRef.push().getKey();

        // Create Outfit object with URIs for shirt and bottom images
        Outfit outfit = new Outfit(outfitName, selectedShirtUri.toString(), selectedBottomUri.toString(), null, null);

        outfitRef.child(outfitId).setValue(outfit)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Outfit saved successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateOutfitActivity.this, ViewOutfitsActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save outfit!", Toast.LENGTH_SHORT).show());
    }
}
