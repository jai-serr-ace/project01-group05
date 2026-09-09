package com.example.project01_group05.storage

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Handles local file operations for manga chapters, including downloads and cache management.
 */
class ChapterStorageManager(private val context: Context) {
    private val client = OkHttpClient()

    /**
     * Downloads an image from the given URL and saves it to the local file system.
     *
     * @param url The source URL of the image.
     * @param mangaId The UUID of the manga.
     * @param chapterId The UUID of the chapter.
     * @param fileName The name to save the file as (e.g., page_001.jpg).
     * @param toExternal If true, saves to external files dir; otherwise uses internal cache.
     * @return True if download and save were successful.
     */
    suspend fun downloadAndSavePage(url: String, mangaId: String, chapterId: String,
                                    fileName: String, toExternal: Boolean): Boolean {
        val parentDir = if (toExternal) {
            File(context.getExternalFilesDir("Chapters"), "$mangaId/$chapterId")
        } else {
            File(context.cacheDir, "Chapters/$mangaId/$chapterId")
        }

        if (!parentDir.exists()) {
            parentDir.mkdirs()
        }

        val file = File(parentDir, fileName)
        
        val request = Request.Builder().url(url).build()
        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return false
                val inputStream: InputStream = response.body?.byteStream() ?: return false
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Retrieves the list of locally saved files for a specific chapter.
     */
    fun getChapterFiles(mangaId: String, chapterId: String, fromExternal: Boolean): List<File> {
        val parentDir = if (fromExternal) {
            File(context.getExternalFilesDir("Chapters"), "$mangaId/$chapterId")
        } else {
            File(context.cacheDir, "Chapters/$mangaId/$chapterId")
        }
        return parentDir.listFiles()?.toList() ?: emptyList()
    }

    /**
     * Deletes all cached chapter files.
     */
    fun clearCache() {
        val cacheDir = File(context.cacheDir, "Chapters")
        if (cacheDir.exists()) {
            cacheDir.deleteRecursively()
        }
    }
}
