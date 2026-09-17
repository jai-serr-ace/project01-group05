package com.example.project01_group05.api

data class MangaAttributes(
    val title: Map<String, String>?,
    val description: Map<String, String>? = null,
    val status: String? = null,
    val year: Int? = null,
    val originalLanguage: String? = null
) {

    fun getDisplayTitle(): String {
        if (title.isNullOrEmpty()) {
            return "Unknown Title"
        }

        val englishTitle = title["en"]

        if (!englishTitle.isNullOrEmpty()) {
            return englishTitle
        }

        for (value in title.values) {
            if (value.isNotEmpty()) {
                return value
            }
        }

        return "Unknown Title"
    }

    fun getDisplayDescription(): String {
        if (description.isNullOrEmpty()) {
            return "No description available."
        }

        val englishDescription = description["en"]

        if (!englishDescription.isNullOrEmpty()) {
            return englishDescription
        }

        for (value in description.values) {
            if (value.isNotEmpty()) {
                return value
            }
        }

        return "No description available."
    }
}