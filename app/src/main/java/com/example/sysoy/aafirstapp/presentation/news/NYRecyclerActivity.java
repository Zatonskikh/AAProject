package com.example.sysoy.aafirstapp.presentation.news;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.network.NewsApi;
import com.example.sysoy.aafirstapp.presentation.about.AboutActivity;
import com.example.sysoy.aafirstapp.presentation.news.adapter.NYTimesAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NYRecyclerActivity extends AppCompatActivity {

    private final NYTimesAdapter.OnItemClickListener clickListener
            = newsItem -> {
        NYDetailsActivity.start(this,
                newsItem.getUrl(),
                newsItem.getSubSection());
    };
    private Disposable disposable;

    private void initScreen() {
        RecyclerView rw = findViewById(R.id.rw);
        ProgressBar pb = findViewById(R.id.recycler_progress);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            rw.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rw.setLayoutManager(new LinearLayoutManager(this));
        }
        disposable = NewsApi
                .getInstance()
                .news()
                .getNews("world")
                .doOnSubscribe(disposable ->
                        showProgress(pb, true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newsListDTO -> {
                            rw.setAdapter(new NYTimesAdapter(this,
                                    newsListDTO, clickListener));
                        },
                        t -> showProgress(pb, false),
                        () -> showProgress(pb, false));

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.ic_info).setOnClickListener(view -> AboutActivity.start(NYRecyclerActivity.this));
    }

    private void showProgress(ProgressBar pb, boolean needShowing){
        pb.setVisibility(needShowing ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_news_activity);
        initToolbar();
        initScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) disposable.dispose();
    }
}