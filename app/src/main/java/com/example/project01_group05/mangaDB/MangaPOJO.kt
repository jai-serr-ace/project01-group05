package com.example.project01_group05.mangaDB

import androidx.room.Embedded
import androidx.room.Relation

data class MangaPOJO(
    @Embedded val manga: MangaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mangaId"
    )
    val tags: List<TagEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "mangaId"
    )
    val chapters: List<ChapterEntity>
)
