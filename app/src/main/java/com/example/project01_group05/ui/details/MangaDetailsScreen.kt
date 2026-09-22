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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project01_group05.api.MangaData
import com.example.project01_group05.ui.chapterDisplay.DisplayChapterActivity
import com.example.project01_group05.ui.chapterList.ChapterListPopup
import com.example.project01_group05.ui.chapterList.ChapterListViewModel

@Composable
fun MangaDetailsScreen(
    mangaId: String,
    viewModel: MangaDetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showChapterList by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(mangaId) {
        viewModel.loadManga(mangaId)
    }

    //This button displays the chapter list when clicked
    if (showChapterList && uiState.manga != null) {
        val chapterListViewModel: ChapterListViewModel = viewModel()
        ChapterListPopup(
            mangaDexId = uiState.manga!!.id,
            viewModel = chapterListViewModel,
            onDismissRequest = { showChapterList = false },
            onChapterClick = { chapter ->
                val orderedIds = chapterListViewModel.uiState.value.chapters.map { it.id }
                val intent = DisplayChapterActivity.newIntent(
                    context = context,
                    mangaId = uiState.manga!!.id,
                    chapterId = chapter.id,
                    title = chapter.attributes.title ?: "Chapter ${chapter.attributes.chapter ?: "?"}"
                ).apply {
                    putStringArrayListExtra("extra_chapter_ids", ArrayList(orderedIds))
                }
                context.startActivity(intent)
            }
        )
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
                onBackClick = onBackClick,
                onViewChaptersClick = { showChapterList = true }
            )
        }
    }
}

@Composable
fun MangaDetailsContent(
    manga: MangaData,
    onBackClick: () -> Unit,
    onViewChaptersClick: () -> Unit
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
        //This button displays the chapter list when clicked
        Button(
            onClick = onViewChaptersClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Chapters")
        }

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
