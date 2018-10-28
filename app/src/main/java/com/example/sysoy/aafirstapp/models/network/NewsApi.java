package com.example.sysoy.aafirstapp.models.network;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApi {

    private static final String API_KEY = "7dffaea5c35041c487929432736f05a2";
    private static final String API_KEY_NAME = "apikey";
    private static final String URL = "https://api.nytimes.com/";

    private static final int TIMEOUT_IN_SECONDS = 2;
    private final NewsEndpoint newsEndpoint;
    private static NewsApi newsApi;

    private NewsApi() {
        final Retrofit retrofitInstance = buildRetrofitClient(buildOkHttpClient());
        newsEndpoint = retrofitInstance.create(NewsEndpoint.class);
    }

    public static NewsApi getInstance() {
        return newsApi == null ? new NewsApi() : newsApi;
    }

    @NonNull
    private OkHttpClient buildOkHttpClient() {
        final HttpLoggingInterceptor networkLogInterceptor = new HttpLoggingInterceptor();
        networkLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .addInterceptor(networkLogInterceptor)
                .addInterceptor(new ApiKeyInterceptor())
                .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    public NewsEndpoint news() {
        return newsEndpoint;
    }

    private Retrofit buildRetrofitClient(@NonNull OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private class ApiKeyInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            return chain.proceed(
                    chain
                            .request()
                            .newBuilder()
                            .addHeader(API_KEY_NAME, API_KEY)
                            .build()
            );
        }
    }
}
