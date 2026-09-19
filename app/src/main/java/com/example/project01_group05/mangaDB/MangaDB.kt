package com.example.project01_group05.mangaDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project01_group05.database.UserDao
import com.example.project01_group05.database.entities.UserEntity

@Database(
    entities = [
        MangaEntity::class,
        TagEntity::class,
        ChapterEntity::class,
        UserEntity::class
    ],
    version = 4
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
                    .addMigrations(MIGRATION_3_4)
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE chapters ADD COLUMN downloadRootUri TEXT DEFAULT NULL"
                )
            }
        }
    }
}
