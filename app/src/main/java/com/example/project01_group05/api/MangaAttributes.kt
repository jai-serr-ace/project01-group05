package com.example.project01_group05.api

data class MangaAttributes(
    val title: Map<String, String>? = null,
    val tags: List<TagObject>? = null
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

    fun getTagNames(): List<String> {
        return tags?.mapNotNull { tag ->
            tag.attributes?.name?.get("en") ?: tag.attributes?.name?.values?.firstOrNull()
        } ?: emptyList()
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
