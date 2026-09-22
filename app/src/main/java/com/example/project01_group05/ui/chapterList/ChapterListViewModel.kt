package com.example.project01_group05.ui.chapterList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project01_group05.api.ChapterData
import com.example.project01_group05.api.ChapterListResponse
import com.example.project01_group05.api.MangaDexClient
import com.example.project01_group05.mangaDB.ChapterEntity
import com.example.project01_group05.mangaDB.MangaDB
import com.example.project01_group05.mangaDB.MangaEntity
import com.example.project01_group05.mangaDB.StorageStatus
import com.example.project01_group05.storage.ChapterStorageLocation
import com.example.project01_group05.storage.ChapterStorageManager
import com.example.project01_group05.storage.DownloadLocationStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

data class ChapterListUiState(
    val chapters: List<ChapterData> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val downloadProgress: Map<String, Float> = emptyMap(), // chapterId -> progress (0..1)
    val downloadedChapters: Set<String> = emptySet()
)

class ChapterListViewModel(application: Application) : AndroidViewModel(application) {

    private val api = MangaDexClient.getApi()
    private val db = MangaDB.getDatabase(application)
    private val storageManager = ChapterStorageManager(application)

    private val _uiState = MutableStateFlow(ChapterListUiState())
    val uiState: StateFlow<ChapterListUiState> = _uiState.asStateFlow()

    fun loadChapters(mangaDexId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = api.getMangaFeed(mangaDexId).awaitResponse()
                if (response.isSuccessful) {
                    val rawChapters = response.body()?.data ?: emptyList()
                    val chapters = rawChapters.sortedByDescending { 
                        it.attributes.chapter?.toDoubleOrNull() ?: -1.0 
                    }
                    
                    // Check local DB for downloaded status
                    val mangaEntity = db.mangaDao().getMangaByDexId(mangaDexId)
                    val downloadedIds = if (mangaEntity != null) {
                        db.mangaDao().getChaptersForManga(mangaEntity.id)
                            .filter { it.storageStatus == StorageStatus.DOWNLOADED }
                            .map { it.chapterId }
                            .toSet()
                    } else {
                        emptySet()
                    }

                    _uiState.value = _uiState.value.copy(
                        chapters = chapters,
                        isLoading = false,
                        downloadedChapters = downloadedIds
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load chapters: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An error occurred"
                )
            }
        }
    }

    fun downloadChapter(mangaDexId: String, chapter: ChapterData) {
        val chapterId = chapter.id
        if (_uiState.value.downloadProgress.containsKey(chapterId)) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Get manga entity from DB
                var mangaEntity = db.mangaDao().getMangaByDexId(mangaDexId)
                if (mangaEntity == null) {
                    val newId = db.mangaDao().insertManga(
                        MangaEntity(
                            mangaDexId = mangaDexId,
                            title = "Unknown Manga"
                        )
                    )
                    mangaEntity = db.mangaDao().getMangaByDexId(mangaDexId)
                }

                if (mangaEntity == null) return@launch

                // 2. Get download server info
                val atHomeResponse = api.getAtHomeServer(chapterId).awaitResponse()
                if (!atHomeResponse.isSuccessful) return@launch
                val atHome = atHomeResponse.body() ?: return@launch

                val baseUrl = atHome.baseUrl
                val hash = atHome.chapter.hash
                val fileNames = atHome.chapter.data
                val totalPages = fileNames.size

                val locationStore = DownloadLocationStore(getApplication())
                val selectedUri = locationStore.selectedTreeUriString()
                val location = if (selectedUri != null) {
                    ChapterStorageLocation.UserFolder(selectedUri)
                } else {
                    ChapterStorageLocation.LegacyExternal
                }

                _uiState.value = _uiState.value.copy(
                    downloadProgress = _uiState.value.downloadProgress + (chapterId to 0f)
                )

                var downloadedCount = 0
                fileNames.forEachIndexed { index, fileName ->
                    val url = "$baseUrl/data/$hash/$fileName"
                    val success = storageManager.downloadAndSavePage(
                        url = url,
                        mangaId = mangaDexId,
                        chapterId = chapterId,
                        fileName = fileName,
                        location = location
                    )
                    if (success) {
                        downloadedCount++
                        val progress = downloadedCount.toFloat() / totalPages
                        _uiState.value = _uiState.value.copy(
                            downloadProgress = _uiState.value.downloadProgress + (chapterId to progress)
                        )
                    }
                }

                if (downloadedCount == totalPages) {
                    // 3. Update DB
                    val entity = ChapterEntity(
                        chapterId = chapterId,
                        mangaId = mangaEntity.id,
                        title = chapter.attributes.title,
                        chapterNumber = chapter.attributes.chapter,
                        volume = chapter.attributes.volume,
                        pagesCount = totalPages,
                        storageStatus = StorageStatus.DOWNLOADED,
                        downloadRootUri = selectedUri
                    )
                    db.mangaDao().insertChapters(listOf(entity))

                    _uiState.value = _uiState.value.copy(
                        downloadedChapters = _uiState.value.downloadedChapters + chapterId,
                        downloadProgress = _uiState.value.downloadProgress - chapterId
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        downloadProgress = _uiState.value.downloadProgress - chapterId
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    downloadProgress = _uiState.value.downloadProgress - chapterId
                )
            }
        }
    }

    fun deleteChapter(mangaDexId: String, chapterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val chapter = db.mangaDao().getChapterByDexId(chapterId) ?: return@launch
            val location = when {
                chapter.storageStatus == StorageStatus.CACHED -> ChapterStorageLocation.Cache
                chapter.downloadRootUri != null -> ChapterStorageLocation.UserFolder(chapter.downloadRootUri)
                else -> ChapterStorageLocation.LegacyExternal
            }
            val success = storageManager.deleteChapter(mangaDexId, chapterId, location = location)
            if (success) {
                db.mangaDao().deleteChapterByDexId(chapterId)
                _uiState.value = _uiState.value.copy(
                    downloadedChapters = _uiState.value.downloadedChapters - chapterId
                )
            }
        }
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            storageManager.clearCache()
            // If we had chapters marked as CACHED in DB, we'd need to update them too
        }
    }
}
