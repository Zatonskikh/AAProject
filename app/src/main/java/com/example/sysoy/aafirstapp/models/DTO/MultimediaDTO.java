package com.example.sysoy.aafirstapp.models.DTO;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class MultimediaDTO {

    @SerializedName("url")
    @Nullable
    private String url;

    @Nullable
    public String getUrl(){
        return url;
    }
}
