package com.example.project01_group05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.project01_group05.mangaDB.MangaDB
import com.example.project01_group05.ui.home.HomeScreen
import com.example.project01_group05.ui.login.CreateAccountScreen
import com.example.project01_group05.ui.login.LoginScreen
import com.example.project01_group05.ui.search.MangaSearchScreen
import com.example.project01_group05.ui.search.MangaSearchViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = MangaDB.getDatabase(applicationContext)
        val userDao = database.userDao()

        setContent {
            var loggedInUser by remember {
                mutableStateOf<String?>(null)
            }

            var currentScreen by remember {
                mutableStateOf("login")
            }

            if (loggedInUser == null) {
                if (currentScreen == "createAccount") {
                    CreateAccountScreen(
                        userDao = userDao,
                        onAccountCreated = {
                            currentScreen = "login"
                        },
                        onBackClick = {
                            currentScreen = "login"
                        }
                    )
                } else {
                    LoginScreen(
                        userDao = userDao,
                        onLoginSuccess = { username ->
                            loggedInUser = username
                            currentScreen = "home"
                        },
                        onCreateAccountClick = {
                            currentScreen = "createAccount"
                        }
                    )
                }
            } else {
                when (currentScreen) {
                    "home" -> {
                        HomeScreen(
                            username = loggedInUser ?: "",
                            onLogoutClick = {
                                loggedInUser = null
                                currentScreen = "home"
                            },
                            onSearchClick = {
                                currentScreen = "search"
                            }
                        )
                    }

                    "search" -> {
                        val searchViewModel = remember {
                            MangaSearchViewModel()
                        }

                        MangaSearchScreen(
                            viewModel = searchViewModel,
                            onMangaSelected = { mangaId ->
                                // Manga details will be connected later.
                            },
                            onBackClick = {
                                currentScreen = "home"
                            }
                        )
                    }
                }
            }
        }
    }
}