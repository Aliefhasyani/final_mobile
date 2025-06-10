package com.example.final_mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE_TITLE = "extra_course_title";
    public static final String EXTRA_COURSE_DESC = "extra_course_desc";
    public static final String EXTRA_COURSE_IMAGE = "extra_course_image";

    private SharedPreferences prefs;
    private String title, desc, imageUrl;
    private Button btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize views
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDesc = findViewById(R.id.tvDesc);

        btnFavorite = findViewById(R.id.btnFavorite);

        // Get extras
        title = getIntent().getStringExtra(EXTRA_COURSE_TITLE);
        desc = getIntent().getStringExtra(EXTRA_COURSE_DESC);
        imageUrl = getIntent().getStringExtra(EXTRA_COURSE_IMAGE);

        // Set text
        tvTitle.setText(title);
        tvDesc.setText(desc);

        // Load image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl);

        }

        // Initialize SharedPreferences
        prefs = getSharedPreferences("favorites", MODE_PRIVATE);

        // Update button state
        updateFavoriteButton();

        // Set click listener
        btnFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void updateFavoriteButton() {
        String entry = title + "||" + desc + "||" + imageUrl;
        String favorites = prefs.getString("courses", "");
        boolean isFavorite = favorites.contains(entry);

        btnFavorite.setText(isFavorite ? "★" : "☆");
        btnFavorite.setContentDescription(isFavorite ? "Remove from favorites" : "Add to favorites");
    }

    private void toggleFavorite() {
        String entry = title + "||" + desc + "||" + imageUrl;
        String favorites = prefs.getString("courses", "");

        if (favorites.contains(entry)) {
            // Remove from favorites
            String newFavorites = favorites.replace(entry, "")
                    .replace(";;;;", ";;")
                    .replaceAll("^;;|;;$", "");

            prefs.edit().putString("courses", newFavorites).apply();
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            // Add to favorites
            favorites = favorites.isEmpty() ? entry : favorites + ";;" + entry;
            prefs.edit().putString("courses", favorites).apply();
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }

        updateFavoriteButton();
    }
}