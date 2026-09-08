package com.example.project01_group05;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project01_group05.api.MangaCallback;
import com.example.project01_group05.api.MangaData;
import com.example.project01_group05.api.MangaRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class MangaDexApiTest {

    @Test
    public void fetchManga_returnsMangaIdsAndTitles() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        MangaRepository repository = new MangaRepository();

        repository.fetchManga(new MangaCallback() {
            @Override
            public void onSuccess(List<MangaData> mangaList) {

                assertNotNull(mangaList);
                assertFalse(mangaList.isEmpty());

                for (MangaData manga : mangaList) {

                    assertNotNull(manga.getId());
                    assertNotNull(manga.getAttributes());

                    String title = manga.getAttributes().getDisplayTitle();

                    assertNotNull(title);

                    System.out.println(
                            "Manga ID: " + manga.getId()
                                    + " | Title: " + title
                    );
                }

                latch.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("API Error: " + errorMessage);
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);
    }
}