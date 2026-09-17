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
import com.example.project01_group05.ui.details.MangaDetailsScreen
import com.example.project01_group05.ui.details.MangaDetailsViewModel
import com.example.project01_group05.ui.home.HomeScreen
import com.example.project01_group05.ui.login.LoginScreen
import com.example.project01_group05.ui.search.MangaSearchScreen
import com.example.project01_group05.ui.search.MangaSearchViewModel
import com.example.project01_group05.database.entities.UserEntity
import com.example.project01_group05.ui.admin.AdminScreen
import com.example.project01_group05.ui.admin.UserManagementScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = MangaDB.getDatabase(applicationContext)
        val userDao = database.userDao()

        lifecycleScope.launch {
            val existingAdmin = userDao.getUserByUsername("admin")

            if (existingAdmin == null) {
                userDao.insertUser(
                    UserEntity(
                        username = "admin",
                        password = "admin",
                        isAdmin = true
                    )
                )
            }
        }


        setContent {
            var loggedInUser by remember {
                mutableStateOf<UserEntity?>(null)
            }

            var currentScreen by remember {
                mutableStateOf("home")
            }

            var selectedMangaId by remember {
                mutableStateOf<String?>(null)
            }

            if (loggedInUser == null) {
                LoginScreen(
                    userDao = userDao,
                    onLoginSuccess = { user  ->
                        loggedInUser = user
                        if(user.isAdmin) {
                            currentScreen ="admin"
                        } else {
                            currentScreen = "home"
                        }
                    },
                    onCreateAccountClick = {
                        val intent = Intent(
                            this,
                            CreateAccountActivity::class.java
                        )
                        startActivity(intent)
                    }
                )
            } else {
                when (currentScreen) {
                    "home" -> {
                        HomeScreen(
                            username = loggedInUser?.username ?: "",
                            onLogoutClick = {
                                loggedInUser = null
                                currentScreen = "home"
                                selectedMangaId = null
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
                                selectedMangaId = mangaId
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
                                    currentScreen = "search"
                                }
                            )
                        } else {
                            currentScreen = "search"
                        }
                    }

                    "admin" -> {
                        AdminScreen(
                            username = loggedInUser?.username?: "",
                            onUsersClick = {
                                currentScreen = "users"
                            },
                            onMangaClick = {


                            },
                            onLogoutClick = {
                                loggedInUser = null
                                currentScreen = "home"
                            }


                        )
                    }

                    "users" -> {
                        UserManagementScreen(
                            userDao = userDao,
                            onBackClick = {
                                currentScreen = "admin"
                            }                        )
                    }


                }
            }
        }
    }
}