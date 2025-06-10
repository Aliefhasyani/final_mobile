package com.example.final_mobile;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE_TITLE = "extra_course_title";
    public static final String EXTRA_COURSE_DESC = "extra_course_desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDesc = findViewById(R.id.tvDesc);

        String title = getIntent().getStringExtra(EXTRA_COURSE_TITLE);
        String desc = getIntent().getStringExtra(EXTRA_COURSE_DESC);

        tvTitle.setText(title);
        tvDesc.setText(desc);
    }
}