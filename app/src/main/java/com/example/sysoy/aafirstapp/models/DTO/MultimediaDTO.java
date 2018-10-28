package com.example.sysoy.aafirstapp.models.DTO;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class MultimediaDTO {

    @SerializedName("url")
    @Nullable
    private String url;

    @Nullable
    String getUrl(){
        return url;
    }
}
