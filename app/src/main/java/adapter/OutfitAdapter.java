package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.outfitorganizer.R;
import com.example.outfitorganizer.Outfit;
import com.example.outfitorganizer.ViewOutfitsActivity;

import java.util.List;

public class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.OutfitViewHolder> {

    private List<Outfit> outfitList;
    private OnOutfitClickListener listener;

    public OutfitAdapter(List<Outfit> outfitList, OnOutfitClickListener listener) {
        this.outfitList = outfitList;
        this.listener = listener;
    }

    @Override
    public OutfitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outfit, parent, false);
        return new OutfitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OutfitViewHolder holder, int position) {
        Outfit currentOutfit = outfitList.get(position);
        holder.textViewOutfitName.setText(currentOutfit.getName());

        // Load the shirt image using Glide
        Glide.with(holder.itemView.getContext())
                .load(currentOutfit.getShirtImage()) // Assuming shirtImage is a URL or drawable resource
                .into(holder.imageViewOutfit);

        // Load the other images as needed
        Glide.with(holder.itemView.getContext())
                .load(currentOutfit.getBottomImage()) // Assuming bottomImage is a URL or drawable resource
                .into(holder.imageViewBottom);

        Glide.with(holder.itemView.getContext())
                .load(currentOutfit.getShoesImage()) // Assuming shoesImage is a URL or drawable resource
                .into(holder.imageViewShoes);

        Glide.with(holder.itemView.getContext())
                .load(currentOutfit.getAccessoriesImage()) // Assuming accessoriesImage is a URL or drawable resource
                .into(holder.imageViewAccessories);

        // Set up delete button
        holder.deleteButton.setOnClickListener(v -> {
            if (listener instanceof ViewOutfitsActivity) {
                ((ViewOutfitsActivity) listener).deleteOutfit(currentOutfit, position);  // Call delete method in activity
            }
        });

        holder.itemView.setOnClickListener(v -> listener.onOutfitClick(currentOutfit)); // Passing the selected outfit
    }

    @Override
    public int getItemCount() {
        return outfitList.size();
    }

    public static class OutfitViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOutfitName;
        public ImageView imageViewOutfit;
        public ImageView imageViewBottom;
        public ImageView imageViewShoes;
        public ImageView imageViewAccessories;
        public Button deleteButton;

        public OutfitViewHolder(View itemView) {
            super(itemView);
            textViewOutfitName = itemView.findViewById(R.id.textViewOutfitName);
            imageViewOutfit = itemView.findViewById(R.id.imageViewShirt);
            imageViewBottom = itemView.findViewById(R.id.imageViewBottom);
            imageViewShoes = itemView.findViewById(R.id.imageViewShoes);
            imageViewAccessories = itemView.findViewById(R.id.imageViewAccessories);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnOutfitClickListener {
        void onOutfitClick(Outfit outfit); // Passing the selected outfit
    }
}
