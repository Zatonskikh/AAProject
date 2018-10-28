package com.example.sysoy.aafirstapp.models.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewsListDTO {
    @SerializedName("results")
    private List<NewsItemDTO> newsList;

    public List<NewsItemDTO> getNews(){
        return newsList;
    }

}
