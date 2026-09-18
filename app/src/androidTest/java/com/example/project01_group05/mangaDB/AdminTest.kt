package com.example.project01_group05.mangaDB

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.project01_group05.database.UserDao
import com.example.project01_group05.database.entities.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AdminTest {

    private lateinit var database: MangaDB
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MangaDB::class.java
        )
            .allowMainThreadQueries()
            .build()

        userDao = database.userDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun adminLoginReturnsAdminUser() = runBlocking {
        val admin = UserEntity(
            username = "admin",
            password = "admin",
            isAdmin = true
        )

        userDao.insertUser(admin)

        val result = userDao.login("admin", "admin")

        assertEquals(true, result?.isAdmin)
    }
}