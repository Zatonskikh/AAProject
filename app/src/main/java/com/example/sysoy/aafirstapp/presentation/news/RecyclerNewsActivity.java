package com.example.sysoy.aafirstapp.presentation.news;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.presentation.about.AboutActivity;
import com.example.sysoy.aafirstapp.presentation.news.adapter.NewsRecyclerAdapter;
import com.example.sysoy.aafirstapp.utils.DataUtils;

import java.text.DateFormat;

import static com.example.sysoy.aafirstapp.presentation.news.NewsDetailsActivity.start;

public class RecyclerNewsActivity extends AppCompatActivity {

    private final NewsRecyclerAdapter.OnItemClickListener clickListener = newsItem -> start(RecyclerNewsActivity.this,
            newsItem.getImageUrl(),
            newsItem.getCategory()
                    .getName(),
            newsItem.getTitle(),
            newsItem.getFullText(),
            DateFormat
                    .getDateInstance()
                    .format(newsItem
                            .getPublishDate()));

    private void initScreen(){
        RecyclerView rw = findViewById(R.id.rw);
        rw.setAdapter( new NewsRecyclerAdapter(this,
                DataUtils.generateNews(), clickListener));
        rw.setLayoutManager(new LinearLayoutManager(this));
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            rw.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.ic_info).setOnClickListener(view -> AboutActivity.start(RecyclerNewsActivity.this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_news_activity);
        initToolbar();
        initScreen();

    }
}