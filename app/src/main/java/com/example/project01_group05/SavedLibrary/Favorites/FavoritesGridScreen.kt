package com.example.project01_group05.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavoritesGridScreen(
    viewModel: FavoriteModel,
    onMangaClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val mangas by viewModel.mangas.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextButton(
            onClick = onBackClick,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text("<- Back")
        }

        if (mangas.isEmpty()) {
            EmptyFavoritesMessage()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(
                    items = mangas,
                    key = { manga -> manga.id }
                ) { manga ->
                    FavoriteMangaCard(
                        manga = manga,
                        onClick = {
                            manga.mangaDexId?.let { id ->
                                onMangaClick(id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyFavoritesMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No favorite manga yet.")
    }
}