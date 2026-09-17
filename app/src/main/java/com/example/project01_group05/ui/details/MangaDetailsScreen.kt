package com.example.project01_group05.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project01_group05.api.MangaData

@Composable
fun MangaDetailsScreen(
    mangaId: String,
    viewModel: MangaDetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(mangaId) {
        viewModel.loadManga(mangaId)
    }

    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
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
                    text = uiState.errorMessage
                        ?: "Unable to load manga details.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        uiState.manga != null -> {
            MangaDetailsContent(
                manga = uiState.manga!!,
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
fun MangaDetailsContent(
    manga: MangaData,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(
            onClick = onBackClick
        ) {
            Text("<- Back")
        }

        Text(
            text = manga.attributes.getDisplayTitle(),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = manga.attributes.getDisplayDescription(),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        MangaDetailRow(
            label = "Status",
            value = manga.attributes.status
                ?.replaceFirstChar { it.uppercase() }
                ?: "Unknown"
        )

        MangaDetailRow(
            label = "Year",
            value = manga.attributes.year?.toString()
                ?: "Unknown"
        )

        MangaDetailRow(
            label = "Original Language",
            value = manga.attributes.originalLanguage
                ?: "Unknown"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "MangaDex ID",
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = manga.id,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MangaDetailRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}