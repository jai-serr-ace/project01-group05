package com.example.project01_group05.ui.chapterDisplay

/**
 * This file contains the ViewModel and related data structures for managing the chapter reading experience.
 * It handles the logic for switching reading modes and fetching manga pages.
 *
 * Connection to Project:
 * - Interacts with [MangaDexClient] for network requests.
 * - Interacts with [ChapterStorageManager] for local cache/storage management.
 * - Provides state to [ChapterReaderScreen] via a [StateFlow].
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project01_group05.api.MangaDexClient
import com.example.project01_group05.storage.ChapterStorageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * Defines the direction or layout in which manga pages are displayed.
 */
sealed class ReadingMode {
    /** Pages flip horizontally from right to left. */
    object RightToLeft : ReadingMode()
    /** Pages scroll vertically in a continuous flow. */
    object Vertical : ReadingMode()
}

/**
 * Represents the current state of the chapter reader UI.
 */
data class ChapterUiState(
    val isLoading: Boolean = false,
    val pages: List<String> = emptyList(),
    val error: String? = null,
    val readingMode: ReadingMode = ReadingMode.RightToLeft,
    val chapterTitle: String = "",
    val chapterNumber: String = ""
)

/**
 * ViewModel for [DisplayChapterActivity].
 * It orchestrates the loading of manga pages, prioritizing local storage and falling back to the API.
 */
class DisplayChapterViewModel(application: Application) : AndroidViewModel(application) {
    private val storageManager = ChapterStorageManager(application)
    private val apiService = MangaDexClient.getApiService()

    private val _uiState = MutableStateFlow(ChapterUiState())
    
    /**
     * Observable state for the Chapter Reader UI.
     */
    val uiState: StateFlow<ChapterUiState> = _uiState

    /**
     * Loads the pages for a specific chapter.
     * It first checks if the pages are saved locally (external storage or cache).
     * If not found locally, it fetches the page list and base URL from the MangaDex API.
     *
     * @param mangaId The UUID of the manga.
     * @param chapterId The UUID of the chapter to load.
     * @param title The title of the chapter for display purposes.
     */
    fun loadChapter(mangaId: String, chapterId: String, title: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                chapterTitle = title ?: ""
            )
            
            try {
                // 1. Check local storage (External first, then Cache)
                var localFiles = storageManager.getChapterFiles(mangaId, chapterId, true)
                if (localFiles.isEmpty()) {
                    localFiles = storageManager.getChapterFiles(mangaId, chapterId, false)
                }

                if (localFiles.isNotEmpty()) {
                    // Sort files to ensure correct page order
                    val sortedPages = localFiles.sortedBy { it.name }.map { it.absolutePath }
                    _uiState.value = _uiState.value.copy(isLoading = false, pages = sortedPages)
                } else {
                    // 2. Fetch from API
                    val response = apiService.getAtHomeServer(chapterId)
                    val baseUrl = response.baseUrl
                    val hash = response.chapter.hash
                    val pageNames = response.chapter.data
                    
                    val urls = pageNames.map { pageName -> "$baseUrl/data/$hash/$pageName" }
                    _uiState.value = _uiState.value.copy(isLoading = false, pages = urls)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, 
                    error = "Failed to load chapter: ${e.message}"
                )
            }
        }
    }

    /**
     * Updates the current reading mode (e.g., RightToLeft to Vertical).
     */
    fun setReadingMode(mode: ReadingMode) {
        _uiState.value = _uiState.value.copy(readingMode = mode)
    }
}
