package com.example.spotify_api.ui

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

    Column(modifier = Modifier.fillMaxSize()) { 
        Spacer(modifier = Modifier.height(16.dp)) 
        TextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChanged,
            label = { Text("Búsqueda.") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            trailingIcon = {
                if (searchText.isNotBlank()) {
                    IconButton(onClick = { viewModel.onSearchTextChanged("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (searchText.isNotBlank()) {
            // Lista de botones que el usuario puede seleccionar. 'track' no está aquí.
            val userSelectableCategories = listOf("artist", "album", "playlist", "audiobook")

            // --- ¡LÓGICA DE BOTONES CORREGIDA! ---
            val categoriesToShow = if (selectedCategory in userSelectableCategories) {
                // Si el usuario ha seleccionado una categoría, muestra solo esa.
                listOf(selectedCategory!!)
            } else {
                // Si no (búsqueda por defecto de tracks o ninguna), muestra todas.
                userSelectableCategories
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                // Centra el botón si solo hay uno.
                horizontalArrangement = if (categoriesToShow.size == 1) Arrangement.Center else Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(categoriesToShow) { category ->
                    CategoryButton(
                        label = category.replaceFirstChar { it.titlecase() },
                        isSelected = selectedCategory == category,
                        onClick = { viewModel.onCategorySelected(category) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            when (val state = screenState) {
                is SearchScreenState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                is SearchScreenState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message) }
                is SearchScreenState.ShowingCategories -> {
                    Column {
                        Text("Explorar todo", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(state.categories) { category ->
                                CategoryCard(category) {
                                    navController.navigate(Routes.CategoryPlaylists.createRoute(category.id))
                                }
                            }
                        }
                    }
                }
                is SearchScreenState.ShowSearchResults -> {
                    SearchResultsContent(navController, state.result)
                }
            }
        }
    }
}

@Composable
fun SearchResultsContent(navController: NavController, result: SearchResult) {
    val bottomPadding = PaddingValues(bottom = 80.dp)
    when (result) {
        is SearchResult.TrackResult -> {
            LazyColumn(Modifier.fillMaxSize(), contentPadding = bottomPadding) { 
                items(result.tracks) { track -> 
                    TrackItem(track) { 
                        if (track.id != null) {
                            navController.navigate(Routes.TrackDetail.createRoute(track.id))
                        }
                    }
                }
            }
        }
        is SearchResult.AlbumResult -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(4.dp), contentPadding = bottomPadding) {
                items(result.albums) {
                        album -> AlbumGridItem(album = album) { albumId -> navController.navigate(Routes.AlbumDetail.createRoute(albumId)) }
                }
            }
        }
        is SearchResult.PlaylistResult -> {
            LazyColumn(Modifier.fillMaxSize(), contentPadding = bottomPadding) {
                items(result.playlists.filterNotNull()) { playlist ->
                    PlaylistItem(playlist) { navController.navigate(Routes.PlaylistDetail.createRoute(playlist.id)) }
                }
            }
        }
        is SearchResult.ArtistResult -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(4.dp), contentPadding = bottomPadding) {
                items(result.artists) { artist ->
                    ArtistGridItem(artist) { artistId -> navController.navigate(Routes.ArtistDetail.createRoute(artistId)) }
                }
            }
        }
        is SearchResult.AudiobookResult -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(4.dp), contentPadding = bottomPadding) {
                items(result.audiobooks) { audiobook ->
                    AudiobookGridItem(audiobook) { audiobookId -> navController.navigate(Routes.AudiobookDetail.createRoute(audiobookId)) }
                }
            }
        }
    }
}


@Composable
fun CategoryButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    // --- ¡COLOR CORREGIDO A ROJO! ---
    val buttonColors = if (isSelected) {
        ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935), contentColor = Color.White)
    } else {
        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)
    }

    Button(onClick = onClick, colors = buttonColors, shape = RoundedCornerShape(20.dp)) {
        Text(text = label)
        if (isSelected) {
            Spacer(Modifier.width(4.dp))
            Icon(imageVector = Icons.Default.Close, contentDescription = "Deseleccionar", modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun ArtistGridItem(artist: Artist, onArtistClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(4.dp).clickable { onArtistClick(artist.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = artist.images?.firstOrNull()?.url, contentDescription = "Foto del artista",
            modifier = Modifier.size(128.dp).clip(CircleShape), contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = artist.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun AudiobookGridItem(audiobook: SimplifiedAudiobook, onAudiobookClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(4.dp).clickable { onAudiobookClick(audiobook.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = audiobook.images.firstOrNull()?.url, contentDescription = "Portada del audiolibro",
            modifier = Modifier.size(128.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = audiobook.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = "Audiolibro", fontSize = 12.sp, color = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    Card(modifier = Modifier.clickable(onClick = onClick).padding(4.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Box(modifier = Modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
            AsyncImage(
                model = category.icons.firstOrNull()?.url, contentDescription = null,
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
            )
            Text(
                text = category.name, fontWeight = FontWeight.Bold, color = Color.White,
                modifier = Modifier.padding(8.dp).align(Alignment.BottomStart)
            )
        }
    }
}

@Composable
fun TrackItem(track: Track, onClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = track.album.images.firstOrNull()?.url, contentDescription = "Portada del álbum", modifier = Modifier.size(56.dp).clip(RoundedCornerShape(4.dp)), contentScale = ContentScale.Crop)
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
        AsyncImage(model = playlist.images?.firstOrNull()?.url, contentDescription = "Portada de la playlist", modifier = Modifier.size(56.dp).clip(RoundedCornerShape(4.dp)), contentScale = ContentScale.Crop)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = playlist.name, fontWeight = FontWeight.Bold, maxLines = 1)
            val ownerName = playlist.owner.displayName ?: "Playlist"
            Text(text = ownerName, fontSize = 14.sp, color = Color.Gray, maxLines = 1)
        }
    }
}
