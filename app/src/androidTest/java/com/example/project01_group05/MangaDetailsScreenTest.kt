package com.example.project01_group05

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.project01_group05.api.MangaAttributes
import com.example.project01_group05.api.MangaData
import com.example.project01_group05.ui.details.MangaDetailsContent
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MangaDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testManga = MangaData(
        id = "test-manga-id",
        type = "manga",
        attributes = MangaAttributes(
            title = mapOf(
                "en" to "Test Manga"
            ),
            description = mapOf(
                "en" to "This is a test manga description."
            ),
            status = "ongoing",
            year = 2024,
            originalLanguage = "ja"
        )
    )

    @Test
    fun mangaDetails_displaysCorrectInformation() {
        composeTestRule.setContent {
            MangaDetailsContent(
                manga = testManga,
                onBackClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Test Manga")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("This is a test manga description.")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Ongoing")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("2024")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("ja")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("test-manga-id")
            .assertIsDisplayed()
    }

    @Test
    fun backButton_callsOnBackClick() {
        var backClicked = false

        composeTestRule.setContent {
            MangaDetailsContent(
                manga = testManga,
                onBackClick = {
                    backClicked = true
                }
            )
        }

        composeTestRule
            .onNodeWithText("<- Back")
            .performClick()

        assertTrue(backClicked)
    }
}