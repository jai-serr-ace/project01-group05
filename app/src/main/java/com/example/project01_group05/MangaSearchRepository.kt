package com.example.project01_group05.api

interface MangaSearchRepository {

    fun searchManga(
        title: String,
        contentRatings: List<String> = listOf("safe", "suggestive"),
        callback: MangaCallback
    )
}