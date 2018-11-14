package com.example.sysoy.aafirstapp.presentation.news.db;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Completable;
import io.reactivex.Observable;

import static android.support.constraint.Constraints.TAG;


public class NewsRepository {

    private AppDatabase appDatabase;

    public NewsRepository(Context context){
        appDatabase = AppDatabase.getInstance(context);

    }

    public Completable add(List<NewsEntity> news){
        return Completable.fromCallable((Callable<Void>) () -> {
            NewsEntity[] newsArray = news
                    .toArray(new NewsEntity[news.size()]);
            appDatabase.newsDao().insertAll(newsArray);
            Log.w( TAG, "I have been written to db!");
            return null;
        });
    }

    public Observable<List<NewsEntity>> get(){
        return Observable.fromCallable(() -> appDatabase.newsDao().getAll());
    }

    public Observable<List<NewsEntity>> getById(String[] titles){
        return Observable
                .fromCallable(() -> appDatabase.newsDao().loadAllByIds(titles));
    }
}
