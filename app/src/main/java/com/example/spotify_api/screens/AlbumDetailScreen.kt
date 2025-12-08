package com.example.spotify_api.screens

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
import com.example.spotify_api.model.Album
import com.example.spotify_api.model.SimplifiedTrack
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.playback.SpotifyPlaybackManager
import com.example.spotify_api.viewModel.AlbumDetailViewModel
import com.example.spotify_api.viewModel.AlbumState
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    navController: NavController,
    playbackManager: SpotifyPlaybackManager,
    viewModel: AlbumDetailViewModel = hiltViewModel()
) {
    val albumState by viewModel.albumState.collectAsState()
    var dominantColor by remember { mutableStateOf(Color.Black) }
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    when (val state = albumState) {
        is AlbumState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AlbumState.Success -> {
            val imageUrl = state.album.images.firstOrNull()?.url

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
                        title = {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Buscar en el álbum...", color = Color.LightGray, fontSize = 14.sp) },
                                modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 16.dp),
                                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                                singleLine = true,
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.White) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                                    cursorColor = Color.White,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        },
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
                Box(modifier = Modifier.fillMaxSize().background(gradientBrush).padding(paddingValues)) {
                    AlbumDetailContent(
                        album = state.album,
                        navController = navController,
                        searchQuery = searchQuery,
                        playbackManager = playbackManager
                    )
                }
            }
        }
        is AlbumState.Error -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = Color.White)
            }
        }
    }
}

@Composable
fun AlbumDetailContent(
    album: Album,
    navController: NavController,
    searchQuery: String,
    playbackManager: SpotifyPlaybackManager
) {
    val filteredTracks = remember(searchQuery, album.tracks) {
        if (searchQuery.isBlank()) {
            album.tracks?.items ?: emptyList()
        } else {
            album.tracks?.items?.filter { track ->
                track.name.contains(searchQuery, ignoreCase = true)
            } ?: emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        if (searchQuery.isBlank()) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { AlbumHeader(album = album, navController = navController) }
            item { HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = Color.White.copy(alpha = 0.1f)) }
        }

        itemsIndexed(filteredTracks) { index, track ->
            TrackRow(
                track = track,
                index = index + 1,
                navController = navController,
                onPlayClick = { playbackManager.play(track.uri) }
            )
        }
    }
}

@Composable
fun AlbumHeader(album: Album, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = album.images.firstOrNull()?.url,
            contentDescription = "Portada del álbum",
            modifier = Modifier.size(250.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(album.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)

        Row(verticalAlignment = Alignment.CenterVertically) {
            val artist = album.artists.firstOrNull()
            if (artist != null) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.clickable { navController.navigate(Routes.ArtistDetail.createRoute(artist.id)) }
                )
                Text(
                    text = " • ${album.releaseDate.substringBefore('-')}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
            } else {
                Text(
                    text = "Artista Desconocido • ${album.releaseDate.substringBefore('-')}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun TrackRow(
    track: SimplifiedTrack,
    index: Int,
    navController: NavController,
    onPlayClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                navController.navigate(Routes.TrackDetail.createRoute(track.id))
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$index", modifier = Modifier.width(32.dp), color = Color.LightGray)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = track.artists.joinToString(separator = ", ") { it.name },
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

        IconButton(onClick = onPlayClick) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir canción", tint = Color.White)
        }
    }
}

private fun formatDuration(ms: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%d:%02d", minutes, seconds)
}
