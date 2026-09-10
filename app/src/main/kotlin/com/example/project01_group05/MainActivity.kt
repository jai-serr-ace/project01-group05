package com.example.project01_group05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project01_group05.database.UserDatabase
import com.example.project01_group05.ui.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Create the Room database
        val database = Room.databaseBuilder(
            applicationContext, UserDatabase::class.java,
            "user_database"

        ).build()

        // Get the UserDao from the database
        val userDao = database.userDAO()
        // Pass the UserDao to the Login screen
        setContent {
            LoginScreen(userDao = userDao)
        }
    }
}