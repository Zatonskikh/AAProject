package com.example.sysoy.aafirstapp.presentation.news.app_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.network.NewsApi;
import com.example.sysoy.aafirstapp.presentation.about.AboutActivity;
import com.example.sysoy.aafirstapp.presentation.news.NYDetailsActivity;
import com.example.sysoy.aafirstapp.presentation.news.adapter.NYTimesAdapter;
import com.example.sysoy.aafirstapp.presentation.news.db.NewsRepository;
import com.example.sysoy.aafirstapp.presentation.news.helpers.Converter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class NewsFragment extends Fragment {

//    private static final String ARGS_POSITION = "args:position";

    public static NewsFragment newInstance() {

        Bundle args = new Bundle();
//        args.putInt(ARGS_POSITION, position);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CompositeDisposable disposables = new CompositeDisposable();
    private ProgressBar pb;
    private NYTimesAdapter ad;
    private LinearLayout errorScreen;
    private FloatingActionButton fab;
    private NewsRepository newsRepository;
    private AppCompatSpinner spinner;
    Converter converter = new Converter();

    private final NYTimesAdapter.OnItemClickListener clickListener
            = newsItem -> {
        Intent intent = new Intent(this.getContext(), NYDetailsActivity.class);
        intent.putExtra("id", spinner.getSelectedItem().toString().concat(newsItem.getTitle()));
        startActivityForResult(intent, 1);
    };


    private void initScreen(View view) {
        Context context = view.getContext();
        newsRepository = new NewsRepository(context);
        RecyclerView rw = view.findViewById(R.id.rw);
        AppCompatButton retryButton = view.findViewById(R.id.button_retry);
        errorScreen = view.findViewById(R.id.error_message);
        pb = view.findViewById(R.id.recycler_progress);
        fab = view.findViewById(R.id.reload);
        spinner = view.findViewById(R.id.spinner);
        retryButton.setOnClickListener(lambda_view -> {
            checkDbAndLoad(spinner.getSelectedItem().toString());
            errorScreen.setVisibility(View.GONE);
        });
        fab.setOnClickListener(lambda_view -> loadNews(spinner.getSelectedItem().toString()));
        ad = new NYTimesAdapter(clickListener, context);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rw.setLayoutManager(new GridLayoutManager(context, 2));
        } else {
            rw.setLayoutManager(new LinearLayoutManager(context));
        }
        rw.setAdapter(ad);
    }

    private void checkDbAndLoad(String type) {
        Disposable disposable = newsRepository.getByType(type)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> {
                    fab.hide();
                    showProgress(pb, true);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsEntityList -> {
                    if (newsEntityList.size() == 0) {
                        loadNews(type);
                    } else {
                        fab.show();
                        showProgress(pb, false);
                        ad.replaceItems(converter.fromDatabase(newsEntityList));
                    }
                }, throwable ->
                        errorScreen.setVisibility(View.VISIBLE));
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
                            ad.replaceItems(converter.fromDTO(newsListDTO.getNews()));
                            fab.show();
                            disposables.add(newsRepository
                                    .add(converter.toDatabase(converter.fromDTO(newsListDTO.getNews()), query))
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

    private void initToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        view.findViewById(R.id.ic_info).setOnClickListener(lambda_view -> AboutActivity.start(view.getContext()));
        AppCompatSpinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item,
                getResources().getStringArray(R.array.categories));
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkDbAndLoad(arrayAdapter.getItem(i));
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null && ad.getItemCount() != 0) {
            if (data.getStringExtra("exit_code").equals("deleted")){
                ad.removeAt(data.getStringExtra("title"));
            } else if(data.getStringExtra("exit_code").equals("edited")){
                String title = data.getStringExtra("title");
                String type = data.getStringExtra("type");
                Disposable disposable =
                        newsRepository.getById(type.concat(title))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(newsEntity -> ad.editItem(title, converter.fromDatabase(newsEntity)),
                        throwable -> Log.w(TAG, throwable.toString()));
                disposables.add(disposable);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_news_fragment, container, false);
        initToolbar(view);
        initScreen(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.dispose();
    }
}