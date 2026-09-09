package com.example.project01_group05.api

interface MangaCallback {

    fun onSuccess(mangaList: List<MangaData>)

    fun onError(errorMessage: String)
}