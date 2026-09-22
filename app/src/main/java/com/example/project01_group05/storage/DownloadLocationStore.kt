package com.example.project01_group05.storage

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

/** Persists the folder used for future permanent chapter downloads. */
class DownloadLocationStore(private val context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun selectedTreeUri(): Uri? = preferences.getString(KEY_TREE_URI, null)?.let(Uri::parse)

    fun selectedTreeUriString(): String? = preferences.getString(KEY_TREE_URI, null)

    @Throws(SecurityException::class)
    fun selectFolder(uri: Uri) {
        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        preferences.edit().putString(KEY_TREE_URI, uri.toString()).apply()
    }

    /** Resets future downloads only. Old URI grants are retained for existing chapters. */
    fun useAppDefault() {
        preferences.edit().remove(KEY_TREE_URI).apply()
    }

    fun folderName(uri: Uri? = selectedTreeUri()): String? {
        if (uri == null) return null
        return runCatching { DocumentFile.fromTreeUri(context, uri)?.name }.getOrNull()
    }

    fun hasReadWriteAccess(uri: Uri): Boolean {
        return context.contentResolver.persistedUriPermissions.any { permission ->
            permission.uri == uri && permission.isReadPermission && permission.isWritePermission
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "download_location"
        private const val KEY_TREE_URI = "selected_tree_uri"
    }
}

sealed interface ChapterStorageLocation {
    data object Cache : ChapterStorageLocation
    data object LegacyExternal : ChapterStorageLocation
    data class UserFolder(val treeUri: String) : ChapterStorageLocation
}

data class StoredPage(
    val name: String,
    val source: String
)
