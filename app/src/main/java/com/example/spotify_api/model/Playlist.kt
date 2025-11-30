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
    val images: List<Image>,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    // ¡CORREGIDO! El objeto tracks ahora contiene un objeto de paginación de PlaylistTrack.
    val tracks: PagingObject<PlaylistTrack>,
    val uri: String,
    val type: String,
    @SerializedName("snapshot_id") val snapshotId: String,
    val public: Boolean
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
 * ¡AÑADIDO! Este es el "envoltorio" para cada canción en una playlist.
 * Contiene la información de la canción en la propiedad `track`.
 */
data class PlaylistTrack(
    @SerializedName("added_at") val addedAt: String?,
    @SerializedName("added_by") val addedBy: UserProfile?,
    @SerializedName("is_local") val isLocal: Boolean,
    // La canción en sí puede ser nula si no está disponible.
    val track: Track?
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
