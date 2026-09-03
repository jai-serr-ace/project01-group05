package com.example.project01_group05.mangaDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//Note**: manga for now works as a dummy variable, it might need to be
//changed in the future to work with the database correctly.
@Dao
interface MangaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManga(manga: MangaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>)

    //Note**: update might need to be changed since it will only work when the entire row gets replace.
    //Meaning, in if we want to change things individually the method must be updated.
    @Update
    suspend fun updateManga(manga: MangaEntity)

    @Delete
    suspend fun deleteManga(manga: MangaEntity)

    @Query("SELECT * FROM manga")
    suspend fun getAllMangas(): List<MangaEntity>

    @androidx.room.Transaction
    @Query("SELECT * FROM manga")
    suspend fun getAllMangaWithTags(): List<MangaPOJO>

    @androidx.room.Transaction
    @Query("SELECT * FROM manga WHERE id = :mangaId")
    suspend fun getMangaById(mangaId: Int): MangaPOJO?

    @Query("DELETE FROM manga WHERE id = :mangaId")
    suspend fun deleteMangaById(mangaId: Int)
}