package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitorganizer.OutfitSchedule;
import com.example.outfitorganizer.R;

import java.util.List;

public class ScheduleOutfitAdapter extends RecyclerView.Adapter<ScheduleOutfitAdapter.OutfitViewHolder> {

    private List<OutfitSchedule> outfitList;
    private Context context;

    public ScheduleOutfitAdapter(List<OutfitSchedule> outfitList, Context context) {
        this.outfitList = outfitList;
        this.context = context;
    }

    @NonNull
    @Override
    public OutfitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_outfit, parent, false);
        return new OutfitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitViewHolder holder, int position) {
        OutfitSchedule outfit = outfitList.get(position);

        // Set the outfit name
        holder.textViewOutfitName.setText(outfit.getOutfitName());

        // Use a method to load the image (e.g., Picasso, Glide, etc.)
        // Example:
        // Picasso.get().load(outfit.getOutfitImageUrl()).into(holder.imageViewOutfit);
    }

    @Override
    public int getItemCount() {
        return outfitList.size();
    }

    public static class OutfitViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewOutfit;
        TextView textViewOutfitName;

        public OutfitViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOutfit = itemView.findViewById(R.id.imageViewOutfit);
            textViewOutfitName = itemView.findViewById(R.id.textViewOutfitName);
        }
    }
}
