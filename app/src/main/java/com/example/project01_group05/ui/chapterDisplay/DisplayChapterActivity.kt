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
        val chapterId = intent.getStringExtra(EXTRA_CHAPTER_ID)
        val title = intent.getStringExtra(EXTRA_CHAPTER_TITLE)

        if (mangaId != null && chapterId != null) {
            viewModel.loadChapter(mangaId, chapterId, title)
        }

        setContent {
            MaterialTheme {
                Surface(color = Color.Black) {
                    ChapterReaderScreen(
                        viewModel = viewModel,
                        onNextChapter = {
                            // Logic to transition to the next chapter
                            // This could involve getting the next chapter ID from the Intent or API
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
