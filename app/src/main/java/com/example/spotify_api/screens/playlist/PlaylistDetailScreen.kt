package com.example.spotify_api.screens.playlist

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify_api.model.Playlist
import com.example.spotify_api.model.Track
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.utils.formatNumberWithCommas
import com.example.spotify_api.viewModel.PlaylistDetailState
import com.example.spotify_api.viewModel.PlaylistDetailViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    navController: NavController, 
    viewModel: PlaylistDetailViewModel = hiltViewModel()
) {
    val state by viewModel.playlistState.collectAsState()

    var dominantColor by remember { mutableStateOf(Color.Black) }
    val context = LocalContext.current

    when (val currentState = state) {
        is PlaylistDetailState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PlaylistDetailState.Success -> {
            val imageUrl = currentState.playlist.images?.firstOrNull()?.url

            LaunchedEffect(imageUrl) {
                if (imageUrl == null) {
                    dominantColor = Color.Black
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
                containerColor = Color.Black
            ) { paddingValues ->
                // --- ¡CAMBIO AQUÍ! ---
                // El padding del Scaffold se ignora para el fondo, pero se aplica a la LazyColumn.
                Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
                    PlaylistDetailContent(playlist = currentState.playlist, navController = navController, topPadding = paddingValues.calculateTopPadding())
                }
            }
        }
        is PlaylistDetailState.Error -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = Color.White)
            }
        }
    }
}

@Composable
fun PlaylistDetailContent(playlist: Playlist, navController: NavController, topPadding: androidx.compose.ui.unit.Dp) {
    // --- ¡CAMBIO AQUÍ! ---
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = topPadding, bottom = 80.dp) // Espacio para TopBar y BottomBar
    ) {
        item {
            PlaylistHeader(playlist = playlist)
        }
        item { HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = Color.White.copy(alpha = 0.1f)) }
        itemsIndexed(playlist.tracks?.items ?: emptyList()) { index, playlistTrackItem ->
            playlistTrackItem.track?.let {
                TrackListItem(track = it, index = index + 1, navController = navController)
            }
        }
    }
}

@Composable
fun PlaylistHeader(playlist: Playlist) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = playlist.images?.firstOrNull()?.url,
            contentDescription = "Portada de la playlist",
            modifier = Modifier.size(250.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(playlist.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
        playlist.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.LightGray
            )
        }
        playlist.owner.displayName?.let {
            Text(
                text = "De $it",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${formatNumberWithCommas(playlist.followers.total)} seguidores",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray
        )
    }
}

@Composable
fun TrackListItem(track: Track, index: Int, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                if (track.id != null) { 
                    navController.navigate(Routes.TrackDetail.createRoute(track.id))
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$index", modifier = Modifier.width(32.dp), color = Color.LightGray)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.name,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
            Text(
                text = track.artists.joinToString { it.name },
                fontSize = 14.sp,
                color = Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = formatDuration(track.durationMs),
            fontSize = 14.sp,
            color = Color.LightGray
        )
    }
}

private fun formatDuration(ms: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%d:%02d", minutes, seconds)
}
