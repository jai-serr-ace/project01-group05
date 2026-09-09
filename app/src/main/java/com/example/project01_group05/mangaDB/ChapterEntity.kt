package com.example.project01_group05.mangaDB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents the storage state of a chapter in the local device.
 */
enum class StorageStatus {
    /** No local copy exists. */
    NONE,
    /** Saved in temporary cache directory. */
    CACHED,
    /** Saved in permanent external/internal storage. */
    DOWNLOADED
}

/**
 * Room Entity representing a single chapter of a Manga.
 *
 * Linked to [MangaEntity] via a foreign key on [mangaId].
 *
 * @property chapterId The unique UUID provided by MangaDex.
 * @property mangaId The local database ID of the parent manga.
 * @property title The title of the chapter.
 * @property chapterNumber The string representation of the chapter number (e.g., "1", "2.5").
 * @property volume The volume number if available.
 * @property pagesCount Number of pages in this chapter.
 * @property storageStatus Indicates if the chapter is available offline.
 */
@Entity(
    tableName = "chapters",
    foreignKeys = [
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["mangaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChapterEntity(
    @PrimaryKey
    val chapterId: String, // MangaDex UUID
    val mangaId: Int,      // FK to MangaEntity.id
    val title: String?,
    val chapterNumber: String? = null,
    val volume: String? = null,
    val pagesCount: Int,
    val storageStatus: StorageStatus = StorageStatus.NONE
)
