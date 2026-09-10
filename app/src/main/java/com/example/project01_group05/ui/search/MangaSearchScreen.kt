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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project01_group05.api.MangaData

@Composable
fun MangaSearchScreen(
    viewModel: MangaSearchViewModel,
    onMangaSelected: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                modifier = Modifier.padding(4.dp)
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
                8.dp
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(12.dp)
    ) {
        Text(
            text = manga.attributes.getDisplayTitle(),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "MangaDex ID: ${manga.id}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}