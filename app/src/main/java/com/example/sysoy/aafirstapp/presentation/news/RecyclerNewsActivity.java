package com.example.sysoy.aafirstapp.presentation.news;

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
import com.example.sysoy.aafirstapp.presentation.about.AboutActivity;
import com.example.sysoy.aafirstapp.presentation.news.adapter.NewsRecyclerAdapter;
import com.example.sysoy.aafirstapp.utils.DataUtils;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    private Disposable disposable;

    private void initScreen() {
        RecyclerView rw = findViewById(R.id.rw);
        ProgressBar pb = findViewById(R.id.recycler_progress);
        showProgress(pb, true);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            rw.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rw.setLayoutManager(new LinearLayoutManager(this));
        }
        disposable = Observable
                .fromCallable(DataUtils::generateNews)
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newsItems -> {
                            rw.setAdapter(new NewsRecyclerAdapter(this,
                                    DataUtils.generateNews(), clickListener));
                        },
                        t -> showProgress(pb, false),
                        () -> showProgress(pb, false));
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.ic_info).setOnClickListener(view -> AboutActivity.start(RecyclerNewsActivity.this));
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