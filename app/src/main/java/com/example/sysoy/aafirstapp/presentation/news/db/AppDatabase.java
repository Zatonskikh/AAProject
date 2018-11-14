package com.example.sysoy.aafirstapp.presentation.news.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {NewsEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase dbInstance;

    private static final String DB_NAME = "newsDatabase.db";

    public abstract NewsDao newsDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (dbInstance == null){
            dbInstance = Room
                    .databaseBuilder(context,
                            AppDatabase.class,
                            DB_NAME)
                    .build();
        }
        return dbInstance;
    }
}
