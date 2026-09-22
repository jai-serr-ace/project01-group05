package com.example.project01_group05.ui.search

import com.example.project01_group05.api.MangaCallback
import com.example.project01_group05.api.MangaSearchRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MangaSearchViewModelTest {

    @Test
    fun setSafeOnly_updatesState() {
        val fakeRepo = object : MangaSearchRepository {
            override fun searchManga(
                title: String,
                contentRatings: List<String>,
                callback: MangaCallback
            ) {
                callback.onSuccess(emptyList())
            }
        }

        val viewModel = MangaSearchViewModel(fakeRepo)

        assertTrue(viewModel.uiState.value.isSafeOnly)

        viewModel.setSafeOnly(false)

        assertFalse(viewModel.uiState.value.isSafeOnly)
    }

    @Test
    fun searchManga_passesCorrectContentRatings() {
        var passedRatings: List<String>? = null

        val fakeRepo = object : MangaSearchRepository {
            override fun searchManga(
                title: String,
                contentRatings: List<String>,
                callback: MangaCallback
            ) {
                passedRatings = contentRatings
                callback.onSuccess(emptyList())
            }
        }

        val viewModel = MangaSearchViewModel(fakeRepo)
        viewModel.updateQuery("Naruto")

        // Search with Safe Only enabled by default
        viewModel.search()
        assertEquals(listOf("safe"), passedRatings)

        // Disable Safe Only and search again
        viewModel.setSafeOnly(false)
        viewModel.search()
        assertEquals(listOf("safe", "suggestive", "erotica", "pornographic"), passedRatings)
    }
}
