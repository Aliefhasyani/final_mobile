package com.example.final_mobile.fragments;

import android.os.Bundle;
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
        // TODO: Load favorite courses from local storage
        loadFavoriteCourses();
    }

    private void setupRecyclerView() {
        adapter = new CourseAdapter(new ArrayList<>(),
                course -> {
                    // Handle favorite item click
                },
                null  // No load more for favorites
        );

        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        rvFavorites.setAdapter(adapter);
    }

    private void loadFavoriteCourses() {
        // TODO: Implement loading favorites from local storage
        List<Course> favorites = new ArrayList<>();
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