package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Representa el perfil de un usuario de Spotify, basado en el endpoint GET /v1/me.
 */
data class UserProfile(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
    val email: String,
    val country: String,
    val followers: Followers,
    val images: List<Image>,      // Reutiliza el modelo de Imagen ya existente.
    val product: String,        // Nivel de suscripción: "premium", "free", etc.
    val type: String,
    val uri: String
)

/**
 * Representa la respuesta del endpoint que devuelve los artistas más escuchados por el usuario.
 */
data class TopArtistsResponse(
    val items: List<Artist> // Reutiliza el modelo de Artista ya existente.
)
