package com.example.project01_group05.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project01_group05.api.MangaDetailsResponse
import com.example.project01_group05.api.MangaDexClient
import com.example.project01_group05.mangaDB.MangaDAO
import com.example.project01_group05.mangaDB.MangaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteModel(
    private val mangaDao: MangaDAO
) : ViewModel() {

    private val api = MangaDexClient.getApi()

    val mangas: StateFlow<List<MangaEntity>> =
        mangaDao.getAllMangas()
            .onEach { mangaList ->
                fetchMissingCovers(mangaList)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private fun fetchMissingCovers(mangaList: List<MangaEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (manga in mangaList) {
                val dexId = manga.mangaDexId

                if (manga.cover.isNullOrEmpty() && !dexId.isNullOrEmpty()) {
                    fetchAndSaveCover(manga, dexId)
                }
            }
        }
    }

    private fun fetchAndSaveCover(manga: MangaEntity, dexId: String) {
        api.getMangaById(dexId).enqueue(object : Callback<MangaDetailsResponse> {

            override fun onResponse(
                call: Call<MangaDetailsResponse>,
                response: Response<MangaDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    val mangaData = response.body()?.data

                    if (mangaData != null) {
                        val coverUrl = mangaData.getCoverImageUrl()
                        val title = mangaData.attributes.getDisplayTitle()

                        if (!coverUrl.isNullOrEmpty()) {
                            viewModelScope.launch(Dispatchers.IO) {
                                val updated = manga.copy(
                                    cover = coverUrl,
                                    title = if (
                                        manga.title.isNullOrEmpty() ||
                                        manga.title == "Unknown Manga"
                                    ) {
                                        title
                                    } else {
                                        manga.title
                                    }
                                )

                                mangaDao.insertManga(updated)
                            }
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<MangaDetailsResponse>,
                t: Throwable
            ) {
            }
        })
    }

    fun saveFavorite(manga: MangaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val mangaDexId = manga.mangaDexId

            if (!mangaDexId.isNullOrEmpty()) {
                val existing = mangaDao.getMangaByDexId(mangaDexId)

                if (existing != null) {
                    val updated = manga.copy(
                        id = existing.id,
                        cover = if (!manga.cover.isNullOrEmpty()) {
                            manga.cover
                        } else {
                            existing.cover
                        },
                        title = if (!manga.title.isNullOrEmpty()) {
                            manga.title
                        } else {
                            existing.title
                        },
                        description = if (!manga.description.isNullOrEmpty()) {
                            manga.description
                        } else {
                            existing.description
                        }
                    )

                    mangaDao.insertManga(updated)
                    return@launch
                }
            }

            mangaDao.insertManga(manga)
        }
    }

    fun removeFavorite(mangaDexId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mangaDao.deleteMangaByDexId(mangaDexId)
        }
    }

    suspend fun isFavorite(mangaDexId: String): Boolean {
        return mangaDao.getMangaByDexId(mangaDexId) != null
    }


    class FavoriteModelFactory(
        private val mangaDao: MangaDAO
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            if (modelClass.isAssignableFrom(FavoriteModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoriteModel(mangaDao) as T
            }

            throw IllegalArgumentException(
                "Unknown ViewModel class: ${modelClass.name}"
            )
        }
    }
}