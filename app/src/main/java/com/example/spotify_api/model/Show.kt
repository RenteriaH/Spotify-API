package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class Show(
    @SerializedName("available_markets") val availableMarkets: List<String>,
    val copyrights: List<Copyright>,
    val description: String,
    val explicit: Boolean,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    @SerializedName("is_externally_hosted") val isExternallyHosted: Boolean,
    val languages: List<String>,
    @SerializedName("media_type") val mediaType: String,
    val name: String,
    val publisher: String,
    val type: String,
    val uri: String,
    @SerializedName("total_episodes") val totalEpisodes: Int,
    @SerializedName("html_description") val htmlDescription: String,
    val episodes: PagingObject<SimplifiedEpisode>
)

data class SimplifiedShow(
    @SerializedName("available_markets") val availableMarkets: List<String>,
    val copyrights: List<Copyright>,
    val description: String,
    val explicit: Boolean,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    @SerializedName("is_externally_hosted") val isExternallyHosted: Boolean,
    val languages: List<String>,
    @SerializedName("media_type") val mediaType: String,
    val name: String,
    val publisher: String,
    val type: String,
    val uri: String,
    @SerializedName("total_episodes") val totalEpisodes: Int,
    @SerializedName("html_description") val htmlDescription: String
)

data class ShowSearchResult(
    val items: List<SimplifiedShow>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

data class SeveralShowsResponse(
    val shows: List<SimplifiedShow>
)

data class SavedShow(
    @SerializedName("added_at") val addedAt: String,
    val show: SimplifiedShow
)

data class Episode(
    @SerializedName("audio_preview_url") val audioPreviewUrl: String?,
    val description: String,
    @SerializedName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    @SerializedName("html_description") val htmlDescription: String,
    val id: String,
    val images: List<Image>,
    @SerializedName("is_externally_hosted") val isExternallyHosted: Boolean,
    @SerializedName("is_playable") val isPlayable: Boolean,
    val languages: List<String>,
    val name: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String,
    @SerializedName("resume_point") val resumePoint: ResumePoint?,
    val type: String,
    val uri: String,
    val show: SimplifiedShow
)

data class SimplifiedEpisode(
    @SerializedName("audio_preview_url") val audioPreviewUrl: String?,
    val description: String,
    @SerializedName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    @SerializedName("html_description") val htmlDescription: String,
    val id: String,
    val images: List<Image>,
    @SerializedName("is_externally_hosted") val isExternallyHosted: Boolean,
    @SerializedName("is_playable") val isPlayable: Boolean,
    val languages: List<String>,
    val name: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String,
    @SerializedName("resume_point") val resumePoint: ResumePoint?,
    val type: String,
    val uri: String
)
