package com.example.spotify_api.di

import android.content.Context
import com.example.spotify_api.playback.SpotifyPlaybackManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaybackModule {

    @Provides
    @Singleton
    fun provideSpotifyPlaybackManager(
        @ApplicationContext context: Context
    ): SpotifyPlaybackManager {
        return SpotifyPlaybackManager(context)
    }
}
