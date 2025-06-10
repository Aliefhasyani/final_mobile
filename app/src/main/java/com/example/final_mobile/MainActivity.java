package com.example.final_mobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.final_mobile.fragments.AboutFragment;
import com.example.final_mobile.fragments.FavoriteFragment;
import com.example.final_mobile.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment fragment = null;
            if (itemId == R.id.navigation_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.navigation_favorite) {
                fragment = new FavoriteFragment();
            } else if (itemId == R.id.navigation_about) {
                fragment = new AboutFragment();
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Show HomeFragment by default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}