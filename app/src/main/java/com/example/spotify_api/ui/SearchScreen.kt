package com.example.spotify_api.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.spotify_api.model.*
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.viewModel.SearchScreenState
import com.example.spotify_api.viewModel.SearchResult
import com.example.spotify_api.viewModel.SearchViewModel

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    val searchText by viewModel.searchText
    val screenState by viewModel.screenState
    val selectedCategory by viewModel.selectedCategory

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChanged,
            label = { Text("Busca canciones, artistas o pódcasts") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        if (searchText.isNotBlank()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                // Lógica simplificada para los botones de categoría
                val categories = listOf("artist", "album", "playlist")
                val selected = selectedCategory

                if (selected != "track") {
                    CategoryButtonWithClear(label = "Artistas", isSelected = selected == "artist", onClick = { viewModel.onCategorySelected("artist") })
                    Spacer(modifier = Modifier.width(8.dp))
                    CategoryButtonWithClear(label = "Álbumes", isSelected = selected == "album", onClick = { viewModel.onCategorySelected("album") })
                    Spacer(modifier = Modifier.width(8.dp))
                    CategoryButtonWithClear(label = "Playlists", isSelected = selected == "playlist", onClick = { viewModel.onCategorySelected("playlist") })
                } else {
                    CategoryButtonWithClear(label = "Artistas", isSelected = false, onClick = { viewModel.onCategorySelected("artist") })
                    Spacer(modifier = Modifier.width(8.dp))
                    CategoryButtonWithClear(label = "Álbumes", isSelected = false, onClick = { viewModel.onCategorySelected("album") })
                    Spacer(modifier = Modifier.width(8.dp))
                    CategoryButtonWithClear(label = "Playlists", isSelected = false, onClick = { viewModel.onCategorySelected("playlist") })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        when (val state = screenState) {
            is SearchScreenState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is SearchScreenState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(state.message) }

            is SearchScreenState.ShowingCategories -> {
                Text("Explorar todo", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
                    items(state.categories) { category ->
                        CategoryCard(category) {
                            navController.navigate(Routes.CategoryPlaylists.createRoute(category.id))
                        }
                    }
                }
            }

            is SearchScreenState.ShowSearchResults -> {
                when (val result = state.result) {
                    is SearchResult.TrackResult -> {
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(result.tracks) { track ->
                                TrackItem(track)
                            }
                        }
                    }

                    is SearchResult.AlbumResult -> {
                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                            items(result.albums) { album ->
                                AlbumGridItem(album) { albumId ->
                                    navController.navigate(Routes.AlbumDetail.createRoute(albumId))
                                }
                            }
                        }
                    }

                    is SearchResult.PlaylistResult -> {
                        LazyColumn(Modifier.fillMaxSize()) {
                            val validPlaylists = result.playlists.filterNotNull()
                            items(validPlaylists) { playlist ->
                                PlaylistItem(playlist) {
                                    navController.navigate(Routes.PlaylistDetail.createRoute(playlist.id))
                                }
                            }
                        }
                    }

                    is SearchResult.ArtistResult -> {
                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                            items(result.artists) { artist ->
                                ArtistGridItem(artist) { artistId ->
                                    navController.navigate(Routes.ArtistDetail.createRoute(artistId))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTES DE LA INTERFAZ ---

@Composable
fun CategoryButtonWithClear(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(text = label)
            if (isSelected) Spacer(Modifier.width(16.dp))
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Deseleccionar",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp).size(18.dp).clickable(onClick = onClick)
            )
        }
    }
}

@Composable
fun ArtistGridItem(artist: Artist, onArtistClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp).clickable { onArtistClick(artist.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = artist.images?.firstOrNull()?.url,
            contentDescription = "Foto del artista",
            modifier = Modifier.size(128.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = artist.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun AlbumGridItem(album: Album, onAlbumClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp).clickable { onAlbumClick(album.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = album.images.firstOrNull()?.url,
            contentDescription = "Portada del álbum",
            modifier = Modifier.size(128.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = album.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun AlbumGridItem(album: SimplifiedAlbum, onAlbumClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp).clickable { onAlbumClick(album.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = album.images.firstOrNull()?.url,
            contentDescription = "Portada del álbum",
            modifier = Modifier.size(128.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = album.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) { // Se añade el callback onClick
    Card(modifier = Modifier.padding(8.dp).clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = category.icons.firstOrNull()?.url, contentDescription = "Icono de la categoría", modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = category.name, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
fun TrackItem(track: Track, onClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = track.album.images.firstOrNull()?.url, contentDescription = "Portada del álbum", modifier = Modifier.size(56.dp), contentScale = ContentScale.Crop)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = track.name, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = track.artists.joinToString { it.name }, fontSize = 14.sp, color = Color.Gray, maxLines = 1)
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, onClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = playlist.images.firstOrNull()?.url, contentDescription = "Portada de la playlist", modifier = Modifier.size(56.dp), contentScale = ContentScale.Crop)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = playlist.name, fontWeight = FontWeight.Bold, maxLines = 1)
            val ownerName = playlist.owner?.displayName ?: "Playlist"
            Text(text = ownerName, fontSize = 14.sp, color = Color.Gray, maxLines = 1)
        }
    }
}
