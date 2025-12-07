package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta del endpoint de recomendaciones de Spotify.
 * Contiene una lista de canciones recomendadas.
 */
data class RecommendationsResponse(
    @SerializedName("tracks") val tracks: List<Track>
)
