package com.example.spotify_api.api

/**
 * Objeto que contiene todas las constantes necesarias para interactuar con la API de Spotify.
 * Centralizar estas constantes aquí facilita su mantenimiento y evita tener valores "mágicos" esparcidos por el código.
 */
object ApiConstants {
    // El ID de cliente único para tu aplicación, obtenido desde el dashboard de desarrolladores de Spotify.
    const val CLIENT_ID = "4b1960b175a94a5aa6b1277fd29a2ac1"

    // El secreto de cliente para tu aplicación. Es como una contraseña y debe mantenerse confidencial.
    // IMPORTANTE: Este valor no debe subirse a repositorios públicos.
    const val CLIENT_SECRET = "ed65955506ba4440b6046a5dfea81644"

    // La URI a la que Spotify redirigirá al usuario después de que autorice (o deniegue) la aplicación.
    // Usamos un esquema personalizado para que la app intercepte esta URL.
    const val REDIRECT_URI = "spotify-api-app://callback"

    // La URL base para los endpoints de autenticación de Spotify (solicitar y refrescar tokens).
    const val ACCOUNTS_BASE_URL = "https://accounts.spotify.com/"

    // La URL base para los endpoints principales de la API de Spotify (obtener álbumes, artistas, etc.).
    const val API_BASE_URL = "https://api.spotify.com/v1/"
}
