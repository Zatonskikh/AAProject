package com.example.sysoy.aafirstapp.presentation.news;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.network.NewsApi;
import com.example.sysoy.aafirstapp.presentation.about.AboutActivity;
import com.example.sysoy.aafirstapp.presentation.news.adapter.NYTimesAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class NYRecyclerActivity extends AppCompatActivity {

    private final NYTimesAdapter.OnItemClickListener clickListener
            = newsItem -> {
        NYDetailsActivity.start(this,
                newsItem.getUrl(),
                newsItem.getTitle());
    };
    private Disposable disposable;
    private ProgressBar pb;
    private RecyclerView rw;
    private NYTimesAdapter ad;

    private void initScreen() {
        rw = findViewById(R.id.rw);
        pb = findViewById(R.id.recycler_progress);
        ad = new NYTimesAdapter(clickListener, Glide.with(this));
        AppCompatSpinner spinner = findViewById(R.id.spinner);
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
                .doOnSubscribe(disposable ->
                        showProgress(pb, true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newsListDTO -> {
                            ad.replaceItems(newsListDTO.getNews());
                        },
                        t -> {
                            showProgress(pb, false);
                        },
                        () -> showProgress(pb, false));
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
                = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.categories));
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadNews(arrayAdapter.getItem(i));
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
