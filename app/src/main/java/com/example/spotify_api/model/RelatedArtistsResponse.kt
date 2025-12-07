package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta de la API de Spotify para la petición de artistas relacionados.
 */
data class RelatedArtistsResponse(
    @SerializedName("artists")
    val artists: List<Artist>
)
