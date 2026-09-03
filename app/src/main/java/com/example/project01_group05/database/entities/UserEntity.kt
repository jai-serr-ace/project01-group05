package com.example.project01_group05.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "users")
//@Entity tells room that this table will be called "users" for my database
//UserEntity becomes a table that collects info on username password and isAdmin
//A primary key is a value that uniquely identifies each row in a database table
//for example: student have student id numbers
@Entity(tableName = "users")
data class UserEntity(@PrimaryKey(autoGenerate = true)
    val username: String,
    val password: String,
    val isAdmin: Boolean = false
)