package com.example.spotify_api.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.viewModel.NewReleasesViewModel
import com.example.spotify_api.viewModel.ReleasesState

@Composable
fun NewReleasesScreen(navController: NavController) {
    val viewModel: NewReleasesViewModel = hiltViewModel()
    val releasesState by viewModel.releasesState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        when (val state = releasesState) {
            is ReleasesState.Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            is ReleasesState.Success -> {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(state.albums) { album ->
                        // ¡CORREGIDO! Se especifica el nombre del parámetro (onAlbumClick) para resolver la ambigüedad.
                        AlbumGridItem(
                            album = album,
                            onAlbumClick = { albumId ->
                                navController.navigate(Routes.AlbumDetail.createRoute(albumId))
                            }
                        )
                    }
                }
            }
            is ReleasesState.Error -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = state.message)
                }
            }
        }
    }
}
