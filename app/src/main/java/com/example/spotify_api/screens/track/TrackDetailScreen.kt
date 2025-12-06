package com.example.spotify_api.screens.track

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify_api.model.Track
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.viewModel.TrackDetailState
import com.example.spotify_api.viewModel.TrackDetailViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailScreen(
    navController: NavController,
    viewModel: TrackDetailViewModel = hiltViewModel()
) {
    val state by viewModel.trackState.collectAsState()

    val dominantColor = Color.DarkGray

    when (val currentState = state) {
        is TrackDetailState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is TrackDetailState.Success -> {
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(dominantColor.copy(alpha = 0.5f), Color.Black)
            )
            // --- ¡CAMBIO AQUÍ! ---
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
                    TrackDetailContent(track = currentState.track, navController = navController)
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
fun TrackDetailContent(track: Track, navController: NavController) {
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
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = track.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Artistas (clicables)
        Row {
            track.artists.forEachIndexed { index, artist ->
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    modifier = Modifier.clickable { // Navega al perfil del artista
                        navController.navigate(Routes.ArtistDetail.createRoute(artist.id))
                    }
                )
                if (index < track.artists.lastIndex) {
                    Text(text = ", ", style = MaterialTheme.typography.bodyLarge, color = Color.LightGray)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Álbum (clicable) y Duración
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.clickable { // Navega al detalle del álbum
                navController.navigate(Routes.AlbumDetail.createRoute(track.album.id))
            }) {
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

// Función para formatear la duración de milisegundos a "m:ss"
private fun formatDuration(ms: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%d:%02d", minutes, seconds)
}
