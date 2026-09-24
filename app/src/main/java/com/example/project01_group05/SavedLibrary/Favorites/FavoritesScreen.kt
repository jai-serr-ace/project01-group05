package com.example.project01_group05.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project01_group05.mangaDB.MangaEntity

@Composable
fun FavoritesScreen(
    viewModel: FavoriteModel,
    onMangaClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    val mangas by viewModel.mangas.collectAsState()

    if (mangas.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "No favorite manga yet.",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        return
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        RowHeader(
            onSeeAllClick = onSeeAllClick
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = mangas.take(10),
                key = { manga -> manga.id }
            ) { manga ->

                Box(
                    modifier = Modifier.width(140.dp)
                ) {
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
fun RowHeader(
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Favorites"
        )

        TextButton(
            onClick = onSeeAllClick
        ) {
            Text("See All")
        }
    }
}

@Composable
fun FavoriteMangaCard(
    manga: MangaEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            if (!manga.cover.isNullOrEmpty()) {
                AsyncImage(
                    model = manga.cover,
                    contentDescription = manga.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Cover",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Text(
                text = manga.title ?: "Unknown Manga",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}