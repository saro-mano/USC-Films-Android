package com.example.hw9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ReviewFullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_review_full_screen);

        //Getting the Data from the Previous Intent
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("reviewBy");
        String star = bundle.getString("rating");
        String content = bundle.getString("content");


        TextView reviewByText = findViewById(R.id.Rname);
        TextView ratingText = findViewById(R.id.Rstar);
        TextView contentText = findViewById(R.id.Rcontent);

        reviewByText.setText(name);
        ratingText.setText(star);
        contentText.setText(content);

    }
}