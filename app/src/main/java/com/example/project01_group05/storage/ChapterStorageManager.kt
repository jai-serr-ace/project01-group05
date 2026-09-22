package com.example.project01_group05.storage

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URLConnection

/** Handles chapter files in cache, legacy app storage, and user-selected SAF folders. */
class ChapterStorageManager(private val context: Context) {
    private val client = OkHttpClient()

    suspend fun downloadAndSavePage(
        url: String,
        mangaId: String,
        chapterId: String,
        fileName: String,
        location: ChapterStorageLocation
    ): Boolean {
        val request = Request.Builder().url(url).build()
        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return false
                val input = response.body?.byteStream() ?: return false
                when (location) {
                    ChapterStorageLocation.Cache,
                    ChapterStorageLocation.LegacyExternal -> saveToFile(
                        input = input,
                        mangaId = mangaId,
                        chapterId = chapterId,
                        fileName = fileName,
                        location = location
                    )

                    is ChapterStorageLocation.UserFolder -> saveToDocumentTree(
                        input = input,
                        mangaId = mangaId,
                        chapterId = chapterId,
                        fileName = fileName,
                        treeUri = Uri.parse(location.treeUri)
                    )
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    fun getChapterPages(
        mangaId: String,
        chapterId: String,
        location: ChapterStorageLocation
    ): List<StoredPage> {
        return try {
            when (location) {
                ChapterStorageLocation.Cache,
                ChapterStorageLocation.LegacyExternal -> chapterFileDirectory(
                    mangaId,
                    chapterId,
                    location
                ).listFiles()?.filter { it.isFile }?.map {
                    StoredPage(it.name, it.absolutePath)
                }.orEmpty()

                is ChapterStorageLocation.UserFolder -> findChapterDocumentDirectory(
                    Uri.parse(location.treeUri),
                    mangaId,
                    chapterId
                )?.listFiles()?.filter { it.isFile }?.mapNotNull { document ->
                    document.name?.let { StoredPage(it, document.uri.toString()) }
                }.orEmpty()
            }.sortedBy { it.name }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emptyList()
        }
    }

    fun deleteChapter(
        mangaId: String,
        chapterId: String,
        location: ChapterStorageLocation
    ): Boolean {
        return try {
            when (location) {
                ChapterStorageLocation.Cache,
                ChapterStorageLocation.LegacyExternal -> {
                    val directory = chapterFileDirectory(mangaId, chapterId, location)
                    !directory.exists() || directory.deleteRecursively()
                }

                is ChapterStorageLocation.UserFolder -> {
                    val directory = findChapterDocumentDirectory(
                        Uri.parse(location.treeUri),
                        mangaId,
                        chapterId
                    )
                    directory == null || directory.delete()
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }

    fun getCacheSize(): Long {
        val directory = File(context.cacheDir, CHAPTERS_DIRECTORY)
        return if (directory.exists()) directory.walkTopDown().filter { it.isFile }.sumOf { it.length() } else 0L
    }

    fun clearCache(): Boolean {
        val directory = File(context.cacheDir, CHAPTERS_DIRECTORY)
        return !directory.exists() || directory.deleteRecursively()
    }

    private fun saveToFile(
        input: InputStream,
        mangaId: String,
        chapterId: String,
        fileName: String,
        location: ChapterStorageLocation
    ): Boolean {
        val parent = chapterFileDirectory(mangaId, chapterId, location)
        if (!parent.exists() && !parent.mkdirs()) return false
        val target = File(parent, safeSegment(fileName))
        return try {
            FileOutputStream(target).use(input::copyTo)
            true
        } catch (exception: Exception) {
            target.delete()
            throw exception
        }
    }

    private fun saveToDocumentTree(
        input: InputStream,
        mangaId: String,
        chapterId: String,
        fileName: String,
        treeUri: Uri
    ): Boolean {
        val chapterDirectory = getOrCreateChapterDocumentDirectory(treeUri, mangaId, chapterId)
            ?: return false
        val safeName = safeSegment(fileName)
        chapterDirectory.findFile(safeName)?.delete()
        val mimeType = URLConnection.guessContentTypeFromName(safeName) ?: "image/jpeg"
        val document = chapterDirectory.createFile(mimeType, safeName) ?: return false
        return try {
            val output = context.contentResolver.openOutputStream(document.uri, "wt") ?: return false
            output.use(input::copyTo)
            true
        } catch (exception: Exception) {
            document.delete()
            throw exception
        }
    }

    private fun chapterFileDirectory(
        mangaId: String,
        chapterId: String,
        location: ChapterStorageLocation
    ): File {
        val root = when (location) {
            ChapterStorageLocation.Cache -> File(context.cacheDir, CHAPTERS_DIRECTORY)
            ChapterStorageLocation.LegacyExternal -> context.getExternalFilesDir(CHAPTERS_DIRECTORY)
                ?: File(context.filesDir, CHAPTERS_DIRECTORY)
            is ChapterStorageLocation.UserFolder -> error("A user folder is not a java.io.File")
        }
        return File(root, "${safeSegment(mangaId)}/${safeSegment(chapterId)}")
    }

    private fun getOrCreateChapterDocumentDirectory(
        treeUri: Uri,
        mangaId: String,
        chapterId: String
    ): DocumentFile? {
        var current = DocumentFile.fromTreeUri(context, treeUri) ?: return null
        listOf(CHAPTERS_DIRECTORY, safeSegment(mangaId), safeSegment(chapterId)).forEach { name ->
            current = current.findFile(name) ?: current.createDirectory(name) ?: return null
            if (!current.isDirectory) return null
        }
        return current
    }

    private fun findChapterDocumentDirectory(
        treeUri: Uri,
        mangaId: String,
        chapterId: String
    ): DocumentFile? {
        var current = DocumentFile.fromTreeUri(context, treeUri) ?: return null
        listOf(CHAPTERS_DIRECTORY, safeSegment(mangaId), safeSegment(chapterId)).forEach { name ->
            current = current.findFile(name) ?: return null
            if (!current.isDirectory) return null
        }
        return current
    }

    private fun safeSegment(value: String): String {
        val sanitized = value.replace(Regex("[^A-Za-z0-9._-]"), "_").trim('.', ' ')
        return sanitized.ifBlank { "unnamed" }
    }

    companion object {
        private const val CHAPTERS_DIRECTORY = "chapters"
    }
}
