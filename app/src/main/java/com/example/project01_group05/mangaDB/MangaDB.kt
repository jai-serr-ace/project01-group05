package com.example.project01_group05.mangaDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project01_group05.database.UserDao
import com.example.project01_group05.database.entities.UserEntity

@Database(
    entities = [
        MangaEntity::class,
        TagEntity::class,
        ChapterEntity::class,
        UserEntity::class
    ],
    version = 3
)
abstract class MangaDB : RoomDatabase() {
    abstract fun mangaDao(): MangaDAO
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: MangaDB? = null

        fun getDatabase(context: Context): MangaDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MangaDB::class.java,
                    "manga_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}