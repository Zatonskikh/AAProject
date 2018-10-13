package com.example.sysoy.aafirstapp.presentation.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.NewsItem;
import com.example.sysoy.aafirstapp.presentation.news.adapter.NewsAdapter;
import com.example.sysoy.aafirstapp.utils.DataUtils;

import java.text.DateFormat;

import static com.example.sysoy.aafirstapp.presentation.news.NewsDetailsActivity.start;
import static com.example.sysoy.aafirstapp.presentation.about.AboutActivity.start;

public class ListNewsActivity extends AppCompatActivity {

    private void initScreen(){
        ListView rw = findViewById(R.id.lw);
        rw.setAdapter( new NewsAdapter(this,
                DataUtils.generateNews()));
        rw.setOnItemClickListener((adapterView, view, i, l) -> {
            NewsAdapter newsAdapter = (NewsAdapter) adapterView.getAdapter();
            NewsItem item = newsAdapter.getNewsItem(i);
            start(ListNewsActivity.this,
                    item.getImageUrl(),
                    item.getCategory()
                            .getName(),
                    item.getTitle(),
                    item.getFullText(),
                    DateFormat
                            .getDateInstance()
                            .format(item
                                    .getPublishDate()));
        });
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.ic_info).setOnClickListener(view -> start(ListNewsActivity.this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news_activity);
        initToolbar();
        initScreen();

    }
}
