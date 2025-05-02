package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.outfitorganizer.R;
import com.example.outfitorganizer.SellItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SellItemAdapter extends RecyclerView.Adapter<SellItemAdapter.ViewHolder> {
    private final List<SellItem> sellItemList;
    private final Context context;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_ITEMS = "sellItems";

    public SellItemAdapter(List<SellItem> sellItemList, Context context) {
        this.sellItemList = sellItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SellItem item = sellItemList.get(position);

        // Set item details
        holder.textViewName.setText(item.getItemName());
        holder.textViewPrice.setText("₹" + item.getItemPrice());

        // Use Glide to load image
        Glide.with(context).load(item.getImageUri()).into(holder.imageViewItem);

        // Handle delete button click
        holder.buttonDelete.setOnClickListener(v -> {
            removeItemFromPreferences(item);
            sellItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, sellItemList.size());
        });
    }

    @Override
    public int getItemCount() {
        return sellItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice;
        ImageView imageViewItem;
        ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewItemName);
            textViewPrice = itemView.findViewById(R.id.textViewItemPrice);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    // Remove item from SharedPreferences when deleted
    private void removeItemFromPreferences(SellItem item) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> itemSet = new HashSet<>(prefs.getStringSet(KEY_ITEMS, new HashSet<>()));

        itemSet.remove(item.getItemName() + "###" + item.getItemPrice() + "###" + item.getImageUri());

        editor.putStringSet(KEY_ITEMS, itemSet);
        editor.apply();
    }
}
