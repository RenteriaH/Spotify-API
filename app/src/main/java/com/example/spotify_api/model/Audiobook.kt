package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para un Audiobook completo, obtenido de /audiobooks/{id}.
 */
data class Audiobook(
    val id: String,
    val name: String,
    val href: String,
    val authors: List<Author>,
    val narrators: List<Narrator>,
    val publisher: String,
    val description: String,
    @SerializedName("html_description") val htmlDescription: String,
    val explicit: Boolean,
    val images: List<Image>,
    val languages: List<String>,
    val edition: String?,
    @SerializedName("total_chapters") val totalChapters: Int,
    @SerializedName("media_type") val mediaType: String,
    val chapters: PagingObject<SimplifiedChapter>,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val uri: String,
    val type: String,
    @SerializedName("available_markets") val availableMarkets: List<String>,
    val copyrights: List<Copyright>
)

/**
 * Modelo de datos para un Audiobook simplificado, usado en búsquedas o librerías.
 */
data class SimplifiedAudiobook(
    val id: String,
    val name: String,
    val authors: List<Author>,
    val narrators: List<Narrator>,
    val publisher: String,
    val description: String,
    @SerializedName("html_description") val htmlDescription: String,
    val explicit: Boolean,
    val images: List<Image>,
    val languages: List<String>,
    val edition: String?,
    @SerializedName("total_chapters") val totalChapters: Int,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val uri: String,
    val type: String,
    @SerializedName("available_markets") val availableMarkets: List<String>,
    val copyrights: List<Copyright>
)

/**
 * Objeto de respuesta para la petición de varios audiolibros.
 */
data class SeveralAudiobooksResponse(
    val audiobooks: List<Audiobook?>
)

data class AudiobookSearchResult(
    val items: List<SimplifiedAudiobook>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

/**
 * Modelo para un capítulo de un audiolibro.
 */
data class SimplifiedChapter(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("html_description") val htmlDescription: String,
    @SerializedName("chapter_number") val chapterNumber: Int,
    @SerializedName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    val images: List<Image>,
    @SerializedName("is_playable") val isPlayable: Boolean,
    val languages: List<String>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String,
    @SerializedName("audio_preview_url") val audioPreviewUrl: String?,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val uri: String,
    val type: String,
    val href: String,
    @SerializedName("available_markets") val availableMarkets: List<String>?,
    @SerializedName("resume_point") val resumePoint: ResumePoint?,
    val restrictions: Restrictions?
)

/**
 * Modelo para el autor de un audiolibro.
 */
data class Author(
    val name: String
)

/**
 * Modelo para el narrador de un audiolibro.
 */
data class Narrator(
    val name: String
)

/**
 * Modelo para las restricciones de contenido.
 */
data class Restrictions(
    val reason: String
)
