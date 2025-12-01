package com.example.spotify_api.api

import com.example.spotify_api.model.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interfaz de Retrofit para los servicios de autenticación de Spotify.
 */
interface AuthApiService {

    /**
     * Intercambia un código de autorización por un token de acceso.
     */
    @FormUrlEncoded
    @POST("api/token")
    suspend fun getToken(
        @Header("Authorization") authHeader: String,
        @Field("grant_type") grantType: String,      // Valor: "authorization_code"
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Token

    /**
     * Refresca un token de acceso que ha expirado, usando un refresh_token.
     */
    @FormUrlEncoded
    @POST("api/token")
    suspend fun refreshToken(
        @Header("Authorization") authHeader: String,
        @Field("grant_type") grantType: String,      // Valor: "refresh_token"
        @Field("refresh_token") refreshToken: String
    ): Token
}
