package com.example.spotify_api.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.spotify_api.model.Playlist
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.viewModel.LibraryState
import com.example.spotify_api.viewModel.LibraryViewModel

@Composable
fun LibraryScreen(
    navController: NavController, 
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val state by viewModel.libraryState.collectAsState()

    when (val currentState = state) {
        is LibraryState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is LibraryState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message)
            }
        }
        is LibraryState.Success -> {
            // --- ¡CAMBIO AQUÍ! ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 80.dp) // Espacio para la barra
            ) {
                item {
                    Text(
                        text = "Mis Playlists",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                items(currentState.playlists) { playlist ->
                    PlaylistItemRow(playlist = playlist) {
                        navController.navigate(Routes.PlaylistDetail.createRoute(playlist.id))
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistItemRow(playlist: Playlist, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = playlist.images?.firstOrNull()?.url
        AsyncImage(
            model = imageUrl,
            contentDescription = "Portada de la playlist",
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = playlist.name, fontWeight = FontWeight.SemiBold)
            Text(text = "${playlist.tracks?.total ?: 0} canciones", fontSize = 12.sp)
        }
    }
}
