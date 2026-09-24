package com.example.project01_group05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project01_group05.database.entities.UserEntity
import com.example.project01_group05.mangaDB.MangaDB
import com.example.project01_group05.ui.admin.AdminScreen
import com.example.project01_group05.ui.admin.UserManagementScreen
import com.example.project01_group05.ui.details.MangaDetailsScreen
import com.example.project01_group05.ui.details.MangaDetailsViewModel
import com.example.project01_group05.ui.downloadManager.DownloadManagerScreen
import com.example.project01_group05.ui.downloadManager.DownloadManagerViewModel
import com.example.project01_group05.ui.home.HomeScreen
import com.example.project01_group05.ui.home.HomeViewModel
import com.example.project01_group05.ui.login.CreateAccountScreen
import com.example.project01_group05.ui.login.LoginScreen
import com.example.project01_group05.ui.search.MangaSearchScreen
import com.example.project01_group05.ui.search.MangaSearchViewModel
import kotlinx.coroutines.launch
import com.example.project01_group05.ui.favorites.FavoriteModel
import com.example.project01_group05.ui.favorites.FavoritesGridScreen

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
                mutableStateOf("login")
            }

            var selectedMangaId by remember {
                mutableStateOf<String?>(null)
            }

            var previousScreen by remember {
                mutableStateOf("home")
            }

            val homeViewModel: HomeViewModel = viewModel()

            // Keep the search ViewModel alive when navigating
            // between Search and Manga Details.
            val searchViewModel = remember {
                MangaSearchViewModel()
            }

            val favoriteModel: FavoriteModel = viewModel(
                factory = FavoriteModel.FavoriteModelFactory(
                    database.mangaDao()
                )
            )




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
                        onLoginSuccess = { user ->
                            loggedInUser = user

                            if (user.isAdmin) {
                                currentScreen = "admin"
                            } else {
                                currentScreen = "home"
                            }
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
                            username = loggedInUser?.username ?: "",
                            viewModel = homeViewModel,
                            onLogoutClick = {
                                loggedInUser = null
                                currentScreen = "login"
                                selectedMangaId = null
                                previousScreen = "home"
                            },
                            onSearchClick = {
                                searchViewModel.setSafeOnly(homeViewModel.uiState.value.isSafeOnly)
                                currentScreen = "search"
                            },
                            onSettingsClick = {
                                currentScreen = "settings"
                            },

                            onFavoritesClick = {
                                currentScreen = "favorites"
                            },
                            onMangaSelected = { mangaId ->
                                selectedMangaId = mangaId
                                previousScreen = "home"
                                currentScreen = "details"
                            }
                        )
                    }

                    "favorites" -> {
                        FavoritesGridScreen(
                            viewModel = favoriteModel,
                            onMangaClick = { mangaId ->
                                selectedMangaId = mangaId
                                previousScreen = "favorites"
                                currentScreen = "details"
                            },
                            onBackClick = {
                                currentScreen ="home"
                            }

                        )
                    }



                    "settings" -> {
                        val downloadViewModel = remember {
                            DownloadManagerViewModel(application)
                        }
                        DownloadManagerScreen(
                            viewModel = downloadViewModel,
                            onBackClick = {
                                currentScreen = "home"
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

                    "admin" -> {
                        AdminScreen(
                            username = loggedInUser?.username ?: "",
                            onUsersClick = {
                                currentScreen = "users"
                            },
                            onMangaClick = {
                            },
                            onLogoutClick = {
                                loggedInUser = null
                                currentScreen = "login"
                            }
                        )
                    }

                    "users" -> {
                        UserManagementScreen(
                            userDao = userDao,
                            onBackClick = {
                                currentScreen = "admin"
                            }
                        )
                    }
                }
            }
        }
    }
}
