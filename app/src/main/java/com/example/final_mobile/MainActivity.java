package com.example.final_mobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.final_mobile.adapter.CourseAdapter;
import com.example.final_mobile.model.Course;
import com.example.final_mobile.model.CoursesResponse;
import com.example.final_mobile.network.ApiService;
import com.example.final_mobile.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvCourses;
    private CourseAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupRecyclerView();
        loadCourses();
    }

    private void setupViews() {
        rvCourses = findViewById(R.id.rvCourses);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(this::loadCourses);
    }

    private void setupRecyclerView() {
        adapter = new CourseAdapter(new ArrayList<>(), course -> {
            // No action for now
            Toast.makeText(this, "Selected: " + course.getTitle(), Toast.LENGTH_SHORT).show();
        });

        rvCourses.setLayoutManager(new LinearLayoutManager(this));
        rvCourses.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvCourses.setAdapter(adapter);
    }

    private void loadCourses() {
        apiService = RetrofitClient.getApiService(this);
        apiService.getCourses(0).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isEmpty()) {
                        adapter.updateData(response.body());
                        Log.d("MainActivity", "Loaded " + response.body().size() + " courses");
                    } else {
                        Log.e("MainActivity", "Response successful but no courses");
                        Toast.makeText(MainActivity.this, "No courses found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.e("MainActivity", "Error: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this,
                            "Error loading courses: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.e("MainActivity", "Network error: " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(MainActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}