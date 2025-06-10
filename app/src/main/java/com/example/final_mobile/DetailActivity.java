package com.example.final_mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE_TITLE = "extra_course_title";
    public static final String EXTRA_COURSE_DESC = "extra_course_desc";
    public static final String EXTRA_COURSE_IMAGE = "extra_course_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDesc = findViewById(R.id.tvDesc);

        String title = getIntent().getStringExtra(EXTRA_COURSE_TITLE);
        String desc = getIntent().getStringExtra(EXTRA_COURSE_DESC);
        String imageUrl = getIntent().getStringExtra(EXTRA_COURSE_IMAGE); // Add this extra


        tvTitle.setText(title);
        tvDesc.setText(desc);

        Button btnFavorite = findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("favorites", MODE_PRIVATE);
            String favorites = prefs.getString("courses", "");
            String entry = title + "||" + desc + "||" + imageUrl;
            if (!favorites.contains(entry)) {
                favorites = favorites.isEmpty() ? entry : favorites + ";;" + entry;
                prefs.edit().putString("courses", favorites).apply();
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already in favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }
}