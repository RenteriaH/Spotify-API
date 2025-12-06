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
    private var expiresAt: Long = 0L

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
            this.token = null 
            false
        }
    }

    suspend fun getBearerToken(): String? {
        if (token == null) {
            return null
        }

        if (isTokenExpired()) {
            if (refreshToken() == null) {
                return null
            }
        }

        return token?.let { "Bearer ${it.accessToken}" }
    }

    private fun isTokenExpired(): Boolean {
        if (expiresAt == 0L) return true
        return System.currentTimeMillis() >= expiresAt - 60000 
    }

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
            this.token = null 
            null
        }
    }

    private fun updateToken(newToken: Token) {
        this.token = newToken
        this.expiresAt = System.currentTimeMillis() + (newToken.expiresIn * 1000L)
    }

    fun getAuthUrl(): String {
        // --- ¡CAMBIO AQUÍ! ---
        val scopes = "user-read-private user-read-email user-top-read user-library-read playlist-read-private playlist-read-collaborative user-read-recently-played"
        return "${ApiConstants.ACCOUNTS_BASE_URL}authorize?"
            .plus("response_type=code")
            .plus("&client_id=${ApiConstants.CLIENT_ID}")
            .plus("&scope=$scopes")
            .plus("&redirect_uri=${ApiConstants.REDIRECT_URI}")
    }
}
