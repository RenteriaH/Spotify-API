package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para una Playlist de Spotify (respuesta del endpoint /playlists/{id}).
 */
data class Playlist(
    val id: String,
    val name: String,
    val collaborative: Boolean,
    val description: String?,
    val owner: PlaylistOwner,
    val images: List<Image>?,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val tracks: PagingObject<PlaylistTrack>?,
    val uri: String,
    val type: String,
    @SerializedName("snapshot_id") val snapshotId: String,
    val public: Boolean,
    val followers: Followers
)

/**
 * Un item dentro de una playlist, que puede ser una canción o un episodio.
 * Gson poblará los campos que correspondan y dejará los otros como null.
 */
data class PlaylistItem(
    // Campos comunes
    val id: String,
    val name: String,
    val type: String, // "track" o "episode"
    @SerializedName("duration_ms") val durationMs: Int,
    val explicit: Boolean,

    // Campos específicos de Track
    val album: SimplifiedAlbum?,
    val artists: List<Artist>?,

    // Campos específicos de Episode
    val images: List<Image>?,
    val show: SimplifiedShow?
)

/**
 * Contenedor para cada item en una playlist. El item puede ser una canción o un episodio.
 */
data class PlaylistTrack(
    @SerializedName("added_at") val addedAt: String?,
    @SerializedName("added_by") val addedBy: UserProfile?,
    @SerializedName("is_local") val isLocal: Boolean,
    val track: PlaylistItem? // MODIFICADO: Antes era Track?, ahora es PlaylistItem?
)

/**
 * Modelo para el dueño de una playlist.
 */
data class PlaylistOwner(
    val id: String,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val uri: String
)

/**
 * Modelo para la respuesta de una búsqueda de playlists.
 */
data class PlaylistSearchResult(
    val items: List<Playlist>,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
)

/**
 * Objeto de respuesta para la petición de playlists destacadas.
 */
data class FeaturedPlaylistsResponse(
    val message: String,
    val playlists: PlaylistSearchResult
)

/**
 * Objeto genérico para paginación, usado por la API de Spotify.
 */
data class PagingObject<T>(
    val href: String,
    val items: List<T>,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
)

// --- ¡NUEVO MODELO AÑADIDO! ---
data class PlayHistoryObject(
    val track: Track,
    @SerializedName("played_at") val playedAt: String
)
