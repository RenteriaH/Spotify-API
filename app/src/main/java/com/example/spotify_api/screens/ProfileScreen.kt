package com.example.spotify_api.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.example.spotify_api.model.Artist
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.viewModel.ProfileState
import com.example.spotify_api.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController, // Se añade el NavController
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when (val state = profileState) {
            is ProfileState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ProfileState.Success -> {
                val user = state.userProfile
                val topArtists = state.topArtists

                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    item {
                        AsyncImage(
                            model = user.images.firstOrNull()?.url,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.size(150.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(user.displayName, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Text("${user.followers.total} Seguidores", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                            // ¡ACCIÓN! Se navega al hacer clic.
                            onClick = { navController.navigate(Routes.ArtistDetail.createRoute(artist.id)) }
                        )
                    }
                }
            }
            is ProfileState.Error -> {
                Text(text = state.message, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun ArtistRow(artist: Artist, onClick: () -> Unit) { // Se añade el callback onClick
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Se hace la fila clickeable
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
