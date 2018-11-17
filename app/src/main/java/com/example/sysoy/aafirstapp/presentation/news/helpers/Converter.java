package com.example.sysoy.aafirstapp.presentation.news.helpers;

import android.nfc.Tag;
import android.util.Log;

import com.example.sysoy.aafirstapp.models.DTO.NewsItemDTO;
import com.example.sysoy.aafirstapp.models.NewsItem;
import com.example.sysoy.aafirstapp.presentation.news.db.NewsEntity;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class Converter {

    public static NewsEntity toDatabase(NewsItem news, String type){
            NewsEntity newsEntity = new NewsEntity();
            newsEntity.setType(type);
            newsEntity.setAbstractNew(news.getPreviewText());
            newsEntity.setMultimedia(news.getImageUrl() != null ? news.getImageUrl(): "");
            newsEntity.setPublishedDate(news.getPublishDate());
            newsEntity.setSubSection(news.getCategory());
            newsEntity.setTitle(news.getTitle());
            newsEntity.setUrl(news.getUrlToFull());
        return newsEntity;
    }

    public static List<NewsEntity> toDatabase(List<NewsItem> news, String type){
        List<NewsEntity> items = new ArrayList<>();
        for (int i = 0; i < news.size(); i++){
            items.add(toDatabase(news.get(i), type));
        }
        return items;
    }

    public static NewsItem fromDatabase(NewsEntity news){
        return new NewsItem(
                news.getTitle(),
                news.getMultimedia(),
                news.getSubSection(),
                news.getPublishedDate(),
                news.getAbstractNew(),
                news.getUrl()
        );
    }

    public static List<NewsItem> fromDatabase(List<NewsEntity> news){
        List<NewsItem> items = new ArrayList<>();
        for (int i = 0; i < news.size(); i++){
            items.add(fromDatabase(news.get(i)));
        }
        return items;
    }

    public static NewsItem fromDTO(NewsItemDTO news) {
        String multimedia = news.getMultimedia().size() != 0 ? news.getMultimedia().get(0).getUrl() : "";
        return new NewsItem(
                news.getTitle(),
                multimedia,
                news.getSubSection(),
                news.getPublishedDate(),
                news.getAbstractNew(),
                news.getUrl()
        );
    }

    public static List<NewsItem> fromDTO(List<NewsItemDTO> news){
        List<NewsItem> items = new ArrayList<>();
        for (int i = 0; i < news.size(); i++){
            items.add(fromDTO(news.get(i)));
        }
        return items;
    }
}
