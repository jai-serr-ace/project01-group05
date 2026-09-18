package com.example.project01_group05.ui.downloadManager

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadManagerScreen(
    viewModel: DownloadManagerViewModel,
    onBackClick: () -> Unit,
    onMangaSelected: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val folderPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) viewModel.selectDownloadFolder(uri)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings & Downloads") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Download Folder", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(uiState.selectedFolderName, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = if (uiState.selectedFolderUri == null) {
                            "New chapters are stored in the app's private folder."
                        } else {
                            "New chapters are stored here. Existing downloads stay where they are."
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                    uiState.locationError?.let { message ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            folderPicker.launch(uiState.selectedFolderUri?.let(Uri::parse))
                        }) {
                            Text(if (uiState.selectedFolderUri == null) "Choose Folder" else "Change Folder")
                        }
                        if (uiState.selectedFolderUri != null) {
                            OutlinedButton(onClick = viewModel::useAppDefaultFolder) {
                                Text("Use App Default")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Cache", style = MaterialTheme.typography.titleMedium)
                        Text("Current usage: ${uiState.cacheSize}", style = MaterialTheme.typography.bodySmall)
                    }
                    Button(onClick = viewModel::clearCache) { Text("Clean Cache") }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Downloaded Chapters", style = MaterialTheme.typography.headlineSmall)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.downloadedChapters, key = { it.chapterId }) { chapter ->
                    val mangaDexId = uiState.mangaIdToDexId[chapter.mangaId]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (mangaDexId != null && mangaDexId.isNotEmpty()) {
                                    Modifier.clickable { onMangaSelected(mangaDexId) }
                                } else {
                                    Modifier
                                }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = chapter.title ?: "Chapter ${chapter.chapterNumber ?: "?"}",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Manga ID: ${chapter.mangaId}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        IconButton(onClick = { viewModel.deleteChapter(chapter) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
