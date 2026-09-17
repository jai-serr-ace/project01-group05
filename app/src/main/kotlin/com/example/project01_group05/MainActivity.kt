package com.example.project01_group05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.project01_group05.mangaDB.MangaDB
import com.example.project01_group05.ui.details.MangaDetailsScreen
import com.example.project01_group05.ui.details.MangaDetailsViewModel
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

            var selectedMangaId by remember {
                mutableStateOf<String?>(null)
            }

            var previousScreen by remember {
                mutableStateOf("home")
            }

            // Keep the search ViewModel alive when navigating
            // between Search and Manga Details.
            val searchViewModel = remember {
                MangaSearchViewModel()
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
                                selectedMangaId = null
                                previousScreen = "home"
                            },
                            onSearchClick = {
                                currentScreen = "search"
                            },
                            onMangaSelected = { mangaId ->
                                selectedMangaId = mangaId
                                previousScreen = "home"
                                currentScreen = "details"
                            }
                        )
                    }

                    "search" -> {
                        MangaSearchScreen(
                            viewModel = searchViewModel,
                            onMangaSelected = { mangaId ->
                                selectedMangaId = mangaId
                                previousScreen = "search"
                                currentScreen = "details"
                            },
                            onBackClick = {
                                currentScreen = "home"
                            }
                        )
                    }

                    "details" -> {
                        val mangaId = selectedMangaId

                        if (mangaId != null) {
                            val detailsViewModel = remember {
                                MangaDetailsViewModel()
                            }

                            MangaDetailsScreen(
                                mangaId = mangaId,
                                viewModel = detailsViewModel,
                                onBackClick = {
                                    selectedMangaId = null
                                    currentScreen = previousScreen
                                }
                            )
                        } else {
                            currentScreen = previousScreen
                        }
                    }
                }
            }
        }
    }
}