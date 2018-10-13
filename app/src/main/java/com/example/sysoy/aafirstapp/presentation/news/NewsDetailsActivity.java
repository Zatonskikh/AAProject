package com.example.sysoy.aafirstapp.presentation.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.sysoy.aafirstapp.R;

public class NewsDetailsActivity extends AppCompatActivity {

    public static void start(Context context, String imageUrl, String category, String title, String text, String date){
        Intent starter = new Intent(context, NewsDetailsActivity.class);
        starter.putExtra("url", imageUrl);
        starter.putExtra("category", category);
        starter.putExtra("title", title);
        starter.putExtra("text", text);
        starter.putExtra("date", date);
        context.startActivity(starter);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view -> NewsDetailsActivity.super.onBackPressed());
        Intent intent = getIntent();
        toolbar.setTitle(intent.getStringExtra("category").toUpperCase());
    }

    private void initScreen(){
        Intent activityIntent = getIntent();
        AppCompatImageView image = findViewById(R.id.news_image);
        AppCompatTextView title = findViewById(R.id.news_title);
        AppCompatTextView text = findViewById(R.id.news_text);
        AppCompatTextView date = findViewById(R.id.news_date);

        Glide.with(this)
                .load(activityIntent.getStringExtra("url"))
                .into(image);
        title.setText(activityIntent.getStringExtra("title"));
        text.setText(activityIntent.getStringExtra("text").replace("\n", ""));
        date.setText(activityIntent.getStringExtra("date"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details);
        initScreen();
        initToolbar();

    }
}
