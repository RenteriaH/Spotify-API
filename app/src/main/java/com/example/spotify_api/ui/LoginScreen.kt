package com.example.spotify_api.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {
    // Se obtiene el estado de autenticación desde el ViewModel.
    val authState by loginViewModel.authState.collectAsState()

    // Se usa LaunchedEffect para manejar la navegación como un efecto secundario.
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            // Si la autenticación es exitosa, navega a la pantalla principal.
            navController.navigate(Routes.Main.route) {
                // Limpia la pila de navegación para que el usuario no pueda volver a esta pantalla.
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        }
    }

    // Box actúa como un contenedor para superponer elementos.
    Box(modifier = Modifier.fillMaxSize()) {
        // AndroidView para mostrar la WebView.
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    // Se pone el fondo transparente para evitar el destello de la página de error.
                    setBackgroundColor(Color.TRANSPARENT)
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            url?.let {
                                if (it.startsWith("spotify-api-app://callback")) {
                                    val code = it.substringAfter("code=").substringBefore("&")
                                    loginViewModel.handleAuthCode(code)
                                }
                            }
                        }
                    }
                    loadUrl(loginViewModel.getAuthUrl())
                }
            }
        )

        // Se muestra un indicador de carga en el centro de la pantalla si el estado es `Loading`.
        if (authState is AuthState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
