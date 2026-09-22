package com.example.project01_group05.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project01_group05.api.MangaData

@Composable
fun MangaSearchScreen(
    viewModel: MangaSearchViewModel,
    onMangaSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(
            onClick = onBackClick
        ) {
            Text("<- Back")
        }

        Text(
            text = "Search Manga",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { newQuery ->
                    viewModel.updateQuery(newQuery)
                },
                label = {
                    Text("Manga title")
                },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Button(
                onClick = {
                    viewModel.search()
                }
            ) {
                Text("Search")
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )
        }

        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(
                    vertical = 8.dp
                )
            )
        }

        if (
            uiState.hasSearched &&
            !uiState.isLoading &&
            uiState.errorMessage == null &&
            uiState.results.isEmpty()
        ) {
            Text(
                text = "No manga found.",
                modifier = Modifier.padding(
                    vertical = 8.dp
                )
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(
                12.dp
            )
        ) {
            items(
                items = uiState.results,
                key = { manga ->
                    manga.id
                }
            ) { manga ->
                MangaSearchResultItem(
                    manga = manga,
                    onClick = {
                        onMangaSelected(manga.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun MangaSearchResultItem(
    manga: MangaData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val coverUrl = manga.getCoverImageUrl()

            if (coverUrl != null) {
                AsyncImage(
                    model = coverUrl,
                    contentDescription = manga.attributes.getDisplayTitle(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(
                        width = 80.dp,
                        height = 120.dp
                    )
                )
            } else {
                Card(
                    modifier = Modifier.size(
                        width = 80.dp,
                        height = 120.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No Cover",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = manga.attributes.getDisplayTitle(),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    val rating = manga.attributes.contentRating
                    if (!rating.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        val is18 = rating == "erotica" || rating == "pornographic"
                        Surface(
                            color = if (is18) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.extraSmall
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

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "MangaDex ID: ${manga.id}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}