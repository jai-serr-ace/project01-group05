package com.example.project01_group05.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project01_group05.database.entities.UserEntity

//The database connects  the userEntity and UserDao together so
//Room can create and manage the database

//Room will create a users table based on your UserEntity
@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDao
}
