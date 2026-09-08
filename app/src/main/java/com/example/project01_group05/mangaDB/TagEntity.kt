package com.example.project01_group05.mangaDB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
    foreignKeys = [
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["mangaId"],
            onDelete = ForeignKey.CASCADE // If manga is deleted, delete its tags too
        )
    ]
)
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val tagId: Int = 0,
    val mangaId: Int, // The ID of the manga this tag belongs to
    val tagName: String
)