package com.example.project01_group05.api

import com.google.gson.annotations.SerializedName

/**
 * Generic wrapper for MangaDex API responses.
 */
data class MangaDexResponse<T>(
    val result: String,
    val response: String,
    val data: T
)

/**
 * Response model for the manga feed (list of chapters) endpoint.
 */
data class ChapterListResponse(
    val result: String,
    val response: String,
    val data: List<ChapterData>,
    val limit: Int,
    val offset: Int,
    val total: Int
)

/**
 * Data container for an individual chapter in an API response.
 */
data class ChapterData(
    val id: String,
    val type: String,
    val attributes: ChapterAttributes
)

/**
 * Metadata attributes for a chapter.
 */
data class ChapterAttributes(
    val volume: String?,
    val chapter: String?,
    val title: String?,
    val pages: Int,
    val publishAt: String,
    val readableAt: String,
    val externalUrl: String?
)

/**
 * Response model for the MangaDex@Home server locator.
 * Provides the base URL and hash for downloading images.
 */
data class AtHomeResponse(
    val baseUrl: String,
    val chapter: AtHomeChapter
)

/**
 * Contains the image filenames and hash for a specific chapter.
 */
data class AtHomeChapter(
    val hash: String,
    val data: List<String>,
    val dataSaver: List<String>
)
