package com.example.project01_group05

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.project01_group05.api.MangaAttributes
import com.example.project01_group05.api.MangaCallback
import com.example.project01_group05.api.MangaData
import com.example.project01_group05.api.MangaSearchRepository
import com.example.project01_group05.ui.search.MangaSearchScreen
import com.example.project01_group05.ui.search.MangaSearchViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MangaSearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchDisplaysMatchingManga() {
        val fakeRepository = FakeMangaSearchRepository(
            results = listOf(
                MangaData(
                    id = "naruto-id-123",
                    type = "manga",
                    attributes = MangaAttributes(
                        title = mapOf(
                            "en" to "Naruto"
                        )
                    )
                )
            )
        )

        val viewModel = MangaSearchViewModel(
            fakeRepository
        )

        composeTestRule.setContent {
            MangaSearchScreen(
                viewModel = viewModel,
                onMangaSelected = {}
            )
        }

        composeTestRule
            .onNodeWithText("Manga title")
            .performTextInput("Naruto")

        composeTestRule
            .onNodeWithText("Search")
            .performClick()

        composeTestRule
            .onNodeWithText(
                "MangaDex ID: naruto-id-123"
            )
            .assertIsDisplayed()
    }

    @Test
    fun clickingMangaReturnsSelectedMangaId() {
        val fakeRepository = FakeMangaSearchRepository(
            results = listOf(
                MangaData(
                    id = "berserk-id-456",
                    type = "manga",
                    attributes = MangaAttributes(
                        title = mapOf(
                            "en" to "Berserk"
                        )
                    )
                )
            )
        )

        val viewModel = MangaSearchViewModel(
            fakeRepository
        )

        var selectedMangaId: String? = null

        composeTestRule.setContent {
            MangaSearchScreen(
                viewModel = viewModel,
                onMangaSelected = { mangaId ->
                    selectedMangaId = mangaId
                }
            )
        }

        composeTestRule
            .onNodeWithText("Manga title")
            .performTextInput("Berserk")

        composeTestRule
            .onNodeWithText("Search")
            .performClick()

        composeTestRule
            .onNodeWithText(
                "MangaDex ID: berserk-id-456"
            )
            .performClick()

        assertEquals(
            "berserk-id-456",
            selectedMangaId
        )
    }

    @Test
    fun searchWithNoResultsDisplaysNoMangaFound() {
        val fakeRepository = FakeMangaSearchRepository(
            results = emptyList()
        )

        val viewModel = MangaSearchViewModel(
            fakeRepository
        )

        composeTestRule.setContent {
            MangaSearchScreen(
                viewModel = viewModel,
                onMangaSelected = {}
            )
        }

        composeTestRule
            .onNodeWithText("Manga title")
            .performTextInput(
                "DefinitelyNotARealManga"
            )

        composeTestRule
            .onNodeWithText("Search")
            .performClick()

        composeTestRule
            .onNodeWithText("No manga found.")
            .assertIsDisplayed()
    }

    private class FakeMangaSearchRepository(
        private val results: List<MangaData>
    ) : MangaSearchRepository {

        override fun searchManga(
            title: String,
            callback: MangaCallback
        ) {
            callback.onSuccess(results)
        }
    }
}