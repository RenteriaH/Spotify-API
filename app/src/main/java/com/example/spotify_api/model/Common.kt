package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class Image(val url: String, val height: Int?, val width: Int?)
data class ExternalUrls(val spotify: String)

data class Followers(
    val href: String?,
    val total: Int
)

data class SearchResponse(
    val albums: AlbumSearchResult? = null,
    val artists: ArtistSearchResult? = null,
    val playlists: PlaylistSearchResult? = null,
    val tracks: TrackSearchResult? = null,
    val audiobooks: AudiobookSearchResult? = null,
    val shows: ShowSearchResult? = null
)

data class ResumePoint(
    @SerializedName("fully_played") val fullyPlayed: Boolean,
    @SerializedName("resume_position_ms") val resumePositionMs: Int
)

data class Copyright(
    val text: String,
    val type: String
)

data class Restrictions(
    val reason: String
)
