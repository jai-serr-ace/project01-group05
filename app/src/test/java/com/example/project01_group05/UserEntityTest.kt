package com.example.project01_group05

import com.example.project01_group05.database.entities.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit test for [UserEntity].
 */
class UserEntityTest {

    @Test
    fun userEntity_creation_isCorrect() {
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
    fun userEntity_adminStatus_isCorrect() {
        val admin = UserEntity(
            username = "admin",
            password = "adminpassword",
            isAdmin = true
        )
        assertEquals(true, admin.isAdmin)
    }
}
