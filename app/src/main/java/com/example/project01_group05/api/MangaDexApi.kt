package com.example.project01_group05.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MangaDexApi {

    @GET("manga")
    fun getManga(
        @Query("limit") limit: Int
    ): Call<MangaResponse>

    @GET("manga")
    fun searchManga(
        @Query("title") title: String,
        @Query("limit") limit: Int = 20
    ): Call<MangaResponse>
}