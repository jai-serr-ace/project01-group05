package com.example.project01_group05.mangaDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga")
data class MangaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String? = null,
    val cover: String? = null,
    val chapterLst: Int? = null,
    val status: Boolean? = null,
    val author: String? = null,
    val description: String? = null
)
