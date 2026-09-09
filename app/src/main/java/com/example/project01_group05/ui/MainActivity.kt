package com.example.project01_group05.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.project01_group05.ui.search.MangaSearchScreen
import com.example.project01_group05.ui.search.MangaSearchViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    MangaSearchScreen(
                        viewModel = MangaSearchViewModel(),
                        onMangaSelected = { mangaId ->
                            println("Selected MangaDex ID: $mangaId")
                        }
                    )
                }
            }
        }
    }
}
