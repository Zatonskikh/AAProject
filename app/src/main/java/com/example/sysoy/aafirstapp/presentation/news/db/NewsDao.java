package com.example.sysoy.aafirstapp.presentation.news.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    List<NewsEntity> getAll();

    @Query("SELECT * FROM news WHERE type IN (:types)")
    List<NewsEntity> loadAllByIds(String[] types);

    @Query("SELECT * FROM news WHERE type = :type")
    List<NewsEntity> getNewsById(String type);

    @Insert(onConflict = REPLACE)
    void insertAll(NewsEntity... news);

    @Insert(onConflict = REPLACE)
    void insert(NewsEntity news);

    @Query("DELETE FROM news WHERE type = :type")
    void deleteById(String type);

    @Delete
    void delete(NewsEntity news);

    @Delete
    void deleteAll(NewsEntity... news);
}
