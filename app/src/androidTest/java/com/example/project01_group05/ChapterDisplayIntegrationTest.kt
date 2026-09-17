package com.example.project01_group05

import android.content.Context
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project01_group05.api.MangaDexClient
import com.example.project01_group05.ui.chapterDisplay.DisplayChapterActivity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChapterDisplayIntegrationTest {

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    private val chainsawManMangaId = "a77742b1-befd-49a4-bff5-1ad4e6b0ef7b"

    @Test
    fun launchChapterReader_withChainsawManChapter1() = runBlocking {
        val apiService = MangaDexClient.getApiService()

        // 1. Get the chapter feed for Chainsaw Man
        val feedResponse = apiService.getMangaFeed(chainsawManMangaId)
        
        // 2. Find Chapter 1 (that is not an external link)
        val chapter1 = feedResponse.data.find { it.attributes.chapter == "1" && it.attributes.externalUrl == null }
        assertNotNull("Chapter 1 of Chainsaw Man (hosted on MangaDex) should exist", chapter1)
        
        val chapterId = chapter1!!.id
        val chapterTitle = chapter1.attributes.title ?: "Chapter 1"

        // 3. Launch the activity
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = DisplayChapterActivity.newIntent(
            context = context,
            mangaId = chainsawManMangaId,
            chapterId = chapterId,
            title = chapterTitle
        )

        ActivityScenario.launch<DisplayChapterActivity>(intent).use { scenario ->
            // 4. Wait for the ViewModel to load pages
            var pagesLoaded = false
            var error: String? = null
            val timeout = 15000L // 15 seconds
            val start = System.currentTimeMillis()

            while (System.currentTimeMillis() - start < timeout) {
                scenario.onActivity { activity ->
                    val state = activity.viewModel.uiState.value
                    if (!state.isLoading) {
                        pagesLoaded = state.pages.isNotEmpty()
                        error = state.error
                    }
                }
                if (pagesLoaded || error != null) break
                Thread.sleep(500)
            }

            assertNull("Error occurred while loading chapter: $error", error)
            assertTrue("No pages were retrieved from the API or storage within $timeout ms", pagesLoaded)
            
            // 5. Wait until "Next Chapter" button is visible (end of chapter)
            composeTestRule.waitUntil(timeoutMillis = 60000) {
                composeTestRule
                    .onAllNodesWithText("Next Chapter")
                    .fetchSemanticsNodes().isNotEmpty()
            }
        }
    }
}
