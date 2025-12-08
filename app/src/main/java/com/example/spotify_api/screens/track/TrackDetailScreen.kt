package com.example.spotify_api.screens.track

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify_api.model.Track
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.playback.SpotifyPlaybackManager
import com.example.spotify_api.viewModel.TrackDetailState
import com.example.spotify_api.viewModel.TrackDetailViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailScreen(
    navController: NavController,
    viewModel: TrackDetailViewModel = hiltViewModel(),
    playbackManager: SpotifyPlaybackManager // <-- ¡GESTOR INYECTADO!
) {
    val state by viewModel.trackState.collectAsState()
    var dominantColor by remember { mutableStateOf(Color.DarkGray) }
    val context = LocalContext.current

    when (val currentState = state) {
        is TrackDetailState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is TrackDetailState.Success -> {
            val imageUrl = currentState.track.album.images.firstOrNull()?.url

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
                        title = { Text("") },
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
                    TrackDetailContent(
                        track = currentState.track,
                        navController = navController,
                        playbackManager = playbackManager,
                        dominantColor = dominantColor
                    )
                }
            }
        }
        is TrackDetailState.Error -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = Color.White)
            }
        }
    }
}

@Composable
fun TrackDetailContent(
    track: Track,
    navController: NavController,
    playbackManager: SpotifyPlaybackManager, // <-- ¡SE RECIBE EL GESTOR!
    dominantColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = track.album.images.firstOrNull()?.url,
            contentDescription = "Portada del álbum de la canción",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(dominantColor)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = track.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            track.artists.forEachIndexed { index, artist ->
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    modifier = Modifier.clickable { navController.navigate(Routes.ArtistDetail.createRoute(artist.id)) }
                )
                if (index < track.artists.lastIndex) {
                    Text(text = ", ", style = MaterialTheme.typography.bodyLarge, color = Color.LightGray)
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp)) // Aumentado para dar espacio al botón

        // --- ¡¡¡BOTÓN DE PLAY!!! ---
        Button(
            onClick = { playbackManager.play(track.uri) },
            shape = CircleShape,
            modifier = Modifier.size(72.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = dominantColor,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir", modifier = Modifier.size(48.dp))
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.clickable { navController.navigate(Routes.AlbumDetail.createRoute(track.album.id)) }) {
                Text(
                    text = "Álbum: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = track.album.name,
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
                    text = formatDuration(track.durationMs),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

private fun formatDuration(ms: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%d:%02d", minutes, seconds)
}
