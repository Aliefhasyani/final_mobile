package com.example.final_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.final_mobile.adapter.CourseAdapter;
import com.example.final_mobile.fragments.AboutFragment;
import com.example.final_mobile.fragments.FavoriteFragment;
import com.example.final_mobile.model.Course;
import com.example.final_mobile.model.CoursesResponse;
import com.example.final_mobile.network.ApiService;
import com.example.final_mobile.network.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private int currentPage = 0;
    private BottomNavigationView bottomNavigationView;
    private View mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupRecyclerView();
        setupBottomNavigation();
        loadCourses();
    }

    private void setupViews() {
        mainContent = findViewById(R.id.swipeRefresh);  // Changed from main_content to swipeRefresh
        rvCourses = findViewById(R.id.rvCourses);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(() -> {
            currentPage = 0;
            loadCourses();
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                showMainContent();
                return true;
            }if (itemId == R.id.navigation_favorite) {
                loadFragment(new FavoriteFragment());
                return true;
            } else if (itemId == R.id.navigation_about) {
                loadFragment(new AboutFragment());
                return true;
            }
            return false;
        });
    }

    private void showMainContent() {
        // Hide any active fragments
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(currentFragment)
                    .commit();
        }

        // Show main content
        mainContent.setVisibility(View.VISIBLE);
    }

    private void loadFragment(Fragment fragment) {
        // Hide main content
        mainContent.setVisibility(View.GONE);

        // Show fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void setupRecyclerView() {
        adapter = new CourseAdapter(new ArrayList<>(),
                course -> {
                    Toast.makeText(this, "Selected: " + course.getTitle(), Toast.LENGTH_SHORT).show();
                },
                this::loadMoreCourses
        );

        rvCourses.setLayoutManager(new LinearLayoutManager(this));
        rvCourses.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvCourses.setAdapter(adapter);
    }

    private void loadCourses() {
        apiService = RetrofitClient.getApiService(this);
        apiService.getCourses(currentPage).enqueue(new Callback<List<Course>>() {
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
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                handleFailure(t);
            }
        });
    }

    private void loadMoreCourses() {
        currentPage++;
        apiService.getCourses(currentPage).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isEmpty()) {
                        adapter.addCourses(response.body());
                        Log.d("MainActivity", "Loaded " + response.body().size() + " more courses");
                    } else {
                        currentPage--; // Revert page increment if no more courses
                        Toast.makeText(MainActivity.this, "No more courses available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    currentPage--; // Revert page increment on error
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                currentPage--; // Revert page increment on error
                handleFailure(t);
            }
        });
    }

    private void handleError(Response<List<Course>> response) {
        try {
            Log.e("MainActivity", "Error: " + response.errorBody().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this,
                "Error loading courses: " + response.code(), Toast.LENGTH_SHORT).show();
    }

    private void handleFailure(Throwable t) {
        swipeRefresh.setRefreshing(false);
        Log.e("MainActivity", "Network error: " + t.getMessage());
        t.printStackTrace();
        Toast.makeText(MainActivity.this,
                "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}