package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta del endpoint de autenticación de Spotify cuando se solicita un token.
 * Las anotaciones @SerializedName se usan para mapear los nombres de los campos del JSON
 * (que suelen usar snake_case) a los nombres de las propiedades en Kotlin (que usan camelCase).
 */
data class Token(
    // El token de acceso que se usará para hacer llamadas a la API.
    @SerializedName("access_token")
    val accessToken: String,

    // El tipo de token. Casi siempre será "Bearer".
    @SerializedName("token_type")
    val tokenType: String,

    // El tiempo en segundos que el token de acceso es válido.
    @SerializedName("expires_in")
    val expiresIn: Int,

    // Un token que se puede usar para obtener un nuevo token de acceso sin que el usuario
    // tenga que volver a iniciar sesión. Es opcional y no siempre se incluye.
    @SerializedName("refresh_token")
    val refreshToken: String?,

    // Una cadena con los scopes (permisos) que han sido otorgados, separados por espacios.
    val scope: String
)
