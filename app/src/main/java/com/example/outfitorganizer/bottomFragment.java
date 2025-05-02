package com.example.outfitorganizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.ShirtImageAdapter;

public class bottomFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShirtImageAdapter adapter;
    private List<String> imagePaths;

    public bottomFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom, container, false);

        recyclerView = view.findViewById(R.id.bottomRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        imagePaths = new ArrayList<>();
        adapter = new ShirtImageAdapter(imagePaths, getContext());
        recyclerView.setAdapter(adapter);

        loadBottomImages(); // Load bottom images when the fragment is created

        return view;
    }

    private void loadBottomImages() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("ClosetPreferences", Context.MODE_PRIVATE);
        Set<String> savedImagePaths = prefs.getStringSet("Bottom_images", new HashSet<>());

        if (savedImagePaths != null && !savedImagePaths.isEmpty()) {
            imagePaths.clear();
            imagePaths.addAll(savedImagePaths);
            adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
        } else {
            Toast.makeText(getContext(), "No bottoms saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBottomImages(); // Reload images when the fragment is resumed
    }
}
