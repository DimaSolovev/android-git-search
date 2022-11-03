package com.dima.githubsearch.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static final String BASE_URL = "https://api.github.com/";
    private static ApiFactory apiFactory;
    private static Retrofit retrofit;

    private ApiFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static ApiFactory getInstance() {
        if (apiFactory == null) {
            synchronized (ApiFactory.class) {
                apiFactory = new ApiFactory();
            }
        }
        return apiFactory;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
