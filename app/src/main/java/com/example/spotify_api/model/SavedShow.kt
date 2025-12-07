package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Representa un programa guardado en la biblioteca del usuario.
 * Contiene la fecha en que se guardó y la información del programa.
 */
data class SavedShow(
    @SerializedName("added_at") val addedAt: String,
    val show: SimplifiedShow
)
