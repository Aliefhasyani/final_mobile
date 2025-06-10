package com.example.final_mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.final_mobile.DetailActivity;
import com.example.final_mobile.R;
import com.example.final_mobile.adapter.CourseAdapter;
import com.example.final_mobile.model.Course;
import com.example.final_mobile.network.ApiService;
import com.example.final_mobile.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
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
    private ProgressBar progressBar;
    private boolean isSortedAscending = true;
    private List<Course> allCourses = new ArrayList<>();

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
        setupSortButton(view);
        loadCourses();
    }

    private void setupViews(View view) {
        rvCourses = view.findViewById(R.id.rvCourses);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.loadMoreProgress);

        swipeRefresh.setOnRefreshListener(() -> {
            currentPage = 0;
            allCourses.clear();
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

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == CourseAdapter.VIEW_TYPE_LOADING ? 2 : 1;
            }
        });

        rvCourses.setLayoutManager(layoutManager);
        rvCourses.setAdapter(adapter);
    }

    private void setupSortButton(View view) {
        ImageButton btnSort = view.findViewById(R.id.btnSort);
        btnSort.setOnClickListener(v -> sortCourses());
    }

    private void sortCourses() {
        if (adapter != null && !allCourses.isEmpty()) {
            if (isSortedAscending) {
                Collections.sort(allCourses, (c1, c2) ->
                        c1.getTitle().compareToIgnoreCase(c2.getTitle()));
                Toast.makeText(requireContext(), "Sorted A to Z", Toast.LENGTH_SHORT).show();
            } else {
                Collections.sort(allCourses, (c1, c2) ->
                        c2.getTitle().compareToIgnoreCase(c1.getTitle()));
                Toast.makeText(requireContext(), "Sorted Z to A", Toast.LENGTH_SHORT).show();
            }
            isSortedAscending = !isSortedAscending;
            adapter.updateData(new ArrayList<>(allCourses));
        }
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
                        allCourses.clear();
                        allCourses.addAll(response.body());
                        adapter.updateData(allCourses);
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
        View loadMoreProgress = requireView().findViewById(R.id.loadMoreProgress);
        loadMoreProgress.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            currentPage++;
            apiService.getCourses(currentPage).enqueue(new Callback<List<Course>>() {
                @Override
                public void onResponse(@NonNull Call<List<Course>> call,
                                       @NonNull Response<List<Course>> response) {
                    loadMoreProgress.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        if (!response.body().isEmpty()) {
                            allCourses.addAll(response.body());
                            adapter.addCourses(response.body());
                            if (isSortedAscending) {
                                sortCourses();
                            }
                        } else {
                            currentPage--;
                            Toast.makeText(requireContext(), "No more courses available",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        currentPage--;
                        handleError(response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Course>> call, @NonNull Throwable t) {
                    loadMoreProgress.setVisibility(View.GONE);
                    currentPage--;
                    handleFailure(t);
                }
            });
        }, 2000);
    }

    private void handleError(Response<List<Course>> response) {
        try {
            Log.e("HomeFragment", "Error: " + response.errorBody().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(requireContext(), "Error loading courses", Toast.LENGTH_SHORT).show();
    }

    private void handleFailure(Throwable t) {
        Log.e("HomeFragment", "Network error: " + t.getMessage());
        t.printStackTrace();
        Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}