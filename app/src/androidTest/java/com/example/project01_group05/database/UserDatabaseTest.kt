package com.example.project01_group05.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project01_group05.database.entities.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDatabaseTest {

    private lateinit var database: UserDatabase
    private lateinit var dao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            UserDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.userDAO()
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun userEntity_canBeCreated() {
        val user = UserEntity(
            username = "testuser",
            password = "testpassword",
            isAdmin = false
        )

        assertEquals("testuser", user.username)
        assertEquals("testpassword", user.password)
        assertEquals(false, user.isAdmin)
    }

    @Test
    fun insertUser_andGetUserByUsername_returnsCorrectUser() = runBlocking {
        val user = UserEntity(
            username = "testuser",
            password = "testpassword",
            isAdmin = false
        )

        dao.insertUser(user)

        val result = dao.getUserByUsername("testuser")

        assertNotNull(result)
        assertEquals("testuser", result?.username)
        assertEquals("testpassword", result?.password)
        assertEquals(false, result?.isAdmin)
    }

    @Test
    fun login_withCorrectUsernameAndPassword_returnsUser() = runBlocking {
        val user = UserEntity(
            username = "testuser",
            password = "testpassword",
            isAdmin = false
        )

        dao.insertUser(user)

        val result = dao.login("testuser", "testpassword")

        assertNotNull(result)
        assertEquals("testuser", result?.username)
    }

    @Test
    fun login_withIncorrectPassword_returnsNull() = runBlocking {
        val user = UserEntity(
            username = "testuser",
            password = "testpassword",
            isAdmin = false
        )

        dao.insertUser(user)

        val result = dao.login("testuser", "wrongpassword")

        assertNull(result)
    }

    @Test
    fun insertAdminUser_adminStatusIsCorrect() = runBlocking {
        val admin = UserEntity(
            username = "admin",
            password = "adminpassword",
            isAdmin = true
        )

        dao.insertUser(admin)

        val result = dao.getUserByUsername("admin")

        assertNotNull(result)
        assertEquals(true, result?.isAdmin)
    }

    @Test
    fun deleteUser_removesUserFromDatabase() = runBlocking {
        val user = UserEntity(
            username = "testuser",
            password = "testpassword",
            isAdmin = false
        )

        dao.insertUser(user)

        val insertedUser = dao.getUserByUsername("testuser")
        assertNotNull(insertedUser)

        dao.deleteUser(insertedUser!!)

        val result = dao.getUserByUsername("testuser")

        assertNull(result)
    }
}