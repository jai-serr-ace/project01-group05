package com.example.project01_group05.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for MangaDex API interactions.
 */
interface MangaDexApiService {
    
    /**
     * Fetches the list of chapters for a specific manga.
     *
     * @param mangaId The MangaDex UUID of the manga.
     * @param languages List of language codes to filter by (defaults to English).
     * @param order The sorting order for chapters (default: ascending).
     * @param limit Maximum number of chapters to return.
     */
    @GET("manga/{id}/feed")
    suspend fun getMangaFeed(
        @Path("id") mangaId: String,
        @Query("translatedLanguage[]") languages: List<String> = listOf("en"),
        @Query("order[chapter]") order: String = "asc",
        @Query("limit") limit: Int = 500
    ): ChapterListResponse

    /**
     * Fetches the server base URL and image filenames for a specific chapter.
     * Used for downloading or streaming chapter pages.
     */
    @GET("at-home/server/{chapterId}")
    suspend fun getAtHomeServer(
        @Path("chapterId") chapterId: String
    ): AtHomeResponse

    companion object {
        /** The official MangaDex API base URL. */
        const val BASE_URL = "https://api.mangadex.org/"
    }
}
