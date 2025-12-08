package com.example.spotify_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.spotify_api.navigation.NavManager
import com.example.spotify_api.playback.SpotifyPlaybackManager
import com.example.spotify_api.ui.theme.SpotifyAPITheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var playbackManager: SpotifyPlaybackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpotifyAPITheme {
                // ¡Se pasa el gestor de reproducción al NavManager!
                NavManager(playbackManager = playbackManager)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        playbackManager.connect()
    }

    override fun onStop() {
        super.onStop()
        playbackManager.disconnect()
    }
}
