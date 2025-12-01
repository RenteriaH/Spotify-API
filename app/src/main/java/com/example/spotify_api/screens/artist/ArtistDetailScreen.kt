package com.example.spotify_api.screens.artist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify_api.model.Artist
import com.example.spotify_api.model.SimplifiedAlbum
import com.example.spotify_api.model.Track
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.ui.AlbumGridItem
import com.example.spotify_api.utils.formatNumberWithCommas
import com.example.spotify_api.viewModel.ArtistDetailState
import com.example.spotify_api.viewModel.ArtistDetailViewModel

@Composable
fun ArtistDetailScreen(
    navController: NavController, // Se añade el NavController para la navegación.
    onAlbumClick: (String) -> Unit,
    viewModel: ArtistDetailViewModel = hiltViewModel()
) {
    val state by viewModel.artistDetailState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is ArtistDetailState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ArtistDetailState.Success -> {
                ArtistDetailContent(
                    navController = navController,
                    artist = currentState.artist,
                    topTracks = currentState.topTracks,
                    albums = currentState.albums,
                    onAlbumClick = onAlbumClick
                )
            }
            is ArtistDetailState.Error -> {
                Text(text = currentState.message, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun ArtistDetailContent(
    navController: NavController,
    artist: Artist,
    topTracks: List<Track>,
    albums: List<SimplifiedAlbum>,
    onAlbumClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            ArtistHeader(navController = navController, artist = artist)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text("Populares", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }
        itemsIndexed(topTracks) { index, track ->
            TopTrackItem(track = track, trackNumber = index + 1)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Discografía", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(600.dp), // Altura fija para que el LazyColumn pueda scrollar
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(albums) { album ->
                    // ¡CORREGIDO! Se le pasa el SimplifiedAlbum directamente.
                    AlbumGridItem(album = album, onAlbumClick = onAlbumClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArtistHeader(navController: NavController, artist: Artist) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = artist.images?.firstOrNull()?.url,
            contentDescription = "Foto del artista",
            modifier = Modifier.size(180.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(artist.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        // --- ¡CAMBIO AQUÍ! ---
        Text("${formatNumberWithCommas(artist.followers?.total)} seguidores", style = MaterialTheme.typography.bodyLarge)
        // --- FIN DEL CAMBIO ---
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            artist.genres?.forEach { genre ->
                SuggestionChip(
                    onClick = { navController.navigate(Routes.Search.createRoute(genre)) },
                    label = { Text(genre.replaceFirstChar { it.uppercase() }) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TopTrackItem(track: Track, trackNumber: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$trackNumber", modifier = Modifier.width(24.dp), style = MaterialTheme.typography.bodyLarge)
        AsyncImage(
            model = track.album.images.firstOrNull()?.url,
            contentDescription = "Portada de la canción",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(track.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(track.album.name, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
