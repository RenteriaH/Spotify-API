package com.example.spotify_api.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.spotify_api.model.Artist
import com.example.spotify_api.model.SavedAlbum
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.viewModel.ProfileState
import com.example.spotify_api.viewModel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController, 
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") }, 
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // --- ¡CAMBIO AQUÍ! ---
                // El padding del Scaffold se ignora para que el contenido se extienda,
                // pero el padding inferior se añade directamente a la LazyColumn.
        ) {
            when (val state = profileState) {
                is ProfileState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileState.Success -> {
                    val user = state.userProfile
                    val topArtists = state.topArtists
                    val savedAlbums = state.savedAlbums

                    // --- ¡CAMBIO AQUÍ! ---
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally, 
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp) // Espacio para la barra
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding())) // Espacio para la TopBar
                            AsyncImage(
                                model = user.images?.firstOrNull()?.url,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.size(150.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(user.displayName, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                            if (user.followers != null) {
                                Text("${user.followers.total} Seguidores", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Tus Artistas Favoritos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(topArtists) { artist ->
                            ArtistRow(
                                artist = artist,
                                onClick = { navController.navigate(Routes.ArtistDetail.createRoute(artist.id)) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Mis Álbumes",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        item {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2), 
                                modifier = Modifier.heightIn(max = 1000.dp), 
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(savedAlbums) { savedAlbum ->
                                    AlbumGridItem(album = savedAlbum.album, navController = navController)
                                }
                            }
                        }
                    }
                }
                is ProfileState.Error -> {
                    Text(text = state.message, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun ArtistRow(artist: Artist, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = artist.images?.firstOrNull()?.url,
            contentDescription = "Foto del artista",
            modifier = Modifier.size(56.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(artist.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Composable
fun AlbumGridItem(album: com.example.spotify_api.model.Album, navController: NavController) {
    Column(
        modifier = Modifier
            .clickable { navController.navigate(Routes.AlbumDetail.createRoute(album.id)) }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = album.images.firstOrNull()?.url,
            contentDescription = "Portada del álbum",
            modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = album.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = album.artists.firstOrNull()?.name ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
