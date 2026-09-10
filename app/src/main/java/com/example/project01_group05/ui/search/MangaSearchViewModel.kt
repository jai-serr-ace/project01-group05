package com.example.project01_group05.ui.search

import androidx.lifecycle.ViewModel
import com.example.project01_group05.api.MangaCallback
import com.example.project01_group05.api.MangaData
import com.example.project01_group05.api.MangaRepository
import com.example.project01_group05.api.MangaSearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MangaSearchUiState(
    val query: String = "",
    val results: List<MangaData> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)

class MangaSearchViewModel(
    private val repository: MangaSearchRepository = MangaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MangaSearchUiState()
    )

    val uiState: StateFlow<MangaSearchUiState> =
        _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            query = query
        )
    }

    fun search() {
        val title = _uiState.value.query.trim()

        if (title.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                results = emptyList(),
                isLoading = false,
                errorMessage = "Please enter a manga title.",
                hasSearched = false
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            hasSearched = true
        )

        repository.searchManga(
            title,
            object : MangaCallback {

                override fun onSuccess(
                    mangaList: List<MangaData>
                ) {
                    _uiState.value = _uiState.value.copy(
                        results = mangaList,
                        isLoading = false,
                        errorMessage = null,
                        hasSearched = true
                    )
                }

                override fun onError(
                    errorMessage: String
                ) {
                    _uiState.value = _uiState.value.copy(
                        results = emptyList(),
                        isLoading = false,
                        errorMessage = errorMessage,
                        hasSearched = true
                    )
                }
            }
        )
    }
}