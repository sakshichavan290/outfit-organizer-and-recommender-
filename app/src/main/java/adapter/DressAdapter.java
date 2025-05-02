package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitorganizer.Dress;
import com.example.outfitorganizer.R;

import java.util.List;

public class DressAdapter extends RecyclerView.Adapter<DressAdapter.DressViewHolder> {

    private Context context;
    private List<Dress> dressList;
    private OnDressActionListener actionListener; // Listener for handling button actions

    // Constructor for initializing context and dress list
    public DressAdapter(Context context, List<Dress> dressList) {
        this.context = context;
        this.dressList = dressList;
    }

    // Set the listener for handling dress actions (Add to Cart, Add to Wishlist, Buy Now)
    public void setOnDressActionListener(OnDressActionListener listener) {
        this.actionListener = listener;
    }

    @Override
    public DressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dress, parent, false);
        return new DressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DressViewHolder holder, int position) {
        Dress dress = dressList.get(position);

        // Check if the dress has an image URI or a drawable resource
        if (dress.hasImageUri()) {
            holder.dressImage.setImageURI(Uri.parse(dress.getImageUri()));  // Load from URI
        } else {
            holder.dressImage.setImageResource(dress.getImageResId());  // Load from drawable resource
        }

        holder.dressTitle.setText(dress.getTitle());
        holder.dressPrice.setText(dress.getPrice());

        // Handle add to cart button click
        holder.addToCartButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAction(dress, "addToCart");
            }
        });

        // Handle add to wishlist button click
        holder.addToWishlistButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAction(dress, "addToWishlist");
            }
        });

        // Handle buy now button click
        holder.buyNowButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAction(dress, "buyNow");
            }
        });
    }

    @Override
    public int getItemCount() {
        return dressList.size();
    }

    // ViewHolder class for holding view references
    public class DressViewHolder extends RecyclerView.ViewHolder {
        ImageView dressImage;
        TextView dressTitle, dressPrice;
        ImageButton addToCartButton, addToWishlistButton;
        Button buyNowButton;

        @SuppressLint("WrongViewCast")
        public DressViewHolder(View itemView) {
            super(itemView);

            // Initialize view references
            dressImage = itemView.findViewById(R.id.dressImage);
            dressTitle = itemView.findViewById(R.id.dressTitle);
            dressPrice = itemView.findViewById(R.id.dressPrice);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            addToWishlistButton = itemView.findViewById(R.id.wishlistButton);
            buyNowButton = itemView.findViewById(R.id.buynowButton);
        }
    }

    // Interface for dress actions (add to cart, add to wishlist, buy now)
    public interface OnDressActionListener {
        void onAction(Dress dress, String action);
    }
}
