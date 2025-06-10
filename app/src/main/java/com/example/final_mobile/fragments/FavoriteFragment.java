package com.example.final_mobile.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_mobile.R;
import com.example.final_mobile.adapter.CourseAdapter;
import com.example.final_mobile.model.Course;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private RecyclerView rvFavorites;
    private CourseAdapter adapter;
    private TextView tvEmptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        setupRecyclerView();
        loadFavoriteCourses();
    }

    private void setupRecyclerView() {
        adapter = new CourseAdapter(new ArrayList<>(),
                course -> {
                    // Handle favorite item click
                },
                null
        );

        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        rvFavorites.setAdapter(adapter);
    }

    private void loadFavoriteCourses() {
        SharedPreferences prefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        String favoritesStr = prefs.getString("courses", "");
        List<Course> favorites = new ArrayList<>();

        if (!favoritesStr.isEmpty()) {
            String[] entries = favoritesStr.split(";;");
            for (String entry : entries) {
                String[] parts = entry.split("\\|\\|");
                if (parts.length == 3) {
                    // Log the parts for debugging
                    Log.d("FavoriteFragment", "Title: " + parts[0]);
                    Log.d("FavoriteFragment", "Description: " + parts[1]);
                    Log.d("FavoriteFragment", "Image URL: " + parts[2]);

                    // Create course with the image URL in the pic field
                    Course course = new Course(
                            parts[0],     // title
                            "Free",       // price
                            null,         // url
                            parts[2],     // pic (image URL) - This maps to the pic field
                            "Favorite",   // category
                            null,         // id
                            parts[1]      // desc_text
                    );

                    // Verify the image URL is accessible through getImage()
                    Log.d("FavoriteFragment", "Stored image URL: " + course.getImage());

                    favorites.add(course);
                }
            }
        }

        // Log the total number of favorites loaded
        Log.d("FavoriteFragment", "Loaded " + favorites.size() + " favorites");

        updateUI(favorites);
    }

    private void updateUI(List<Course> favorites) {
        if (favorites.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);

            adapter.updateData(favorites);
        }
    }
}