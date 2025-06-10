package com.example.final_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.final_mobile.DetailActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.final_mobile.R;
import com.example.final_mobile.adapter.CourseAdapter;
import com.example.final_mobile.model.Course;
import com.example.final_mobile.network.ApiService;
import com.example.final_mobile.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView rvCourses;
    private CourseAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ApiService apiService;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);
        setupRecyclerView();
        loadCourses();
    }

    private void setupViews(View view) {
        rvCourses = view.findViewById(R.id.rvCourses);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(() -> {
            currentPage = 0;
            loadCourses();
        });
    }

    private void setupRecyclerView() {
        adapter = new CourseAdapter(new ArrayList<>(),
                course -> {
                    Intent intent = new Intent(requireContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_COURSE_TITLE, course.getTitle());
                    intent.putExtra(DetailActivity.EXTRA_COURSE_DESC, course.getDesc_text());
                    startActivity(intent);
                },
                this::loadMoreCourses
        );

        rvCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCourses.addItemDecoration(new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL));
        rvCourses.setAdapter(adapter);
    }

    private void loadCourses() {
        apiService = RetrofitClient.getApiService(requireContext());
        apiService.getCourses(currentPage).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(@NonNull Call<List<Course>> call,
                                   @NonNull Response<List<Course>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Response: " + response.body().toString());
                    if (!response.body().isEmpty()) {
                        adapter.updateData(response.body());
                    } else {
                        Toast.makeText(requireContext(), "No courses found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Error code: " + response.code());
                    try {
                        Log.e("API_ERROR", "Error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(requireContext(), "LIMIT APINYA", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Course>> call, @NonNull Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreCourses() {
        currentPage++;
        apiService.getCourses(currentPage).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(@NonNull Call<List<Course>> call,
                                   @NonNull Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isEmpty()) {
                        adapter.addCourses(response.body());
                    } else {
                        currentPage--; // Revert page increment if no more courses
                        Toast.makeText(requireContext(), "No more courses available",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    currentPage--; // Revert page increment on error
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Course>> call, @NonNull Throwable t) {
                currentPage--; // Revert page increment on error
                handleFailure(t);
            }
        });
    }

    private void handleError(Response<List<Course>> response) {
        try {
            Log.e("HomeFragment", "Error: " + response.errorBody().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(requireContext(),
                "Error loading courses: " + response.code(), Toast.LENGTH_SHORT).show();
    }

    private void handleFailure(Throwable t) {
        swipeRefresh.setRefreshing(false);
        Log.e("HomeFragment", "Network error: " + t.getMessage());
        t.printStackTrace();
        Toast.makeText(requireContext(),
                "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}