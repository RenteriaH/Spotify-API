package com.example.spotify_api.ui

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.viewModel.AuthState
import com.example.spotify_api.viewModel.LoginViewModel

/**
 * Composable que representa la pantalla de inicio de sesión.
 * Utiliza un WebView para mostrar la página de autorización de Spotify.
 * Intercepta la URL de callback para evitar la pantalla de error "Página web no disponible".
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {
    val authState by loginViewModel.authState.collectAsState()
    var webViewVisible by remember { mutableStateOf(true) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            navController.navigate(Routes.Main.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (webViewVisible) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webViewClient = object : WebViewClient() {
                            /**
                             * Intercepta la carga de la URL antes de que se muestre.
                             * Esta es la solución para evitar la pantalla de error net::ERR_UNKNOWN_URL_SCHEME.
                             */
                            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                val url = request?.url.toString()
                                if (url.startsWith("spotify-api-app://callback")) {
                                    // Ocultamos la WebView para una transición más limpia
                                    webViewVisible = false
                                    // Extraemos el código de autorización de la URL
                                    val code = url.substringAfter("code=").substringBefore("&")
                                    // Le pasamos el código al ViewModel para que lo intercambie por un token
                                    loginViewModel.handleAuthCode(code)
                                    // Devolvemos 'true' para indicarle a la WebView que hemos manejado la URL y no debe intentar cargarla.
                                    return true
                                }
                                // Para cualquier otra URL (navegación dentro de spotify.com), dejamos que la WebView la cargue.
                                return false
                            }
                        }
                        loadUrl(loginViewModel.getAuthUrl())
                    }
                }
            )
        }

        // Se muestra un indicador de carga mientras se intercambia el código por el token.
        if (authState is AuthState.Loading || !webViewVisible) {
            CircularProgressIndicator()
        }
    }
}
