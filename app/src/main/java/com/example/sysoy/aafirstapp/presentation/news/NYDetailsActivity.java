package com.example.sysoy.aafirstapp.presentation.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.NewsItem;
import com.example.sysoy.aafirstapp.presentation.news.db.NewsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.sysoy.aafirstapp.presentation.news.helpers.Converter.fromDatabase;
import static com.example.sysoy.aafirstapp.presentation.news.helpers.Converter.toDatabase;

public class NYDetailsActivity extends AppCompatActivity {

    AppCompatEditText category;

    /**Notice! This field is not EditText cause I misunderstood this slide
    *https://docs.google.com/presentation/d/1cQ4W7FtwM2vJXWA_mPF8rltga4GK0TyF0d4Q1e4SXp0/edit#slide=id.g2a0c37941d_0_65
    *and used 'title' as PrimaryKey.
     * To be honest, I still don't understand what I should have used as the primary key because it's not very obvious.
     * btw this isn't the main purpose of this task so I don't think it would be a big problem :)
     */
    AppCompatTextView title;
    AppCompatEditText abstractNews;
    AppCompatEditText date;
    NewsRepository newsRepository;
    LinearLayout errorScreen;
    AppCompatButton retryButton;
    NewsItem currentNews;
    String type;
    Toolbar toolbar;
    boolean isEdited = false;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ny_details);
        initScreen();
        initToolbarElements();
    }

    private void initScreen(){
        toolbar = findViewById(R.id.toolbarer);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        newsRepository = new NewsRepository(this);
        category = findViewById(R.id.category);
        title = findViewById(R.id.header);
        abstractNews = findViewById(R.id.body);
        date = findViewById(R.id.date);
        errorScreen = findViewById(R.id.error_screen);
        retryButton = findViewById(R.id.button_retry);
        retryButton.setOnClickListener(view -> {
            loadDetails();
            errorScreen.setVisibility(View.GONE);
        });
        loadDetails();
    }

    private void disableEditor(){
        category.setEnabled(false);
        abstractNews.setEnabled(false);
        date.setEnabled(false);
    }

    private void enableEditor(){
        category.setEnabled(true);
        abstractNews.setEnabled(true);
        date.setEnabled(true);
    }

    private void loadDetails(){
        Disposable disposable =
                newsRepository
                        .getById(getIntent().getStringExtra("title"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(newsEntityList -> {
                                    disableEditor();
                                    type = newsEntityList.getType();
                                    currentNews = fromDatabase(newsEntityList);
                                    category.setText(currentNews.getCategory());
                                    title.setText(currentNews.getTitle());
                                    abstractNews.setText(currentNews.getPreviewText());
                                    date.setText(currentNews.getPublishDate());
                                    toolbar.setTitle(currentNews.getCategory().toUpperCase());
                                },
                                throwable ->
                                        errorScreen.setVisibility(View.VISIBLE));
        disposables.add(disposable);
    }

    private void initToolbarElements(){
        AppCompatImageButton delete = findViewById(R.id.delete);
        AppCompatImageButton edit = findViewById(R.id.edit);
        AppCompatImageButton save = findViewById(R.id.save);
        delete.setOnClickListener(view -> {
            Disposable disposable =
                    newsRepository
                            .deleteByTitle(getIntent().getStringExtra("title"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
            disposables.add(disposable);
            finishActivity("deleted");
        });
        edit.setOnClickListener(view -> {
            view.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            enableEditor();
        });
        save.setOnClickListener(view -> {
            isEdited = true;
            view.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            disableEditor();
            NewsItem newsItem = new NewsItem(
                    title.getText().toString(),
                    currentNews.getImageUrl(),
                    category.getText().toString(),
                    date.getText().toString(),
                    abstractNews.getText().toString(),
                    currentNews.getUrlToFull()
            );
            Disposable disposable =
                    newsRepository
                    .replaceItem(toDatabase(newsItem, type))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            disposables.add(disposable);
        });
    }

    private void finishActivity(String exit_code) {
        Intent intent = new Intent();
        intent.putExtra("exit_code", exit_code);
        intent.putExtra("title", getIntent().getStringExtra("title"));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) disposables.dispose();
    }

    @Override
    public void onBackPressed() {
        if (isEdited) {
            finishActivity("edited");
        }else {
            finishActivity("");
        }
    }
}
