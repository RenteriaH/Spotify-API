package com.example.spotify_api.screens.showimport

import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify_api.playback.SpotifyPlaybackManager
import com.example.spotify_api.utils.formatDurationWithHours

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailScreen(
    navController: NavController,
    name: String?,
    description: String?,
    durationMs: Int?,
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
                title = { Text("", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, color = Color.White) },
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
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(gradientBrush).padding(paddingValues)) {
            EpisodeDetailContent(
                name = name,
                description = description,
                durationMs = durationMs,
                releaseDate = releaseDate,
                imageUrl = imageUrl,
                uri = uri,
                dominantColor = dominantColor,
                playbackManager = playbackManager
            )
        }
    }
}

@Composable
fun EpisodeDetailContent(
    name: String?,
    description: String?,
    durationMs: Int?,
    releaseDate: String?,
    imageUrl: String?,
    uri: String?,
    dominantColor: Color,
    playbackManager: SpotifyPlaybackManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Portada del episodio",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(dominantColor),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = name ?: "Sin título",
            style = MaterialTheme.typography.titleLarge, // Tamaño de título ajustado
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.85f)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- CONTENEDOR ÚNICO PARA TODA LA INFORMACIÓN ---
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Mismo ancho que la imagen
                .weight(1f) // Ocupa el espacio vertical disponible para permitir el scroll interno
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp)) // Borde único
                .padding(16.dp), // Padding interior general
            horizontalAlignment = Alignment.Start,
        ) {
            // --- Parte de la Fecha y Duración ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = "Publicado: ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = releaseDate ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
                Row {
                    Text(
                        text = "Duración: ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatDurationWithHours((durationMs ?: 0).toLong()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }

            // --- Separador visual dentro del cuadro ---
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.2f))

            // --- Parte de la Descripción (ahora dentro del mismo cuadro) ---
            // --- Parte de la Descripción (código corregido) ---
            Text(
                text = "Descripción del Episodio:", // Añadimos dos puntos para consistencia
                style = MaterialTheme.typography.bodyMedium, // <-- ¡CAMBIO 1! MISMO ESTILO QUE LOS OTROS
                color = Color.LightGray,                     // <-- ¡CAMBIO 2! MISMO COLOR QUE LOS OTROS TÍTULOS
                fontWeight = FontWeight.Bold                 // <-- ¡CAMBIO 3! MISMO GROSOR QUE LOS OTROS TÍTULOS
            )
            Spacer(modifier = Modifier.height(8.dp))

            // HtmlTextView ya tiene su propio scroll, perfecto para textos largos
            HtmlTextView(text = description ?: "No hay descripción disponible.")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Botón de reproducción movido al final ---
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
    }
}

@Composable
fun HtmlTextView(text: String) {
    val scrollState = rememberScrollState()
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setTextColor(android.graphics.Color.WHITE)
                // Opcional: Ajustar el tamaño del texto de la descripción
                textSize = 14f
            }
        },
        update = { view ->
            view.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        },
        modifier = Modifier.verticalScroll(scrollState)
    )
}
