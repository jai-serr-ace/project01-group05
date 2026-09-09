package com.example.project01_group05

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project01_group05.api.MangaCallback
import com.example.project01_group05.api.MangaData
import com.example.project01_group05.api.MangaRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MangaDexApiTest {

    @Test
    fun fetchManga_returnsMangaIdsAndTitles() {

        val latch = CountDownLatch(1)

        val repository = MangaRepository()

        var apiError: String? = null

        repository.fetchManga(object : MangaCallback {

            override fun onSuccess(mangaList: List<MangaData>) {

                assertNotNull(mangaList)
                assertFalse(mangaList.isEmpty())

                for (manga in mangaList) {

                    assertNotNull(manga.id)
                    assertNotNull(manga.attributes)

                    val title = manga.attributes.getDisplayTitle()

                    assertNotNull(title)

                    println(
                        "Manga ID: ${manga.id} | Title: $title"
                    )
                }

                latch.countDown()
            }

            override fun onError(errorMessage: String) {

                println(
                    "MANGADEX_API_ERROR: $errorMessage"
                )

                apiError = errorMessage

                latch.countDown()
            }
        })

        val completed = latch.await(
            30,
            TimeUnit.SECONDS
        )

        assertTrue(
            "MangaDex request timed out.",
            completed
        )

        assertNull(
            "MangaDex API returned an error: $apiError",
            apiError
        )
    }
}