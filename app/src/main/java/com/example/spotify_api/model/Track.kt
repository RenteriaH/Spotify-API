package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class Track(
    val album: SimplifiedAlbum,
    val artists: List<Artist>,
    @SerializedName("available_markets") val availableMarkets: List<String>?,
    @SerializedName("disc_number") val discNumber: Int,
    @SerializedName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Int,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("track_number") val trackNumber: Int,
    val type: String,
    val uri: String
)

data class SimplifiedTrack(
    val artists: List<Artist>,
    @SerializedName("available_markets") val availableMarkets: List<String>,
    @SerializedName("disc_number") val discNumber: Int,
    @SerializedName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    @SerializedName("is_local") val isLocal: Boolean,
    val name: String,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("track_number") val trackNumber: Int,
    val type: String,
    val uri: String
)

data class Tracks(
    val href: String,
    val items: List<SimplifiedTrack>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

data class TrackSearchResult(
    val items: List<Track>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)
