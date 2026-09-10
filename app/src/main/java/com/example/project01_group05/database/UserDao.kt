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

    // put this new user or UserEntity into the users table or database
    @Insert
    suspend fun insertUser(user: UserEntity)

    //deletes an account from user database
    @Delete
    suspend fun deleteUser(user: UserEntity)

    //go to the users table and get all the users and return it as list
    //this means I need a SQL to run these commands through
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    // searches for a user using their username and the ? means
    // it can return null if the username does not exist in the database
    //it basically checks if a username is already taken when creating an account
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    //it checks if the user and the password match the account in the database
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): UserEntity?
}
