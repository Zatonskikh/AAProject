package com.example.sysoy.aafirstapp.presentation.news;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
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
                newsItem.getSubSection() == null ? "" : newsItem.getSubSection());
    };
    private Disposable disposable;
    private ProgressBar pb;
    private NYTimesAdapter ad;
    private AppCompatButton retryButton;
    private LinearLayout errorScreen;

    private void initScreen() {
        RecyclerView rw = findViewById(R.id.rw);
        retryButton = findViewById(R.id.button_retry);
        errorScreen = findViewById(R.id.error_screen);
        pb = findViewById(R.id.recycler_progress);
        AppCompatSpinner spinner = findViewById(R.id.spinner);
        retryButton.setOnClickListener(view -> {
            loadNews(spinner.getSelectedItem().toString());
            errorScreen.setVisibility(View.GONE);
        });
        ad = new NYTimesAdapter(clickListener, Glide.with(this));
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rw.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rw.setLayoutManager(new LinearLayoutManager(this));
        }
        rw.setAdapter(ad);
        loadNews(spinner.getSelectedItem().toString());
    }

    private void loadNews(String query) {
        disposable = NewsApi
                .getInstance()
                .news()
                .getNews(query)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable ->
                        showProgress(pb, true))
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() ->
                        showProgress(pb, false))
                .subscribe(
                        newsListDTO -> {
                            ad.replaceItems(newsListDTO.getNews());
                        },
                        t -> {
                            errorScreen.setVisibility(View.VISIBLE);
                        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.ic_info).setOnClickListener(view -> AboutActivity.start(NYRecyclerActivity.this));
        AppCompatSpinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, R.layout.spinner_item,
                getResources().getStringArray(R.array.categories));
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadNews(arrayAdapter.getItem(i));
                errorScreen.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showProgress(ProgressBar pb, boolean needShowing) {
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