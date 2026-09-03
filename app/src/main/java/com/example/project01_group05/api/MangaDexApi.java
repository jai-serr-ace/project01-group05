package com.example.project01_group05.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MangaDexApi {

    @GET("manga")
    Call<MangaResponse> getManga(
            @Query("limit") int limit
    );
}