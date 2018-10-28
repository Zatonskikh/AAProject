package com.example.sysoy.aafirstapp.models.network;

import android.support.annotation.NonNull;

import com.example.sysoy.aafirstapp.models.DTO.NewsListDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsEndpoint {

    @NonNull
    @GET("svc/topstories/v2/{category}.json")
    Observable<NewsListDTO> getNews(@Path("category") String category);

}
