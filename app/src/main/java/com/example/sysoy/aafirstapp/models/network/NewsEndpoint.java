package com.example.sysoy.aafirstapp.models.network;

import android.support.annotation.NonNull;

import com.example.sysoy.aafirstapp.models.DTO.NewsItemDTO;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface NewsEndpoint {

    @NonNull
    @GET("svc/topstories/v2/world.json")
    Single<List<NewsItemDTO>> getNews();

}
