package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.outfitorganizer.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ShirtImageAdapter extends RecyclerView.Adapter<ShirtImageAdapter.ViewHolder> {

    private List<String> imagePaths;
    private Context context;
    private FirebaseFirestore db;

    public ShirtImageAdapter(List<String> imagePaths, Context context) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);

        // Use Glide to load Firestore images
        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.default_image) // Placeholder image
                .error(R.drawable.default_image) // Error image
                .into(holder.imageView);

        // Handle image click event to show delete confirmation dialog
        holder.imageView.setOnClickListener(view -> showDeleteDialog(position, imagePath));
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    private void showDeleteDialog(int position, String imagePath) {
        new AlertDialog.Builder(context)
                .setTitle("Remove Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Yes", (dialog, which) -> removeImageFromFirestore(position, imagePath))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void removeImageFromFirestore(int position, String imagePath) {
        db.collection("virtualcloset")
                .whereEqualTo("imageUrl", imagePath) // ✅ Fixed field name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                db.collection("virtualcloset").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            // Remove from list
                                            imagePaths.remove(position);
                                            notifyItemRemoved(position); // ✅ Properly updating UI
                                            Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(context, "Failed to delete image", Toast.LENGTH_SHORT).show()
                                        );
                            }
                        } else {
                            Toast.makeText(context, "Image not found in Firestore", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error deleting image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
        }
    }
}
