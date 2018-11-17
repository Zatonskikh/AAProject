package com.example.sysoy.aafirstapp.presentation.news.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "news")
public class NewsEntity {

    @NonNull
    @ColumnInfo(name = "type")
    private String type;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "subsection")
    @Nullable
    private String subSection;

    @ColumnInfo(name = "abstract")
    private String abstractNew;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "published_date")
    private String publishedDate;

    @ColumnInfo(name = "multimedia")
    @Nullable
    private String multimedia;

    @Nullable
    public String getSubSection() {
        return subSection;
    }

    public void setSubSection(@Nullable String subSection) {
        this.subSection = subSection;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getAbstractNew() {
        return abstractNew;
    }

    public void setAbstractNew(String abstractNew) {
        this.abstractNew = abstractNew;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Nullable
    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(@Nullable String multimedia) {
        this.multimedia = multimedia;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }
}
