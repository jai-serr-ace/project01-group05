package com.example.project01_group05

import com.example.project01_group05.api.MangaAttributes
import com.example.project01_group05.api.MangaData
import com.example.project01_group05.api.MangaResponse
import org.junit.Assert
import org.junit.Test

class MangaModelsTest {

    @Test
    fun mangaResponse_containsMockMangaData() {

        val attributes = MangaAttributes(
            title = mapOf(
                "en" to "Mock Manga"
            )
        )

        val manga = MangaData(
            id = "mock-manga-id-123",
            type = "manga",
            attributes = attributes
        )

        val response = MangaResponse(
            result = "ok",
            data = listOf(manga)
        )

        Assert.assertEquals("ok", response.result)

        Assert.assertNotNull(response.data)

        Assert.assertFalse(response.data.isEmpty())

        Assert.assertEquals(
            "mock-manga-id-123",
            response.data[0].id
        )

        Assert.assertEquals(
            "Mock Manga",
            response.data[0].attributes.getDisplayTitle()
        )
    }

    @Test
    fun getDisplayTitle_prefersEnglishTitle() {

        val attributes = MangaAttributes(
            title = mapOf(
                "ja" to "テストマンガ",
                "en" to "Test Manga"
            )
        )

        Assert.assertEquals(
            "Test Manga",
            attributes.getDisplayTitle()
        )
    }

    @Test
    fun getDisplayTitle_usesAvailableTitleWhenEnglishMissing() {

        val attributes = MangaAttributes(
            title = mapOf(
                "ja" to "テストマンガ"
            )
        )

        Assert.assertEquals(
            "テストマンガ",
            attributes.getDisplayTitle()
        )
    }

    @Test
    fun getDisplayTitle_returnsUnknownWhenTitleMissing() {

        val attributes = MangaAttributes(
            title = null
        )

        Assert.assertEquals(
            "Unknown Title",
            attributes.getDisplayTitle()
        )
    }
}