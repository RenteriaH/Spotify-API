package com.example.spotify_api.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify_api.model.Album
import com.example.spotify_api.model.Playlist
import com.example.spotify_api.model.Track
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.viewModel.HomeState
import com.example.spotify_api.viewModel.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.homeState.collectAsState()

    when (val currentState = state) {
        is HomeState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is HomeState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message)
            }
        }
        is HomeState.Success -> {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
                item {
                    CarouselSection(
                        title = "Tus Canciones Favoritas",
                        items = currentState.topTracks,
                        itemContent = { track ->
                            TrackCarouselItem(track = track) { /* TODO: Navegar al reproductor */ }
                        }
                    )
                }
                item {
                    CarouselSection(
                        title = "Nuevos Lanzamientos",
                        items = currentState.newReleases,
                        itemContent = { album ->
                            AlbumCarouselItem(album = album) {
                                navController.navigate(Routes.AlbumDetail.createRoute(album.id))
                            }
                        }
                    )
                }
                item {
                    CarouselSection(
                        title = "Playlists Destacadas",
                        items = currentState.featuredPlaylists,
                        itemContent = { playlist ->
                            PlaylistCarouselItem(playlist = playlist) {
                                navController.navigate(Routes.PlaylistDetail.createRoute(playlist.id))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun <T> CarouselSection(title: String, items: List<T>, itemContent: @Composable (T) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                itemContent(item)
            }
        }
    }
}

@Composable
fun AlbumCarouselItem(album: Album, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(140.dp).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = album.images.firstOrNull()?.url,
            contentDescription = "Portada del álbum",
            modifier = Modifier.size(140.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = album.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text(text = album.artists.joinToString { it.name }, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
fun PlaylistCarouselItem(playlist: Playlist, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(140.dp).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = playlist.images.firstOrNull()?.url,
            contentDescription = "Portada de la playlist",
            modifier = Modifier.size(140.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = playlist.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

@Composable
fun TrackCarouselItem(track: Track, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(140.dp).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = track.album.images.firstOrNull()?.url,
            contentDescription = "Portada de la canción",
            modifier = Modifier.size(140.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = track.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text(text = track.artists.joinToString { it.name }, fontSize = 12.sp, maxLines = 1)
    }
}
