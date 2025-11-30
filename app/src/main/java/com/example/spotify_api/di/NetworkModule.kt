package com.example.spotify_api.di

import com.example.spotify_api.api.ApiConstants
import com.example.spotify_api.api.AuthApiService
import com.example.spotify_api.api.SpotifyApiService
import com.example.spotify_api.data.SpotifyAuthManager
import com.example.spotify_api.repository.SpotifyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("SpotifyApiRetrofit")
    fun provideSpotifyApiRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Named("AuthApiRetrofit")
    fun provideAuthApiRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.ACCOUNTS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSpotifyApiService(@Named("SpotifyApiRetrofit") retrofit: Retrofit): SpotifyApiService = retrofit.create(SpotifyApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApiService(@Named("AuthApiRetrofit") retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideSpotifyAuthManager(authApiService: AuthApiService): SpotifyAuthManager = SpotifyAuthManager(authApiService)

    @Provides
    @Singleton
    fun provideSpotifyRepository(spotifyApiService: SpotifyApiService, authManager: SpotifyAuthManager): SpotifyRepository = SpotifyRepository(spotifyApiService, authManager)
}
