package com.example.project01_group05.ui.chapterDisplay

/**
 * This file contains the Jetpack Compose UI for the manga chapter reader.
 * It provides an immersive reading experience with support for multiple reading modes
 * and a settings drawer.
 *
 * Connection to Project:
 * - Hosted by [DisplayChapterActivity].
 * - Observes state from [DisplayChapterViewModel].
 * - Uses Coil for asynchronous image loading of manga pages.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

/**
 * The main screen for reading a manga chapter.
 * It includes a top app bar, a navigation drawer for settings, and the actual page reader.
 *
 * @param viewModel The ViewModel providing the chapter data and state.
 * @param onNextChapter Callback triggered when the user wants to proceed to the next chapter.
 * @param onBack Callback triggered when the user wants to exit the reader.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterReaderScreen(
    viewModel: DisplayChapterViewModel,
    onNextChapter: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Reading Settings", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Right to Left") },
                    selected = uiState.readingMode is ReadingMode.RightToLeft,
                    onClick = {
                        viewModel.setReadingMode(ReadingMode.RightToLeft)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Vertical Scroll") },
                    selected = uiState.readingMode is ReadingMode.Vertical,
                    onClick = {
                        viewModel.setReadingMode(ReadingMode.Vertical)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = Color.Black,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(uiState.chapterTitle) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black.copy(alpha = 0.7f),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    when (uiState.readingMode) {
                        is ReadingMode.RightToLeft -> RTLPager(
                            pages = uiState.pages,
                            chapterTitle = uiState.chapterTitle,
                            hasNext = uiState.hasNextChapter,
                            onEndOfChapter = onNextChapter
                        )
                        is ReadingMode.Vertical -> VerticalReader(
                            pages = uiState.pages,
                            chapterTitle = uiState.chapterTitle,
                            hasNext = uiState.hasNextChapter,
                            onEndOfChapter = onNextChapter
                        )
                    }
                }
            }
        }
    }
}

/**
 * A horizontal pager that displays manga pages in a Right-to-Left (RTL) flow.
 */
@Composable
fun RTLPager(pages: List<String>, chapterTitle: String, hasNext: Boolean, onEndOfChapter: () -> Unit) {
    // We add +1 for the end-of-chapter view
    val pagerState = rememberPagerState(pageCount = { pages.size + 1 })
    
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        reverseLayout = true,
        beyondViewportPageCount = 1
    ) { pageIndex ->
        if (pageIndex < pages.size) {
            MangaPage(imageUrl = pages[pageIndex])
        } else {
            EndOfChapterView(
                currentChapterTitle = chapterTitle,
                hasNext = hasNext,
                onNextChapter = onEndOfChapter
            )
        }
    }
}

/**
 * A vertical pager that displays manga pages in a continuous top-to-bottom flow.
 */
@Composable
fun VerticalReader(pages: List<String>, chapterTitle: String, hasNext: Boolean, onEndOfChapter: () -> Unit) {
    // We add +1 for the end-of-chapter view
    val pagerState = rememberPagerState(pageCount = { pages.size + 1 })
    
    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        beyondViewportPageCount = 1
    ) { pageIndex ->
        if (pageIndex < pages.size) {
            MangaPage(imageUrl = pages[pageIndex])
        } else {
            EndOfChapterView(
                currentChapterTitle = chapterTitle,
                hasNext = hasNext,
                onNextChapter = onEndOfChapter
            )
        }
    }
}

/**
 * Displays a single manga page image, centered and scaled to fit the screen.
 */
@Composable
fun MangaPage(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Manga Page",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * Displays the end-of-chapter message and provides a button to proceed to the next chapter.
 */
@Composable
fun EndOfChapterView(currentChapterTitle: String, hasNext: Boolean, onNextChapter: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "End of $currentChapterTitle",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNextChapter,
            enabled = hasNext
        ) {
            Text(if (hasNext) "Next Chapter" else "No more chapters left")
        }
    }
}
