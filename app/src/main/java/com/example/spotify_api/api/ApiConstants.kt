package com.example.spotify_api.api

/**
 * Objeto que contiene todas las constantes necesarias para interactuar con la API de Spotify.
 * Centralizar estas constantes aquí facilita su mantenimiento y evita tener valores "mágicos" esparcidos por el código.
 */
object ApiConstants {
    // El ID de cliente único para tu aplicación, obtenido desde el dashboard de desarrolladores de Spotify.
    const val CLIENT_ID = "44081bab66ff42fe8d4d7891e6e4d0fe"

    // El secreto de cliente para tu aplicación. Es como una contraseña y debe mantenerse confidencial.
    // IMPORTANTE: Este valor no debe subirse a repositorios públicos.
    const val CLIENT_SECRET = "90fbbf5ddb13422ba16d0c375b6a6c8a"

    // La URI a la que Spotify redirigirá al usuario después de que autorice (o deniegue) la aplicación.
    // Usamos un esquema personalizado para que la app intercepte esta URL.
    const val REDIRECT_URI = "spotify-api-app://callback"

    // La URL base para los endpoints de autenticación de Spotify (solicitar y refrescar tokens).
    const val ACCOUNTS_BASE_URL = "https://accounts.spotify.com/"

    // La URL base para los endpoints principales de la API de Spotify (obtener álbumes, artistas, etc.).
    const val API_BASE_URL = "https://api.spotify.com/v1/"
}
