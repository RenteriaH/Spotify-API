package com.example.spotify_api.api

import com.example.spotify_api.model.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interfaz de Retrofit para los servicios de autenticación de Spotify.
 * Define los endpoints relacionados con la obtención y actualización de tokens de acceso.
 * Su URL base es ACCOUNTS_BASE_URL (https://accounts.spotify.com/).
 */
interface AuthApiService {

    /**
     * Intercambia un código de autorización por un token de acceso.
     * Esta es una llamada POST al endpoint "api/token".
     *
     * @param authHeader El encabezado de autorización, que contiene el Client ID y el Client Secret codificados en Base64.
     *                   Ejemplo: "Authorization: Basic <base64_encoded_client_id:client_secret>"
     * @param grantType El tipo de concesión que se solicita. Para el flujo de código de autorización, siempre es "authorization_code".
     * @param code El código de autorización recibido de Spotify después de que el usuario aprueba la aplicación.
     * @param redirectUri La misma URI de redirección que se usó en la solicitud de autorización inicial. Se usa para validación.
     * @return Un objeto [Token] que contiene el token de acceso, el token de actualización, el tiempo de expiración, etc.
     */
    @FormUrlEncoded // Especifica que el cuerpo de la solicitud estará codificado como un formulario URL.
    @POST("api/token") // Define que esta es una solicitud POST al path "api/token".
    suspend fun getToken(
        @Header("Authorization") authHeader: String, // Parámetro que se enviará como un encabezado HTTP.
        @Field("grant_type") grantType: String,      // Parámetro que irá en el cuerpo de la solicitud.
        @Field("code") code: String,                  // Parámetro que irá en el cuerpo de la solicitud.
        @Field("redirect_uri") redirectUri: String    // Parámetro que irá en el cuerpo de la solicitud.
    ): Token
}
