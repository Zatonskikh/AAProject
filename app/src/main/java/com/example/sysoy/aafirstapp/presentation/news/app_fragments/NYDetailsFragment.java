package com.example.sysoy.aafirstapp.presentation.news.app_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.NewsItem;
import com.example.sysoy.aafirstapp.presentation.news.db.NewsRepository;
import com.example.sysoy.aafirstapp.presentation.news.helpers.Converter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NYDetailsFragment extends Fragment {

    AppCompatEditText category;

    AppCompatTextView title;
    AppCompatEditText abstractNews;
    AppCompatEditText date;
    NewsRepository newsRepository;
    LinearLayout errorScreen;
    AppCompatButton retryButton;
    NewsItem currentNews;
    String type;
    Toolbar toolbar;
    Converter converter = new Converter();
    boolean isEdited = false;
    private CompositeDisposable disposables = new CompositeDisposable();

    public static NYDetailsFragment newInstance(String id){
    Bundle args = new Bundle();
    args.putString("id", id);
    NYDetailsFragment fragment = new NYDetailsFragment();
        fragment.setArguments(args);
        return fragment;
}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ny_details, container, false);
        initScreen(view);
        initToolbarElements();
        return view;
    }
    private void initScreen(View view) {
        newsRepository = new NewsRepository(getActivity().getApplicationContext());
        category = view.findViewById(R.id.category);
        title = view.findViewById(R.id.header);
        abstractNews = view.findViewById(R.id.body);
        date = view.findViewById(R.id.date);
        errorScreen = view.findViewById(R.id.error_screen);
        retryButton = view.findViewById(R.id.button_retry);
        retryButton.setOnClickListener(lambda_view -> {
            loadDetails();
            errorScreen.setVisibility(View.GONE);
        });
        loadDetails();
    }

    private void disableEditor() {
        category.setEnabled(false);
        abstractNews.setEnabled(false);
        date.setEnabled(false);
    }

    private void enableEditor() {
        category.setEnabled(true);
        abstractNews.setEnabled(true);
        date.setEnabled(true);
    }

    private void loadDetails() {
        Disposable disposable =
                newsRepository
                        .getById(getArguments().getString("id", ""))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(newsEntityList -> {
                                    disableEditor();
                                    type = newsEntityList.getType();
                                    currentNews = converter.fromDatabase(newsEntityList);
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

    private void initToolbarElements() {
        FragmentActivity activity = getActivity();
        toolbar = activity.findViewById(R.id.toolbar);
        String id =getArguments().getString("id", "");
        AppCompatImageButton delete = activity.findViewById(R.id.delete);
        AppCompatImageButton edit = activity.findViewById(R.id.edit);
        AppCompatImageButton save = activity.findViewById(R.id.save);
        delete.setOnClickListener(lambda_view -> {
            Disposable disposable =
                    newsRepository
                            .deleteById(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
            disposables.add(disposable);
            activity.onBackPressed();
        });
        edit.setOnClickListener(lambda_view  -> {
            lambda_view.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            enableEditor();
        });
        save.setOnClickListener(lambda_view  -> {
            isEdited = true;
            lambda_view.setVisibility(View.GONE);
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
                            .replaceItem(converter.toDatabase(newsItem, type))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
            disposables.add(disposable);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
