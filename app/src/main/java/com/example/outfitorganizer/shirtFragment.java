package com.example.outfitorganizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class shirtFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShirtImageAdapter adapter;
    private List<String> imagePaths;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shirt, container, false);

        recyclerView = view.findViewById(R.id.shirtRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        imagePaths = new ArrayList<>();
        adapter = new ShirtImageAdapter(imagePaths, getContext());
        recyclerView.setAdapter(adapter);

        loadShirtImages();

        return view;
    }

    private void loadShirtImages() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("ClosetPreferences", Context.MODE_PRIVATE);
        Set<String> savedImagePaths = prefs.getStringSet("Shirt_images", new HashSet<>());
        if (savedImagePaths != null) {
            imagePaths.addAll(savedImagePaths);
            adapter.notifyDataSetChanged();
        }
    }
}