package com.example.project01_group05.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MangaDexClient {

    private static final String BASE_URL = "https://api.mangadex.org/";

    private static Retrofit retrofit;

    public static MangaDexApi getApi() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(MangaDexApi.class);
    }
}