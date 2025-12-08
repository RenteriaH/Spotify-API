package com.example.spotify_api.playback

import android.content.Context
import android.util.Log
import com.example.spotify_api.api.ApiConstants
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyPlaybackManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var spotifyAppRemote: SpotifyAppRemote? = null

    fun connect() {
        if (spotifyAppRemote?.isConnected == true) {
            Log.d("SpotifyPlaybackManager", "Already connected.")
            return
        }

        val connectionParams = ConnectionParams.Builder(ApiConstants.CLIENT_ID)
            .setRedirectUri(ApiConstants.REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("SpotifyPlaybackManager", "Connected to Spotify!")
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifyPlaybackManager", "Connection to Spotify failed: ${throwable.message}")
            }
        })
    }

    fun disconnect() {
        spotifyAppRemote?.let {
            if (it.isConnected) {
                SpotifyAppRemote.disconnect(it)
                Log.d("SpotifyPlaybackManager", "Disconnected from Spotify.")
            }
        }
    }

    fun play(spotifyUri: String) {
        spotifyAppRemote?.playerApi?.play(spotifyUri)?.setResultCallback {
            Log.d("SpotifyPlaybackManager", "Playback started for URI: $spotifyUri")
        }?.setErrorCallback {
            Log.e("SpotifyPlaybackManager", "Error starting playback: ${it.message}")
        }
    }

    fun pause() {
        spotifyAppRemote?.playerApi?.pause()?.setResultCallback {
            Log.d("SpotifyPlaybackManager", "Playback paused.")
        }?.setErrorCallback {
            Log.e("SpotifyPlaybackManager", "Error pausing playback: ${it.message}")
        }
    }

    fun resume() {
        spotifyAppRemote?.playerApi?.resume()?.setResultCallback {
            Log.d("SpotifyPlaybackManager", "Playback resumed.")
        }?.setErrorCallback {
            Log.e("SpotifyPlaybackManager", "Error resuming playback: ${it.message}")
        }
    }
}
