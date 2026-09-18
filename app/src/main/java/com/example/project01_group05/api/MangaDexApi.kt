package com.example.project01_group05.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("manga/{id}")
    fun getMangaById(
        @Path("id") id: String
    ): Call<MangaDetailsResponse>

    @GET("manga/{id}/feed")
    fun getMangaFeed(
        @Path("id") id: String,
        @Query("translatedLanguage[]") languages: List<String> = listOf("en"),
        @Query("order[chapter]") order: String = "desc",
        @Query("limit") limit: Int = 100
    ): Call<ChapterListResponse>

    @GET("at-home/server/{chapterId}")
    fun getAtHomeServer(
        @Path("chapterId") chapterId: String
    ): Call<AtHomeResponse>
}