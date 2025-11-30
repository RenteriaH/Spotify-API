package com.example.spotify_api.screens.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotify_api.ui.PlaylistItem
import com.example.spotify_api.viewModel.CategoryPlaylistsState
import com.example.spotify_api.viewModel.CategoryPlaylistsViewModel

@Composable
fun CategoryPlaylistsScreen(
    navController: NavController, // Aunque no se usa ahora, es bueno tenerlo para futuras navegaciones.
    viewModel: CategoryPlaylistsViewModel = hiltViewModel()
) {
    val state by viewModel.playlistsState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when (val currentState = state) {
            is CategoryPlaylistsState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is CategoryPlaylistsState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(currentState.playlists) {
                        // Reutilizamos el Composable que ya creamos para la búsqueda.
                        PlaylistItem(playlist = it, onClick = { /* TODO: Navegar al detalle de la playlist si se implementa */ })
                    }
                }
            }
            is CategoryPlaylistsState.Error -> {
                Text(text = currentState.message, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
