package com.example.spotify_api.data

import android.util.Base64
import com.example.spotify_api.api.ApiConstants
import com.example.spotify_api.api.AuthApiService
import com.example.spotify_api.model.Token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyAuthManager @Inject constructor(
    private val authApiService: AuthApiService
) {

    private var token: Token? = null
    // Almacena el timestamp en milisegundos en el que el token expira.
    private var expiresAt: Long = 0L

    /**
     * Intercambia el código de autorización por un token de acceso y lo almacena.
     * Se debe llamar después de que el usuario inicie sesión correctamente.
     */
    suspend fun exchangeCodeForToken(code: String): Boolean {
        return try {
            val authHeader = "Basic " + Base64.encodeToString(
                "${ApiConstants.CLIENT_ID}:${ApiConstants.CLIENT_SECRET}".toByteArray(),
                Base64.NO_WRAP
            )
            val newToken = authApiService.getToken(
                authHeader = authHeader,
                grantType = "authorization_code",
                code = code,
                redirectUri = ApiConstants.REDIRECT_URI
            )
            updateToken(newToken)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            this.token = null // Limpia el token si hay un error.
            false
        }
    }

    /**
     * Proporciona un Bearer Token válido, refrescándolo si está a punto de expirar.
     * Este es el método principal que deben usar los repositorios.
     */
    suspend fun getBearerToken(): String? {
        if (token == null) {
            // Si no hay token, el usuario debe iniciar sesión. No podemos hacer nada.
            return null
        }

        if (isTokenExpired()) {
            // Si el token ha expirado, intenta refrescarlo.
            if (refreshToken() == null) {
                // Si el refresco falla, no se puede obtener un token válido.
                return null
            }
        }

        // Devuelve el token de acceso formateado para las llamadas a la API.
        return token?.let { "Bearer ${it.accessToken}" }
    }

    /**
     * Comprueba si el token actual ha expirado o está a punto de hacerlo (con un margen de 60 segundos).
     */
    private fun isTokenExpired(): Boolean {
        if (expiresAt == 0L) return true
        return System.currentTimeMillis() >= expiresAt - 60000 // Margen de 60 segundos
    }

    /**
     * Intenta obtener un nuevo token de acceso usando el refresh_token guardado.
     */
    private suspend fun refreshToken(): Token? {
        val refreshToken = this.token?.refreshToken ?: return null

        return try {
            val authHeader = "Basic " + Base64.encodeToString(
                "${ApiConstants.CLIENT_ID}:${ApiConstants.CLIENT_SECRET}".toByteArray(),
                Base64.NO_WRAP
            )
            val refreshedToken = authApiService.refreshToken(
                authHeader = authHeader,
                grantType = "refresh_token",
                refreshToken = refreshToken
            )
            updateToken(refreshedToken)
            refreshedToken
        } catch (e: Exception) {
            e.printStackTrace()
            this.token = null // Si el refresco falla, se limpia el token para forzar un nuevo login.
            null
        }
    }

    /**
     * Actualiza el token interno y calcula su nuevo tiempo de expiración.
     */
    private fun updateToken(newToken: Token) {
        this.token = newToken
        // expiresIn viene en segundos. Lo convertimos a un timestamp en milisegundos.
        this.expiresAt = System.currentTimeMillis() + (newToken.expiresIn * 1000L)
    }

    /**
     * Construye la URL a la que se debe dirigir al usuario para que inicie sesión en Spotify.
     */
    fun getAuthUrl(): String {
        val scopes = "user-read-private user-read-email user-top-read"
        return "${ApiConstants.ACCOUNTS_BASE_URL}authorize?"
            .plus("response_type=code")
            .plus("&client_id=${ApiConstants.CLIENT_ID}")
            .plus("&scope=$scopes")
            .plus("&redirect_uri=${ApiConstants.REDIRECT_URI}")
    }
}
