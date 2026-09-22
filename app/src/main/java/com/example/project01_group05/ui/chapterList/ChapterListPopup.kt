package com.example.project01_group05.ui.chapterList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project01_group05.api.ChapterData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListPopup(
    mangaDexId: String,
    viewModel: ChapterListViewModel,
    onDismissRequest: () -> Unit,
    onChapterClick: (ChapterData) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(mangaDexId) {
        viewModel.loadChapters(mangaDexId)
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Chapters",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.chapters) { chapter ->
                        var showDeleteDialog by remember { mutableStateOf(false) }

                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text("Delete Chapter") },
                                text = { Text("Are you sure you want to delete this chapter?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.deleteChapter(mangaDexId, chapter.id)
                                        showDeleteDialog = false
                                    }) {
                                        Text("Delete")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDeleteDialog = false }) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }

                        ChapterItem(
                            chapter = chapter,
                            isDownloaded = uiState.downloadedChapters.contains(chapter.id),
                            downloadProgress = uiState.downloadProgress[chapter.id],
                            onDownloadClick = { viewModel.downloadChapter(mangaDexId, chapter) },
                            onLongClick = {
                                if (uiState.downloadedChapters.contains(chapter.id)) {
                                    showDeleteDialog = true
                                }
                            },
                            onClick = { onChapterClick(chapter) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChapterItem(
    chapter: ChapterData,
    isDownloaded: Boolean,
    downloadProgress: Float?,
    onDownloadClick: () -> Unit,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            val title = chapter.attributes.title ?: "Chapter ${chapter.attributes.chapter ?: "?"}"
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            if (chapter.attributes.chapter != null) {
                Text(
                    text = "Chapter ${chapter.attributes.chapter}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (downloadProgress != null) {
            CircularProgressIndicator(
                progress = { downloadProgress },
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else if (isDownloaded) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Downloaded",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        } else {
            TextButton(onClick = onDownloadClick) {
                Text("Download")
            }
        }
    }
}
