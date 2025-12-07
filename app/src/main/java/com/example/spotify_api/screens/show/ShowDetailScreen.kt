package com.example.spotify_api.screens.show

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
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
import com.example.spotify_api.model.Show
import com.example.spotify_api.model.SimplifiedEpisode
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.utils.formatDurationWithHours
import com.example.spotify_api.viewModel.ShowDetailState
import com.example.spotify_api.viewModel.ShowDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailScreen(
    navController: NavController,
    viewModel: ShowDetailViewModel = hiltViewModel()
) {
    val state by viewModel.showDetailState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (val currentState = state) {
                is ShowDetailState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ShowDetailState.Success -> {
                    ShowDetailContent(show = currentState.show, navController = navController)
                }
                is ShowDetailState.Error -> {
                    Text(text = currentState.message, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun ShowDetailContent(show: Show, navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
        item {
            ShowHeader(show)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                // --- ¡TEXTO CORREGIDO! ---
                text = show.description,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Episodios",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(show.episodes.items.filterNotNull()) { episode ->
            EpisodeListItem(episode = episode) {
                navController.navigate(
                    Routes.EpisodeDetail.createRoute(
                        name = episode.name,
                        // --- CORRECCIÓN AQUÍ TAMBIÉN POR SI ACASO ---
                        description = episode.description,
                        releaseDate = episode.releaseDate,
                        imageUrl = episode.images.firstOrNull()?.url ?: ""
                    )
                )
            }
        }
    }
}

@Composable
fun ShowHeader(show: Show) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = show.images.firstOrNull()?.url,
            contentDescription = "Portada del Show",
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(show.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(show.publisher, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun EpisodeListItem(episode: SimplifiedEpisode, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = episode.images.firstOrNull()?.url,
            contentDescription = "Portada del episodio",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = episode.name, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(
                text = episode.releaseDate,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        // --- ¡DURACIÓN AÑADIDA! ---
        Text(
            text = formatDurationWithHours(episode.durationMs),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
