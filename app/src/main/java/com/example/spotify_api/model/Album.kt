package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("album_type") val albumType: String? = null,
    @SerializedName("total_tracks") val totalTracks: Int,
    @SerializedName("available_markets") val availableMarkets: List<String>? = null,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String? = null,
    val type: String,
    val uri: String,
    val artists: List<Artist>,
    val tracks: Tracks? = null,
    val popularity: Int? = null,
    val label: String? = null,
    val copyrights: List<Copyright>? = null
)

data class SimplifiedAlbum(
    @SerializedName("album_type") val albumType: String,
    @SerializedName("total_tracks") val totalTracks: Int,
    val id: String,
    val images: List<Image>,
    val name: String,
    @SerializedName("release_date") val releaseDate: String,
    val artists: List<Artist>
)

data class SavedAlbum(
    @SerializedName("added_at") val addedAt: String,
    val album: Album
)

data class AlbumSearchResult(
    val items: List<Album>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

data class NewReleasesResponse(
    val albums: AlbumSearchResult
)
