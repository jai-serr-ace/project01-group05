package com.example.project01_group05.ui.downloadManager

import android.app.Application
import android.net.Uri
import android.text.format.Formatter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project01_group05.mangaDB.ChapterEntity
import com.example.project01_group05.mangaDB.MangaDB
import com.example.project01_group05.mangaDB.StorageStatus
import com.example.project01_group05.storage.ChapterStorageLocation
import com.example.project01_group05.storage.ChapterStorageManager
import com.example.project01_group05.storage.DownloadLocationStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

data class DownloadManagerUiState(
    val downloadedChapters: List<ChapterEntity> = emptyList(),
    val mangaIdToDexId: Map<Int, String> = emptyMap(),
    val cacheSize: String = "0 B",
    val selectedFolderUri: String? = null,
    val selectedFolderName: String = "App default",
    val locationError: String? = null
)

class DownloadManagerViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MangaDB.getDatabase(application)
    private val storageManager = ChapterStorageManager(application)
    private val locationStore = DownloadLocationStore(application)
    private val context = application

    private val _uiState = MutableStateFlow(DownloadManagerUiState())
    val uiState: StateFlow<DownloadManagerUiState> = _uiState.asStateFlow()

    init {
        refreshSelectedFolder()
        loadDownloads()
        updateCacheSize()
    }

    fun loadDownloads() {
        viewModelScope.launch(Dispatchers.IO) {
            val chapters = db.mangaDao().getAllDownloadedChapters()
            val mangas = db.mangaDao().getAllMangas().first()

            val map = mangas.associate { manga ->
                manga.id to (manga.mangaDexId ?: "")
            }

            _uiState.value = _uiState.value.copy(
                downloadedChapters = chapters,
                mangaIdToDexId = map
            )
        }
    }
    //This method uses Android SAF to select a folder for download.
    fun selectDownloadFolder(uri: Uri) {
        try {
            locationStore.selectFolder(uri)
            _uiState.value = _uiState.value.copy(
                selectedFolderUri = uri.toString(),
                selectedFolderName = locationStore.folderName(uri) ?: "Selected folder",
                locationError = null
            )
        } catch (_: SecurityException) {
            _uiState.value = _uiState.value.copy(
                locationError = "The app could not keep access to that folder. Please choose another folder."
            )
        }
    }

    //This method updates the user's download folder to the app's default.
    fun useAppDefaultFolder() {
        locationStore.useAppDefault()
        refreshSelectedFolder()
    }

    //This method cleans the cache, updating the database and UI state.
    //Note**: it only erases the manga chapters data that is not download.
    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!storageManager.clearCache()) return@launch
            db.mangaDao().getAllDownloadedChapters()
                .filter { it.storageStatus == StorageStatus.CACHED }
                .forEach { chapter ->
                    db.mangaDao().updateChapterStorage(chapter.chapterId, StorageStatus.NONE, null)
                }
            updateCacheSize()
            loadDownloads()
        }
    }

    //This method deletes the chapter from the database and storage.
    //It basically uses the chapter connects with manga DB to find what chapter it will delete
    //and what manga it belongs to.
    fun deleteChapter(chapter: ChapterEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val manga = db.mangaDao().getAllMangas().first().find{ manga -> manga.id == chapter.mangaId }
            val mangaDexId = manga?.mangaDexId ?: return@launch
            val location = when {
                chapter.storageStatus == StorageStatus.CACHED -> ChapterStorageLocation.Cache
                chapter.downloadRootUri != null -> ChapterStorageLocation.UserFolder(chapter.downloadRootUri)
                else -> ChapterStorageLocation.LegacyExternal
            }
            if (storageManager.deleteChapter(mangaDexId, chapter.chapterId, location)) {
                db.mangaDao().updateChapterStorage(chapter.chapterId, StorageStatus.NONE, null)
                loadDownloads()
            }
        }
    }

    //This method shows the new / current folder use for storing manga chapters.
    private fun refreshSelectedFolder() {
        val uri = locationStore.selectedTreeUri()
        val hasAccess = uri == null || locationStore.hasReadWriteAccess(uri)
        _uiState.value = _uiState.value.copy(
            selectedFolderUri = uri?.toString(),
            selectedFolderName = locationStore.folderName(uri) ?: "App default",
            locationError = if (hasAccess) null else "Access to this folder was revoked. Choose it again or use the app default."
        )
    }

    //This just refreshes or shows the current cache size.
    private fun updateCacheSize() {
        viewModelScope.launch(Dispatchers.IO) {
            val formattedSize = Formatter.formatFileSize(context, storageManager.getCacheSize())
            _uiState.value = _uiState.value.copy(cacheSize = formattedSize)
        }
    }
}
