package com.example.spotify_api.screens.show

import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify_api.playback.SpotifyPlaybackManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailScreen(
    navController: NavController,
    name: String?,
    description: String?,
    releaseDate: String?,
    imageUrl: String?,
    uri: String?,
    playbackManager: SpotifyPlaybackManager
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Episodio", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Volver")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Portada del episodio",
                modifier = Modifier
                    .size(250.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = name ?: "Sin título",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Publicado: ${releaseDate ?: "Fecha desconocida"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de reproducción
            if (uri != null) {
                Button(
                    onClick = { playbackManager.play(uri) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir Episodio")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reproducir Episodio")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Usamos AndroidView para renderizar el HTML de la descripción
            HtmlTextView(text = description ?: "No hay descripción disponible.")
        }
    }
}

@Composable
fun HtmlTextView(text: String) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setTextColor(android.graphics.Color.WHITE) // Color del texto
            }
        },
        update = { view ->
            view.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }
    )
}
