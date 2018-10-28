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
    private ArrayList<MultimediaDTO> multimedia;

    @Nullable
    public String getSubSection(){
        return subSection;
    }

    public String getTitle(){
        return title;
    }

    public String getAbstractNew(){
        return abstractNew;
    }

    public String getUrl(){
        return url;
    }

    public String getPublishedDate(){
        return publishedDate;
    }

    @Nullable
    public ArrayList<MultimediaDTO> getMultimedia(){
        return multimedia;
    }
}
