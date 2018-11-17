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

    public Observable<List<NewsEntity>> get(){
        return Observable.fromCallable(() -> appDatabase.newsDao().getAll());
    }

    public Observable<List<NewsEntity>> getById(String[] titles){
        return Observable
                .fromCallable(new Callable<List<NewsEntity>>() {
                    @Override
                    public List<NewsEntity> call() throws Exception {
                        return appDatabase
                                .newsDao()
                                .loadAllByIds(titles);
                    }
                });
    }
}
