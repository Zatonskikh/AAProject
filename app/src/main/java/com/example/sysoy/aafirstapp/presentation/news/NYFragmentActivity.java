package com.example.sysoy.aafirstapp.presentation.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.presentation.news.app_fragments.NewsFragment;

public class NYFragmentActivity extends AppCompatActivity {

    private final int FRAGMENT_ACTIVITY_LAYOUT = R.layout.frame_activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(FRAGMENT_ACTIVITY_LAYOUT);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.news_list, NewsFragment.newInstance())
                .commit();
    }
}
