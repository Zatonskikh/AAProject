package com.example.sysoy.aafirstapp.models.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsListDTO {
    @SerializedName("results")
    private ArrayList<NewsItemDTO> newsList;

    ArrayList<NewsItemDTO> getNews(){
        return newsList;
    }

}
