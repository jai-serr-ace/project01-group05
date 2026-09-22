package com.example.project01_group05.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.project01_group05.api.MangaData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String = "",
    viewModel: HomeViewModel = viewModel(),
    onLogoutClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onMangaSelected: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSettingsMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.selectedTagForSeeAll != null) {
                            "${uiState.selectedTagForSeeAll} Manga"
                        } else if (username.isNotBlank()) {
                            "Welcome, $username!"
                        } else {
                            "MangaReader"
                        }
                    )
                },
                navigationIcon = {
                    if (uiState.selectedTagForSeeAll != null) {
                        TextButton(onClick = { viewModel.selectTagForSeeAll(null) }) {
                            Text("< Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    if (uiState.selectedTagForSeeAll == null) {
                        IconButton(onClick = onSearchClick) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                    Box {
                        IconButton(onClick = { showSettingsMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                        DropdownMenu(
                            expanded = showSettingsMenu,
                            onDismissRequest = { showSettingsMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Safe Mode")
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Switch(
                                            checked = uiState.isSafeOnly,
                                            onCheckedChange = { isChecked ->
                                                viewModel.toggleSafeOnly(isChecked)
                                            }
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.toggleSafeOnly(!uiState.isSafeOnly)
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    showSettingsMenu = false
                                    onLogoutClick()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = uiState.errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadManga() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                uiState.selectedTagForSeeAll != null -> {
                    // "See All" Expanded Grid for a specific Tag
                    val tagManga = uiState.mangaByTag[uiState.selectedTagForSeeAll] ?: emptyList()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tagManga, key = { it.id }) { manga ->
                            MangaCoverCard(
                                manga = manga,
                                onClick = { onMangaSelected(manga.id) }
                            )
                        }
                    }
                }
                else -> {
                    // Sections by Tag with horizontal scrolling (up to 10 covers) and "See All"
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(uiState.popularTags, key = { it }) { tag ->
                            val mangaListForTag = uiState.mangaByTag[tag] ?: emptyList()
                            if (mangaListForTag.isNotEmpty()) {
                                TagSection(
                                    tag = tag,
                                    mangaList = mangaListForTag.take(10), // Limit to 10 covers
                                    onSeeAllClick = { viewModel.selectTagForSeeAll(tag) },
                                    onMangaClick = onMangaSelected
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagSection(
    tag: String,
    mangaList: List<MangaData>,
    onSeeAllClick: () -> Unit,
    onMangaClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.titleLarge
            )
            TextButton(onClick = onSeeAllClick) {
                Text("See All")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mangaList, key = { it.id }) { manga ->
                Box(modifier = Modifier.width(140.dp)) {
                    MangaCoverCard(
                        manga = manga,
                        onClick = { onMangaClick(manga.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MangaCoverCard(
    manga: MangaData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                val coverUrl = manga.getCoverImageUrl()
                if (coverUrl != null) {
                    AsyncImage(
                        model = coverUrl,
                        contentDescription = manga.attributes.getDisplayTitle(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Cover", style = MaterialTheme.typography.bodySmall)
                    }
                }

                val rating = manga.attributes.contentRating
                if (!rating.isNullOrEmpty()) {
                    val is18 = rating == "erotica" || rating == "pornographic"
                    Surface(
                        color = if (is18) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = if (is18) "18+" else rating.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (is18) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = manga.attributes.getDisplayTitle(),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
