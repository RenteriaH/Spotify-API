package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

// --- Estructuras para Búsqueda y Endpoints Generales ---

data class SearchResponse(
    val albums: AlbumSearchResult? = null,
    val artists: ArtistSearchResult? = null,
    val playlists: PlaylistSearchResult? = null,
    val tracks: TrackSearchResult? = null,
    // ¡AÑADIDO! La respuesta de búsqueda ahora puede contener audiolibros.
    val audiobooks: AudiobookSearchResult? = null 
)

data class AlbumSearchResult(
    val items: List<Album>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

data class ArtistSearchResult(
    val items: List<Artist>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

data class TrackSearchResult(
    val items: List<Track>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

// ¡AÑADIDO! El objeto que contiene los resultados de una búsqueda de audiolibros.

data class AudiobookSearchResult(
    val items: List<SimplifiedAudiobook>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

// --- Estructuras para la Pantalla de Artista ---

data class ArtistTopTracksResponse(val tracks: List<Track>)
data class ArtistAlbumsResponse(val items: List<SimplifiedAlbum>)
data class RelatedArtistsResponse(val artists: List<Artist>)

// --- Modelos de Datos Principales ---

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

data class Artist(
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String,
    val images: List<Image>? = null,
    val genres: List<String>? = null,
    val popularity: Int? = null,
    val followers: Followers? = null
)

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

// --- Modelos Simplificados y Auxiliares ---

data class SimplifiedAlbum(
    @SerializedName("album_type") val albumType: String,
    @SerializedName("total_tracks") val totalTracks: Int,
    val id: String,
    val images: List<Image>,
    val name: String,
    @SerializedName("release_date") val releaseDate: String,
    val artists: List<Artist>
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

data class Image(val url: String, val height: Int?, val width: Int?)
data class ExternalUrls(val spotify: String)
data class Copyright(val text: String, val type: String)

data class Tracks(
    val href: String,
    val items: List<SimplifiedTrack>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

data class Followers(
    val href: String?,
    val total: Int
)
