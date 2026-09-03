package com.example.project01_group05.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.project01_group05.database.entities.UserEntity

//suspend fun is a delay function that can pause while it waits for something to finish,
// without freezing the app

@Dao // DAO defines what you can do with that data
interface UserDao {

    // Put this new user or UserEntity into the users table or database
    @Insert
    suspend fun insertUser(user: UserEntity)

    //need to delete an account from user database
    @Delete
    suspend fun deleteUser(user: UserEntity)

    //Go to the users table and get all the users and return it as list
    //this means I need a SQL to run these commands through
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    //how to return the users info
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    //it checks if the user and the password exist within the database
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): UserEntity?
}
