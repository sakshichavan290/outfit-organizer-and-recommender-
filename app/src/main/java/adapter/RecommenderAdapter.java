package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitorganizer.R;

import java.util.List;

public class RecommenderAdapter extends RecyclerView.Adapter<RecommenderAdapter.ViewHolder> {

    private List<Integer> outfitImages;

    public RecommenderAdapter(List<Integer> outfitImages) {
        this.outfitImages = outfitImages;
    }

    public void setOutfitImages(List<Integer> newImages) {
        this.outfitImages = newImages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommendation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(outfitImages.get(position));
    }

    @Override
    public int getItemCount() {
        return outfitImages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.outfitImage);
        }
    }
}
