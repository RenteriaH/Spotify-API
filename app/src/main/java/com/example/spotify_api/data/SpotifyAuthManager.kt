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

    suspend fun getAccessToken(code: String): String? {
        if (token == null) {
            token = fetchToken(code)
        }
        return token?.accessToken
    }

    private suspend fun fetchToken(code: String): Token? {
        return try {
            val authHeader = "Basic " + Base64.encodeToString(
                "${ApiConstants.CLIENT_ID}:${ApiConstants.CLIENT_SECRET}".toByteArray(),
                Base64.NO_WRAP
            )
            authApiService.getToken(
                authHeader = authHeader,
                grantType = "authorization_code",
                code = code,
                redirectUri = ApiConstants.REDIRECT_URI
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getAuthUrl(): String {
        // ¡CORREGIDO! Se añade el permiso "user-top-read" para poder ver los artistas favoritos.
        val scopes = "user-read-private user-read-email user-top-read"
        return "${ApiConstants.ACCOUNTS_BASE_URL}authorize?"
            .plus("response_type=code")
            .plus("&client_id=${ApiConstants.CLIENT_ID}")
            .plus("&scope=$scopes")
            .plus("&redirect_uri=${ApiConstants.REDIRECT_URI}")
    }

    fun getBearerToken(): String? {
        return token?.let { "Bearer ${it.accessToken}" }
    }
}
