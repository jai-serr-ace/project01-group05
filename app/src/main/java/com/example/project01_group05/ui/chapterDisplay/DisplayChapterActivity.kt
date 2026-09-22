package com.example.project01_group05.ui.chapterDisplay

/**
 * This file contains the activity responsible for displaying manga chapters.
 * It serves as the entry point for the chapter reader experience, handling
 * the reception of manga and chapter IDs and wiring them to the UI components.
 *
 * Connection to Project:
 * - Launched from the Manga Detail screen or Search results.
 * - Uses [DisplayChapterViewModel] to manage state and fetch page data.
 * - Hosts [ChapterReaderScreen] to provide the Compose-based UI.
 */

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color

/**
 * Activity that hosts the manga chapter reader.
 * It extracts chapter information from the starting Intent and triggers data loading.
 */
class DisplayChapterActivity : ComponentActivity() {

    /**
     * The ViewModel responsible for managing chapter loading and reading state.
     */
    internal val viewModel: DisplayChapterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mangaId = intent.getStringExtra(EXTRA_MANGA_ID)
        var currentChapterId = intent.getStringExtra(EXTRA_CHAPTER_ID)
        val chapterIds = intent.getStringArrayListExtra("extra_chapter_ids") ?: emptyList<String>()

        fun loadCurrentChapter(cId: String) {
            if (mangaId != null) {
                // Next chapter corresponds to the one *above* it in the descending list (index - 1)
                val currentIndex = chapterIds.indexOf(cId)
                val hasNext = currentIndex > 0
                val sampleTitle = "Chapter ${chapterIds.size - currentIndex}"
                viewModel.loadChapter(mangaId, cId, sampleTitle, hasNext)
            }
        }

        if (currentChapterId != null) {
            loadCurrentChapter(currentChapterId)
        }

        setContent {
            MaterialTheme {
                Surface(color = Color.Black) {
                    ChapterReaderScreen(
                        viewModel = viewModel,
                        onNextChapter = {
                            val currentIndex = chapterIds.indexOf(currentChapterId)
                            if (currentIndex > 0) {
                                val nextChapterId = chapterIds[currentIndex - 1]
                                currentChapterId = nextChapterId
                                loadCurrentChapter(nextChapterId)
                            }
                        },
                        onBack = { finish() }
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_MANGA_ID = "extra_manga_id"
        const val EXTRA_CHAPTER_ID = "extra_chapter_id"
        const val EXTRA_CHAPTER_TITLE = "extra_chapter_title"

        /**
         * Creates an Intent to launch this activity with the required chapter details.
         */
        fun newIntent(context: Context, mangaId: String, chapterId: String, title: String): Intent {
            return Intent(context, DisplayChapterActivity::class.java).apply {
                putExtra(EXTRA_MANGA_ID, mangaId)
                putExtra(EXTRA_CHAPTER_ID, chapterId)
                putExtra(EXTRA_CHAPTER_TITLE, title)
            }
        }
    }
}
