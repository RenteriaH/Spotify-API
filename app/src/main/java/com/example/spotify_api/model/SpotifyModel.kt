package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

// Root object for the entire search response
data class SearchResult(
    val albums: AlbumResult? = null
)

// Contains the list of albums from a search
data class AlbumResult(
    val totalCount: Int,
    val items: List<AlbumItem>
)

// Represents a single item in the search album list
data class AlbumItem(
    val data: AlbumData
)

// The actual data for a searched album
data class AlbumData(
    val uri: String,
    val name: String,
    val artists: ArtistCollection,
    val coverArt: CoverArt,
    val date: AlbumDate
)

// A collection of artists for an album
data class ArtistCollection(
    val items: List<ArtistItem>
)

// A single artist from a search result
data class ArtistItem(
    val uri: String,
    val profile: ArtistProfile
)

// The artist's profile information from a search result
data class ArtistProfile(
    val name: String
)

// The cover art for an album
data class CoverArt(
    val sources: List<ImageSource>
)

// A single image source for the cover art
data class ImageSource(
    val url: String,
    val width: Int,
    val height: Int
)

// The release date of an album
data class AlbumDate(
    val year: Int
)

// --- DATA CLASSES FOR ALBUM DETAILS ---

// Root object for the album details response
data class AlbumDetailsResponse(
    val albums: List<AlbumFullDetails>
)

// Represents the full details of a single album
data class AlbumFullDetails(
    val id: String,
    val name: String,
    val images: List<ImageSource>,
    @SerializedName("total_tracks") val totalTracks: Int,
    val artists: List<ArtistDetails>,
    val tracks: TrackCollection,
    @SerializedName("release_date") val releaseDate: String
)

// Artist details from the album details endpoint
data class ArtistDetails(
    val id: String,
    val name: String
)

// A collection of tracks
data class TrackCollection(
    val items: List<TrackItem>
)

// A single track item
data class TrackItem(
    val id: String,
    @SerializedName("track_number") val trackNumber: Int,
    @SerializedName("duration_ms") val durationMs: Int,
    val name: String,
    val artists: List<ArtistDetails> // <-- I ADDED THIS MISSING FIELD
)
