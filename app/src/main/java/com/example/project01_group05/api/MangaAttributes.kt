package com.example.project01_group05.api

data class MangaAttributes(
    val title: Map<String, String>? = null,
    val tags: List<TagObject>? = null,
    val description: Map<String, String>? = null,
    val status: String? = null,
    val year: Int? = null,
    val originalLanguage: String? = null,
    val contentRating: String? = null
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

    fun getTagNames(): List<String> {
        return tags?.mapNotNull { tag ->
            tag.attributes?.name?.get("en") ?: tag.attributes?.name?.values?.firstOrNull()
        } ?: emptyList()
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

data class TagObject(
    val id: String,
    val type: String,
    val attributes: TagAttributes?
)

data class TagAttributes(
    val name: Map<String, String>?
)
