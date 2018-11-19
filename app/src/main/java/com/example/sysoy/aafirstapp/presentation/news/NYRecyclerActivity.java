package com.example.sysoy.aafirstapp.presentation.news;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.NewsItem;
import com.example.sysoy.aafirstapp.models.network.NewsApi;
import com.example.sysoy.aafirstapp.presentation.about.AboutActivity;
import com.example.sysoy.aafirstapp.presentation.news.adapter.NYTimesAdapter;
import com.example.sysoy.aafirstapp.presentation.news.db.NewsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;
import static com.example.sysoy.aafirstapp.presentation.news.helpers.Converter.fromDTO;
import static com.example.sysoy.aafirstapp.presentation.news.helpers.Converter.fromDatabase;
import static com.example.sysoy.aafirstapp.presentation.news.helpers.Converter.toDatabase;

public class NYRecyclerActivity extends AppCompatActivity {

    private CompositeDisposable disposables = new CompositeDisposable();
    private ProgressBar pb;
    private NYTimesAdapter ad;
    private AppCompatButton retryButton;
    private LinearLayout errorScreen;
    private FloatingActionButton fab;
    private NewsRepository newsRepository;
    private AppCompatSpinner spinner;

    private final NYTimesAdapter.OnItemClickListener clickListener
            = newsItem -> {
        Intent intent = new Intent(this, NYDetailsActivity.class);
        intent.putExtra("title", newsItem.getTitle());
        startActivityForResult(intent, 1);
    };

    private void initScreen() {
        newsRepository = new NewsRepository(this);

        RecyclerView rw = findViewById(R.id.rw);
        retryButton = findViewById(R.id.button_retry);
        errorScreen = findViewById(R.id.error_message);
        pb = findViewById(R.id.recycler_progress);
        fab = findViewById(R.id.reload);
        spinner = findViewById(R.id.spinner);
        retryButton.setOnClickListener(view -> {
            loadNews(spinner.getSelectedItem().toString());
            errorScreen.setVisibility(View.GONE);
        });
        fab.setOnClickListener(view -> {
            loadNews(spinner.getSelectedItem().toString());
        });
        ad = new NYTimesAdapter(clickListener, Glide.with(this));
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rw.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rw.setLayoutManager(new LinearLayoutManager(this));
        }
        rw.setAdapter(ad);
        String[] strings = {spinner.getSelectedItem().toString()};
        checkDbAndLoad(strings);
    }

    private void checkDbAndLoad(String[] titles) {
        Disposable disposable = newsRepository.getById(titles)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> {
                    fab.hide();
                    showProgress(pb, true);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsEntityList -> {
                    if (newsEntityList.size() == 0) {
                        loadNews(titles[0]);
                    } else {
                        fab.show();
                        showProgress(pb, false);
                        ad.replaceItems(fromDatabase(newsEntityList));
                    }
                });
        disposables.add(disposable);
    }

    private void loadNews(String query) {
        disposables.add(NewsApi
                .getInstance()
                .news()
                .getNews(query)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    fab.hide();
                    showProgress(pb, true);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newsListDTO -> {
                            ad.replaceItems(fromDTO(newsListDTO.getNews()));
                            fab.show();
                            disposables.add(newsRepository
                                    .add(toDatabase(fromDTO(newsListDTO.getNews()), query))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> Log.w(TAG, "Good news everyone!"))
                            );
                        },
                        t -> {
                            showProgress(pb, false);
                            errorScreen.setVisibility(View.VISIBLE);
                        },
                        () -> showProgress(pb, false)));
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
                String[] item = {arrayAdapter.getItem(i)};
                checkDbAndLoad(item);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (data.getStringExtra("exit_code").equals("deleted")){
                ad.removeAt(data.getStringExtra("title"));
            } else if(data.getStringExtra("exit_code").equals("edited")){
                String title = data.getStringExtra("title");
                Disposable disposable =
                        newsRepository.getById(title)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(newsEntity -> {
                            ad.editItem(title, fromDatabase(newsEntity));
                        });
                disposables.add(disposable);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
        if (isFinishing()) disposables.dispose();
    }
}