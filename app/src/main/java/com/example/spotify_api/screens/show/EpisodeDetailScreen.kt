package com.example.spotify_api.screens.show

import android.graphics.drawable.BitmapDrawable
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
    var dominantColor by remember { mutableStateOf(Color.DarkGray) }
    val context = LocalContext.current

    LaunchedEffect(imageUrl) {
        if (imageUrl == null) {
            dominantColor = Color.DarkGray
            return@LaunchedEffect
        }
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .target { result ->
                val bitmap = (result as BitmapDrawable).bitmap
                Palette.from(bitmap).generate { palette ->
                    val color = palette?.vibrantSwatch?.rgb ?: palette?.dominantSwatch?.rgb ?: 0
                    dominantColor = Color(color)
                }
            }
            .build()
        imageLoader.enqueue(request)
    }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(dominantColor.copy(alpha = 0.5f), Color.Black)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Episodio", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize().background(gradientBrush).padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Publicado: ${releaseDate ?: "Fecha desconocida"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Botón de reproducción
                if (uri != null) {
                    Button(
                        onClick = { playbackManager.play(uri) },
                        shape = CircleShape,
                        modifier = Modifier.size(72.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = dominantColor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir Episodio", modifier = Modifier.size(48.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Usamos AndroidView para renderizar el HTML de la descripción
                HtmlTextView(text = description ?: "No hay descripción disponible.")
            }
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
