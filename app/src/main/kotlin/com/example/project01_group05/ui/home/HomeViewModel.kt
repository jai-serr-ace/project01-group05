package com.example.project01_group05.ui.home

import androidx.lifecycle.ViewModel
import com.example.project01_group05.api.MangaData
import com.example.project01_group05.api.MangaDexClient
import com.example.project01_group05.api.MangaResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class HomeUiState(
    val mangaList: List<MangaData> = emptyList(),
    val mangaByTag: Map<String, List<MangaData>> = emptyMap(),
    val popularTags: List<String> = emptyList(),
    val selectedTagForSeeAll: String? = null,
    val isSafeOnly: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadManga()
    }

    fun toggleSafeOnly(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isSafeOnly = enabled)
        loadManga()
    }

    fun loadManga() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        val api = MangaDexClient.getApi()

        val contentRatings = if (_uiState.value.isSafeOnly) {
            listOf("safe")
        } else {
            listOf("safe", "suggestive", "erotica", "pornographic")
        }

        api.getManga(limit = 60, contentRating = contentRatings).enqueue(object : Callback<MangaResponse> {
            override fun onResponse(
                call: Call<MangaResponse>,
                response: Response<MangaResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val mangaList = response.body()!!.data
                    
                    // Group manga by tag
                    val tagMap = mutableMapOf<String, MutableList<MangaData>>()
                    mangaList.forEach { manga ->
                        val tags = manga.attributes.getTagNames()
                        tags.forEach { tag ->
                            tagMap.getOrPut(tag) { mutableListOf() }.add(manga)
                        }
                    }

                    // Select top popular tags (tags with most manga, sorted alphabetically for ties)
                    val popular = tagMap.entries
                        .sortedWith(compareByDescending<Map.Entry<String, MutableList<MangaData>>> { it.value.size }.thenBy { it.key })
                        .take(8)
                        .map { it.key }

                    _uiState.value = _uiState.value.copy(
                        mangaList = mangaList,
                        mangaByTag = tagMap,
                        popularTags = popular,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load manga: HTTP ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<MangaResponse>, t: Throwable) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = t.message ?: "Network error while loading manga."
                )
            }
        })
    }

    fun selectTagForSeeAll(tag: String?) {
        _uiState.value = _uiState.value.copy(selectedTagForSeeAll = tag)
    }
}
