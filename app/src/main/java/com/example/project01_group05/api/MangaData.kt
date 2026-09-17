package com.example.project01_group05.api

data class MangaData(
    val id: String,
    val type: String,
    val attributes: MangaAttributes,
    val relationships: List<Relationship>? = null
) {
    fun getCoverImageUrl(): String? {
        val coverRel = relationships?.find { it.type == "cover_art" }
        val fileName = coverRel?.attributes?.fileName
        if (!fileName.isNullOrEmpty()) {
            return "https://uploads.mangadex.org/covers/$id/$fileName.256.jpg"
        }
        return null
    }
}

data class Relationship(
    val id: String,
    val type: String,
    val attributes: RelationshipAttributes?
)

data class RelationshipAttributes(
    val fileName: String?
)
