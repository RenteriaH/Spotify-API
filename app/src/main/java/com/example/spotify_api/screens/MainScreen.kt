package com.example.spotify_api.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify_api.model.AlbumItem
import com.example.spotify_api.viewModel.SpotifyViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier, 
    viewModel: SpotifyViewModel = hiltViewModel(),
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("bad bunny") }
    val searchResult by viewModel.searchResult.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Spotify Search", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Enter artist or album") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- Search Buttons ---
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.performSearch(searchQuery) }) {
                Text("Album")
            }
            Button(onClick = { /* TODO: Implement playlist search if a working API is found */ }, enabled = false) {
                Text("Playlists")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Results Section ---
        searchResult?.albums?.let { albumResult ->
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp), 
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(albumResult.items) { albumItem ->
                    AlbumRow(album = albumItem, onAlbumClick = {
                        val albumId = albumItem.data.uri.substringAfter("spotify:album:")
                        navController.navigate("AlbumDetail/$albumId")
                    })
                }
            }
        }
    }
}

@Composable
fun AlbumRow(album: AlbumItem, onAlbumClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAlbumClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.data.coverArt.sources.firstOrNull()?.url,
                contentDescription = "Album Cover Art",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 10.dp).weight(1f)) {
                Text(text = album.data.name, fontWeight = FontWeight.Bold)
                Text(text = "${album.data.artists.items.firstOrNull()?.profile?.name ?: "Unknown Artist"} - ${album.data.date.year}", fontSize = 12.sp)
            }
        }
    }
}
