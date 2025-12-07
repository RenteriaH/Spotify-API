package com.example.spotify_api.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify_api.model.Album
import com.example.spotify_api.model.Artist
import com.example.spotify_api.model.Playlist
import com.example.spotify_api.model.SimplifiedShow
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item { // --- SALUDO ---
                    Text(
                        text = currentState.greeting,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                item { // --- SECCIÓN DE PLAYLISTS ---
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.height(240.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(currentState.myPlaylists) { playlist ->
                            PlaylistGridItem(playlist = playlist) {
                                navController.navigate(Routes.PlaylistDetail.createRoute(playlist.id))
                            }
                        }
                    }
                }

                // Ocultamos la sección de recomendaciones por ahora
                // if (currentState.recommendations.isNotEmpty()) { ... }

                item { // --- SECCIÓN DE ESCUCHADO RECIENTEMENTE ---
                    CarouselSection(
                        title = "Escuchado Recientemente",
                        items = currentState.recentlyPlayed,
                        itemContent = { playHistory ->
                            TrackCarouselItem(track = playHistory.track) {
                                navController.navigate(Routes.TrackDetail.createRoute(playHistory.track.id))
                            }
                        }
                    )
                }

                item { // --- TUS CANCIONES FAVORITAS ---
                    CarouselSection(
                        title = "Tus Canciones Favoritas",
                        items = currentState.topTracks,
                        itemContent = { track ->
                            TrackCarouselItem(track = track) {
                                navController.navigate(Routes.TrackDetail.createRoute(track.id))
                            }
                        }
                    )
                }

                item { // --- NUEVOS LANZAMIENTOS ---
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

                // --- ¡SECCIÓN DE PODCASTS! ---
                if (currentState.savedShows.isNotEmpty()) {
                    item {
                        CarouselSection(
                            title = "Tus Podcasts",
                            items = currentState.savedShows,
                            itemContent = { show ->
                                ShowCarouselItem(show = show) {
                                    navController.navigate(Routes.ShowDetail.createRoute(show.id))
                                }
                            }
                        )
                    }
                }

                item { // --- TUS ARTISTAS FAVORITOS ---
                    CarouselSection(
                        title = "Tus Artistas Favoritos",
                        items = currentState.topArtists,
                        itemContent = { artist ->
                            ArtistCarouselItem(artist = artist) {
                                navController.navigate(Routes.ArtistDetail.createRoute(artist.id))
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
fun PlaylistGridItem(playlist: Playlist, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(Color.DarkGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = playlist.images?.firstOrNull()?.url,
            contentDescription = "Portada de la playlist",
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = playlist.name,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 8.dp)
        )
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
fun ShowCarouselItem(show: SimplifiedShow, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            // --- ¡CORRECCIÓN DE CALIDAD APLICADA SOLO AQUÍ! ---
            model = show.images.getOrNull(1)?.url ?: show.images.firstOrNull()?.url,
            contentDescription = "Portada del Show",
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = show.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = show.publisher, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun ArtistCarouselItem(artist: Artist, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(140.dp).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = artist.images?.firstOrNull()?.url,
            contentDescription = "Foto del artista",
            modifier = Modifier.size(140.dp).clip(CircleShape), // Foto circular
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = artist.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
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
