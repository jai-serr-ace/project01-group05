package com.example.project01_group05.api

data class MangaAttributes(
    val title: Map<String, String>?
) {

    fun getDisplayTitle(): String {

        if (title.isNullOrEmpty()) {
            return "Unknown Title"
        }

        // Prefer English when available
        val englishTitle = title["en"]

        if (!englishTitle.isNullOrEmpty()) {
            return englishTitle
        }

        // Otherwise use whichever localized title MangaDex provides
        for (value in title.values) {
            if (value.isNotEmpty()) {
                return value
            }
        }

        return "Unknown Title"
    }
}