package com.example.outfitorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

import adapter.OutfitAdapter;

public class ViewOutfitsActivity extends AppCompatActivity implements OutfitAdapter.OnOutfitClickListener {

    private RecyclerView recyclerView;
    private List<Outfit> outfitList;
    private OutfitAdapter adapter;

    private FirebaseAuth auth;
    private DatabaseReference outfitRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_outfits);

        recyclerView = findViewById(R.id.recyclerViewOutfits);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        outfitList = new ArrayList<>();
        adapter = new OutfitAdapter(outfitList, this);  // ✅ Fixed: only pass outfitList and listener
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        outfitRef = FirebaseDatabase.getInstance().getReference("Outfits").child(auth.getUid());

        loadOutfits();
    }

    private void loadOutfits() {
        outfitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                outfitList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Outfit outfit = child.getValue(Outfit.class);
                    if (outfit != null) {
                        outfit.setId(child.getKey());  // Set the ID from the snapshot key
                        outfitList.add(outfit);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewOutfitsActivity.this, "Failed to load outfits", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOutfitClick(Outfit outfit) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedOutfitName", outfit.getName());
        resultIntent.putExtra("selectedOutfitShirtImage", outfit.getShirtImage());
        resultIntent.putExtra("selectedOutfitBottomImage", outfit.getBottomImage());
        resultIntent.putExtra("selectedOutfitShoesImage", outfit.getShoesImage());
        resultIntent.putExtra("selectedOutfitAccessoriesImage", outfit.getAccessoriesImage());

        setResult(RESULT_OK, resultIntent);
        finish(); // Close the activity and send result back
    }

    // New method to handle deleting an outfit
    public void deleteOutfit(Outfit outfit, int position) {
        // Get the outfit ID and remove it from Firebase
        String outfitId = outfit.getId();
        outfitRef.child(outfitId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ViewOutfitsActivity.this, "Outfit deleted", Toast.LENGTH_SHORT).show();
                        outfitList.remove(position);
                        adapter.notifyItemRemoved(position);
                    } else {
                        Toast.makeText(ViewOutfitsActivity.this, "Failed to delete outfit", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
