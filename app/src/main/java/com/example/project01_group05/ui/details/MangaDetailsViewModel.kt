package com.example.project01_group05.ui.details

import androidx.lifecycle.ViewModel
import com.example.project01_group05.api.MangaData
import com.example.project01_group05.api.MangaDetailsResponse
import com.example.project01_group05.api.MangaDexClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class MangaDetailsUiState(
    val manga: MangaData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MangaDetailsViewModel : ViewModel() {

    private val api = MangaDexClient.getApi()

    private val _uiState = MutableStateFlow(
        MangaDetailsUiState()
    )

    val uiState: StateFlow<MangaDetailsUiState> =
        _uiState.asStateFlow()

    fun loadManga(mangaId: String) {
        _uiState.value = MangaDetailsUiState(
            isLoading = true
        )

        api.getMangaById(mangaId).enqueue(
            object : Callback<MangaDetailsResponse> {

                override fun onResponse(
                    call: Call<MangaDetailsResponse>,
                    response: Response<MangaDetailsResponse>
                ) {
                    if (response.isSuccessful) {
                        val manga = response.body()?.data

                        if (manga != null) {
                            _uiState.value = MangaDetailsUiState(
                                manga = manga,
                                isLoading = false
                            )
                        } else {
                            _uiState.value = MangaDetailsUiState(
                                isLoading = false,
                                errorMessage = "Manga information was not found."
                            )
                        }
                    } else {
                        _uiState.value = MangaDetailsUiState(
                            isLoading = false,
                            errorMessage = "Unable to load manga details."
                        )
                    }
                }

                override fun onFailure(
                    call: Call<MangaDetailsResponse>,
                    t: Throwable
                ) {
                    _uiState.value = MangaDetailsUiState(
                        isLoading = false,
                        errorMessage = t.message
                            ?: "Unable to load manga details."
                    )
                }
            }
        )
    }
}