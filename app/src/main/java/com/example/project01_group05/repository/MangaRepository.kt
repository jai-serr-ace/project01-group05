package com.example.project01_group05.repository

import com.example.project01_group05.api.MangaDexApiService
import com.example.project01_group05.mangaDB.ChapterEntity
import com.example.project01_group05.mangaDB.MangaDAO
import com.example.project01_group05.mangaDB.StorageStatus
import com.example.project01_group05.storage.ChapterStorageLocation
import com.example.project01_group05.storage.ChapterStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Single point of truth for manga data, coordinating between Network (API), 
 * Local Database (DAO), and File System (StorageManager).
 */
class MangaRepository(
    private val apiService: MangaDexApiService,
    private val mangaDao: MangaDAO,
    private val storageManager: ChapterStorageManager
) {
    /**
     * Fetches chapter metadata from the API and persists it to the local database.
     * 
     * @param mangaUuid The MangaDex UUID for network requests.
     * @param mangaId The local database ID for relationship mapping.
     */
    suspend fun fetchAndSaveChapters(mangaUuid: String, mangaId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMangaFeed(mangaUuid)
            val chapters = response.data.map { chapterData ->
                ChapterEntity(
                    chapterId = chapterData.id,
                    mangaId = mangaId,
                    title = chapterData.attributes.title,
                    chapterNumber = chapterData.attributes.chapter,
                    volume = chapterData.attributes.volume,
                    pagesCount = chapterData.attributes.pages
                )
            }
            mangaDao.insertChapters(chapters)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Downloads all pages of a chapter and updates its storage status in the database.
     *
     * @param mangaUuid The UUID of the manga (used for folder naming).
     * @param chapterId The UUID of the chapter to download.
     * @param location The storage location destination.
     */
    suspend fun downloadChapter(mangaUuid: String, chapterId: String, location: ChapterStorageLocation) = withContext(Dispatchers.IO) {
        try {
            val atHome = apiService.getAtHomeServer(chapterId)
            val baseUrl = atHome.baseUrl
            val hash = atHome.chapter.hash
            val files = atHome.chapter.data

            var allSuccess = true
            files.forEachIndexed { index, fileName ->
                val url = "$baseUrl/data/$hash/$fileName"
                val success = storageManager.downloadAndSavePage(
                    url = url,
                    mangaId = mangaUuid,
                    chapterId = chapterId,
                    fileName = "page_${String.format("%03d", index + 1)}.jpg",
                    location = location
                )
                if (!success) allSuccess = false
            }

            if (allSuccess) {
                val status = if (location is ChapterStorageLocation.Cache) StorageStatus.CACHED else StorageStatus.DOWNLOADED
                val rootUri = if (location is ChapterStorageLocation.UserFolder) location.treeUri else null
                mangaDao.updateChapterStorage(chapterId, status, rootUri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Retrieves chapters from the local database for a specific manga.
     */
    suspend fun getChapters(mangaId: Int) = mangaDao.getChaptersForManga(mangaId)
}
