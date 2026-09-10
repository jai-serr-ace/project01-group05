package com.example.project01_group05.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MangaRepository : MangaSearchRepository{

    fun fetchManga(callback: MangaCallback) {

        val api = MangaDexClient.getApi()

        api.getManga(10).enqueue(object : Callback<MangaResponse> {

            override fun onResponse(
                call: Call<MangaResponse>,
                response: Response<MangaResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    val mangaList = response.body()!!.data
                    callback.onSuccess(mangaList)

                } else {

                    callback.onError(
                        "MangaDex request failed. HTTP code: ${response.code()}"
                    )
                }
            }

            override fun onFailure(
                call: Call<MangaResponse>,
                throwable: Throwable
            ) {
                val message =
                    throwable.message ?: "Unable to retrieve manga from MangaDex."

                callback.onError(message)
            }
        })
    }

    override fun searchManga(
        title: String,
        callback: MangaCallback
    ) {
        val api = MangaDexClient.getApi()

        api.searchManga(title).enqueue(object : Callback<MangaResponse> {

            override fun onResponse(
                call: Call<MangaResponse>,
                response: Response<MangaResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    val mangaList = response.body()!!.data
                    callback.onSuccess(mangaList)

                } else {

                    callback.onError(
                        "MangaDex search failed. HTTP code: ${response.code()}"
                    )
                }
            }

            override fun onFailure(
                call: Call<MangaResponse>,
                throwable: Throwable
            ) {
                val message =
                    throwable.message ?: "Unable to search MangaDex."

                callback.onError(message)
            }
        })
    }
}