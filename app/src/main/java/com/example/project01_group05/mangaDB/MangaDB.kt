package com.example.project01_group05.mangaDB

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MangaEntity::class, TagEntity::class], version = 2)
abstract class MangaDB : RoomDatabase() {
    abstract fun mangaDao() : MangaDAO
}