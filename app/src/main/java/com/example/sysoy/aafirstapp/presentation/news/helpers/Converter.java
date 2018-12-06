package com.example.sysoy.aafirstapp.presentation.news.helpers;

import com.example.sysoy.aafirstapp.models.DTO.NewsItemDTO;
import com.example.sysoy.aafirstapp.models.NewsItem;
import com.example.sysoy.aafirstapp.presentation.news.db.NewsEntity;

import java.util.ArrayList;
import java.util.List;


public class Converter {

    public  NewsEntity toDatabase(NewsItem news, String type){
            NewsEntity newsEntity = new NewsEntity();
            newsEntity.setType(type);
            newsEntity.setAbstractNew(news.getPreviewText());
            newsEntity.setMultimedia(news.getImageUrl() != null ? news.getImageUrl(): "");
            newsEntity.setPublishedDate(news.getPublishDate());
            newsEntity.setSubSection(news.getCategory());
            newsEntity.setTitle(news.getTitle());
            newsEntity.setUrl(news.getUrlToFull());
            newsEntity.setId(type.concat(news.getTitle()));
        return newsEntity;
    }

    public  List<NewsEntity> toDatabase(List<NewsItem> news, String type){
        List<NewsEntity> items = new ArrayList<>();
        for (int i = 0; i < news.size(); i++){
            items.add(toDatabase(news.get(i), type));
        }
        return items;
    }

    public  NewsItem fromDatabase(NewsEntity news){
        return new NewsItem(
                news.getTitle(),
                news.getMultimedia(),
                news.getSubSection(),
                news.getPublishedDate(),
                news.getAbstractNew(),
                news.getUrl()
        );
    }

    public  List<NewsItem> fromDatabase(List<NewsEntity> news){
        List<NewsItem> items = new ArrayList<>();
        for (int i = 0; i < news.size(); i++){
            items.add(fromDatabase(news.get(i)));
        }
        return items;
    }

    private NewsItem fromDTO(NewsItemDTO news) {
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

    public  List<NewsItem> fromDTO(List<NewsItemDTO> news){
        List<NewsItem> items = new ArrayList<>();
        for (int i = 0; i < news.size(); i++){
            items.add(fromDTO(news.get(i)));
        }
        return items;
    }
}
