package com.example.project01_group05.mangaDB

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MangaDatabaseTest {

    private lateinit var mangaDao: MangaDAO
    private lateinit var db: MangaDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MangaDB::class.java).build()
        mangaDao = db.mangaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeMangaAndReadInList() = runBlocking {
        val manga = MangaEntity(
            title = "Test Manga",
            author = "Test Author",
            description = "Test Description"
        )
        val mangaId = mangaDao.insertManga(manga).toInt()

        val tag1 = TagEntity(mangaId = mangaId, tagName = "Action")
        val tag2 = TagEntity(mangaId = mangaId, tagName = "Adventure")
        mangaDao.insertTags(listOf(tag1, tag2))

        val mangaWithTags = mangaDao.getMangaById(mangaId)
        
        assertNotNull(mangaWithTags)
        assertEquals("Test Manga", mangaWithTags?.manga?.title)
        assertEquals(2, mangaWithTags?.tags?.size)
        assertEquals("Action", mangaWithTags?.tags?.get(0)?.tagName)
    }

    @Test
    fun updateMangaTest() = runBlocking {
        val manga = MangaEntity(title = "Old Title", author = "Author")
        val mangaId = mangaDao.insertManga(manga).toInt()
        
        val updatedManga = manga.copy(id = mangaId, title = "New Title")
        mangaDao.updateManga(updatedManga)
        
        val result = mangaDao.getMangaById(mangaId)
        assertEquals("New Title", result?.manga?.title)
        assertEquals("Author", result?.manga?.author)
    }

    @Test
    fun getAllMangasTest() = runBlocking {
        mangaDao.insertManga(MangaEntity(title = "Manga 1"))
        mangaDao.insertManga(MangaEntity(title = "Manga 2"))
        
        val all = mangaDao.getAllMangas()
        assertEquals(2, all.size)
    }

    @Test
    fun deleteMangaTest() = runBlocking {
        val manga = MangaEntity(title = "Delete Me")
        val mangaId = mangaDao.insertManga(manga).toInt()
        
        val inserted = mangaDao.getMangaById(mangaId)
        assertNotNull(inserted)
        
        mangaDao.deleteManga(inserted!!.manga)
        
        val deleted = mangaDao.getMangaById(mangaId)
        assertNull(deleted)
    }

    @Test
    fun deleteMangaByIdTest() = runBlocking {
        val manga = MangaEntity(title = "Delete Me By ID")
        val mangaId = mangaDao.insertManga(manga).toInt()
        
        mangaDao.deleteMangaById(mangaId)
        
        val deleted = mangaDao.getMangaById(mangaId)
        assertNull(deleted)
    }

    @Test
    fun multipleMangasTagsTest() = runBlocking {
        val id1 = mangaDao.insertManga(MangaEntity(title = "Manga 1")).toInt()
        val id2 = mangaDao.insertManga(MangaEntity(title = "Manga 2")).toInt()
        
        mangaDao.insertTags(listOf(
            TagEntity(mangaId = id1, tagName = "Tag 1A"),
            TagEntity(mangaId = id1, tagName = "Tag 1B"),
            TagEntity(mangaId = id2, tagName = "Tag 2A")
        ))
        
        val m1 = mangaDao.getMangaById(id1)
        val m2 = mangaDao.getMangaById(id2)
        
        assertEquals(2, m1?.tags?.size)
        assertEquals(1, m2?.tags?.size)
    }

    @Test
    fun deleteMangaCascadesToTags() = runBlocking {
        val manga = MangaEntity(title = "Delete Me")
        val mangaId = mangaDao.insertManga(manga).toInt()
        
        mangaDao.insertTags(listOf(TagEntity(mangaId = mangaId, tagName = "Temporary")))
        
        // Verify tag exists
        val mangaWithTags = mangaDao.getMangaById(mangaId)
        assertEquals(1, mangaWithTags?.tags?.size)
        
        // Delete the manga
        mangaDao.deleteMangaById(mangaId)
        
        // Verify manga is gone
        val deletedManga = mangaDao.getMangaById(mangaId)
        assertNull(deletedManga)
        
        // Ensure it's gone from our POJO view (Relationship is cleared)
        val allWithTags = mangaDao.getAllMangaWithTags()
        assertEquals(0, allWithTags.size)
    }

    @Test
    fun frierenAndBerserkFantasyTest() = runBlocking {
        // Insert Frieren
        val frierenId = mangaDao.insertManga(MangaEntity(title = "Frieren")).toInt()
        mangaDao.insertTags(listOf(
            TagEntity(mangaId = frierenId, tagName = "shonen"),
            TagEntity(mangaId = frierenId, tagName = "fantasy"),
            TagEntity(mangaId = frierenId, tagName = "comedy")
        ))

        // Insert Berserk
        val berserkId = mangaDao.insertManga(MangaEntity(title = "Berserk")).toInt()
        mangaDao.insertTags(listOf(
            TagEntity(mangaId = berserkId, tagName = "seinen"),
            TagEntity(mangaId = berserkId, tagName = "fantasy"),
            TagEntity(mangaId = berserkId, tagName = "tragedy")
        ))

        // Retrieve both
        val frieren = mangaDao.getMangaById(frierenId)
        val berserk = mangaDao.getMangaById(berserkId)

        // Assert both have "fantasy" tag
        assertTrue("Frieren should have fantasy tag", frieren?.tags?.any { it.tagName == "fantasy" } == true)
        assertTrue("Berserk should have fantasy tag", berserk?.tags?.any { it.tagName == "fantasy" } == true)
    }
}
