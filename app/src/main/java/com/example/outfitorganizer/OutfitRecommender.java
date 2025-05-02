package com.example.outfitorganizer;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import adapter.RecommenderAdapter;
public class OutfitRecommender extends AppCompatActivity {

    private Spinner spinnerOccasion, spinnerWeather;
    private Button btnGetRecommendation;
    private RecyclerView recyclerViewOutfits;
    private RecommenderAdapter recommenderAdapter;
    private Map<String, List<Integer>> outfitImagesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_recommender);

        spinnerOccasion = findViewById(R.id.spinnerOccasion);
        spinnerWeather = findViewById(R.id.spinnerWeather);
        btnGetRecommendation = findViewById(R.id.btnGetRecommendation);
        recyclerViewOutfits = findViewById(R.id.recyclerViewOutfits);

        recyclerViewOutfits.setLayoutManager(new GridLayoutManager(this, 2));
        recommenderAdapter = new RecommenderAdapter(new ArrayList<>());
        recyclerViewOutfits.setAdapter(recommenderAdapter);

        setupSpinners();
        setupOutfitImages();

        btnGetRecommendation.setOnClickListener(v -> showRecommendedOutfits());
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> occasionAdapter = ArrayAdapter.createFromResource(this,
                R.array.occasion_array, android.R.layout.simple_spinner_item);
        occasionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOccasion.setAdapter(occasionAdapter);

        ArrayAdapter<CharSequence> weatherAdapter = ArrayAdapter.createFromResource(this,
                R.array.weather_array, android.R.layout.simple_spinner_item);
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeather.setAdapter(weatherAdapter);
    }

    private void setupOutfitImages() {
        outfitImagesMap = new HashMap<>();

        outfitImagesMap.put("Casual_Sunny", new ArrayList<>(Arrays.asList(
                R.drawable.casual_sunny1, R.drawable.casual_sunny2, R.drawable.casual_sunny3,
                R.drawable.casual_sunny4, R.drawable.casual_sunny5, R.drawable.casual_sunny6,
                R.drawable.casual_sunny7, R.drawable.casual_sunny8, R.drawable.casual_sunny9,
                R.drawable.casual_sunny10)));

        outfitImagesMap.put("Casual_Windy", new ArrayList<>(Arrays.asList(
                R.drawable.casual_windy1, R.drawable.casual_windy2, R.drawable.casual_windy3,
                R.drawable.casual_windy4, R.drawable.casual_windy5, R.drawable.casual_windy6,
                R.drawable.casual_windy7, R.drawable.casual_windy8, R.drawable.casual_windy9,
                R.drawable.casual_windy10)));

        outfitImagesMap.put("Formal_Sunny", new ArrayList<>(Arrays.asList(
                R.drawable.formal_sunny1, R.drawable.formal_sunny2,R.drawable.formal_sunny3,
                R.drawable.formal_sunny4, R.drawable.formal_sunny5,
                R.drawable.formal_sunny6,
                R.drawable.formal_sunny7, R.drawable.formal_sunny8, R.drawable.formal_sunny9,
                R.drawable.formal_sunny10)));

        // Add more categories...
    }

    private void showRecommendedOutfits() {
        String selectedOccasion = spinnerOccasion.getSelectedItem().toString();
        String selectedWeather = spinnerWeather.getSelectedItem().toString();
        String categoryKey = selectedOccasion + "_" + selectedWeather;

        List<Integer> recommendedImages = outfitImagesMap.getOrDefault(categoryKey, new ArrayList<>());
        recommenderAdapter.setOutfitImages(recommendedImages);
    }
}
