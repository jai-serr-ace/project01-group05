package com.example.project01_group05

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.project01_group05.mangaDB.MangaDB
import com.example.project01_group05.ui.CreateAccountActivity
import com.example.project01_group05.ui.home.HomeScreen
import com.example.project01_group05.ui.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = MangaDB.getDatabase(applicationContext)
        val userDao = database.userDao()

        setContent {
            var loggedInUser by remember { mutableStateOf<String?>(null) }

            if (loggedInUser == null) {
                LoginScreen(
                    userDao = userDao,
                    onLoginSuccess = { username ->
                        loggedInUser = username
                    },
                    onCreateAccountClick = {
                        val intent = Intent(this, CreateAccountActivity::class.java)
                        startActivity(intent)
                    }
                )
            } else {
                HomeScreen(
                    username = loggedInUser ?: "",
                    onLogoutClick = {
                        loggedInUser = null
                    }
                )
            }
        }
    }
}