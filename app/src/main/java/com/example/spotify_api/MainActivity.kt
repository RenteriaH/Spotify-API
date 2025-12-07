package com.example.spotify_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.spotify_api.navigation.NavManager
import com.example.spotify_api.ui.theme.SpotifyAPITheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * La actividad principal y único punto de entrada de la aplicación.
 * Está anotada con @AndroidEntryPoint para habilitar la inyección de dependencias de Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // ¡Limpio y correcto! Solo instalamos el Splash Screen.
        // Se mostrará durante el tiempo real que tarde la app en arrancar.
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge() // Habilita el modo de pantalla completa de borde a borde.
        setContent {
            // Se aplica el tema de la aplicación definido en ui.theme.
            SpotifyAPITheme {
                // Se llama al NavManager, que se encargará de toda la lógica de navegación.
                NavManager()
            }
        }
    }
}
