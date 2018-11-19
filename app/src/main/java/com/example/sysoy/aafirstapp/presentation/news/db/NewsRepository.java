package com.example.sysoy.aafirstapp.presentation.news.db;

import android.content.Context;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Completable;
import io.reactivex.Observable;


public class NewsRepository {

    private AppDatabase appDatabase;

    public NewsRepository(Context context){
        appDatabase = AppDatabase.getInstance(context);

    }

    public Completable add(List<NewsEntity> news){
        return Completable.fromCallable((Callable<Void>) () -> {
            NewsEntity[] newsArray = news
                    .toArray(new NewsEntity[news.size()]);
            appDatabase.newsDao().deleteById(news.get(0).getType());
            appDatabase.newsDao().insertAll(newsArray);
            return null;
        });
    }

    public Completable replaceItem(NewsEntity news){
        return Completable.fromCallable((Callable<Void>) () -> {
            appDatabase.newsDao().update(news);
            return null;
        });
    }

    public Observable<List<NewsEntity>> get(){
        return Observable.fromCallable(() -> appDatabase.newsDao().getAll());
    }

    public Observable<List<NewsEntity>> getById(String[] type){
        return Observable
                .fromCallable(() -> appDatabase
                        .newsDao()
                        .loadAllByIds(type));
    }

    public Observable<NewsEntity> getById(String title){
        return Observable
                .fromCallable(() -> appDatabase
                        .newsDao()
                        .getNewsById(title));
    }

    public Completable deleteByTitle(String title){
        return Completable
                .fromCallable(() -> {
                    appDatabase.newsDao().deleteByTitle(title);
                    return null;
                });
    }
}
