package com.example.sysoy.aafirstapp.models.DTO;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsItemDTO {

    @SerializedName("subsection")
    @Nullable
    private String subSection;

    @SerializedName("title")
    private String title;

    @SerializedName("abstract")
    private String abstractNew;

    @SerializedName("url")
    private String url;

    @SerializedName("published_date")
    private String publishedDate;

    @SerializedName("multimedia")
    @Nullable
    private ArrayList<String> multimedia;

    @Nullable
    String getSubSection(){
        return subSection;
    }

    String getTitle(){
        return title;
    }

    String getAbstractNew(){
        return abstractNew;
    }

    String getUrl(){
        return url;
    }

    String getPublishedDate(){
        return publishedDate;
    }

    @Nullable
    ArrayList<String> getMultimedia(){
        return multimedia;
    }
}
