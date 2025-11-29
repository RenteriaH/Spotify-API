package com.example.spotify_api.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.spotify_api.model.TrackItem
import com.example.spotify_api.viewModel.SpotifyViewModel
import java.util.concurrent.TimeUnit

@Composable
fun AlbumDetailScreen(
    albumId: String?,
    viewModel: SpotifyViewModel = hiltViewModel()
) {

    val albumDetails by viewModel.albumDetailsResult.collectAsState()
    val albumDuration by viewModel.albumDuration.collectAsState()

    LaunchedEffect(albumId) {
        if (albumId != null) {
            viewModel.getAlbumDetails(albumId)
            viewModel.getAlbumMetadata(albumId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (albumDetails == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 50.dp))
        } else {
            val album = albumDetails!!

            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = album.images.firstOrNull()?.url,
                contentDescription = "Album Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Album Title and Duration ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = album.name, 
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold, 
                    maxLines = 1, 
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                albumDuration?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Text(
                text = "${album.artists.firstOrNull()?.name ?: "Unknown Artist"} • ${album.releaseDate.substringBefore('-')}", 
                fontSize = 16.sp, 
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            LazyColumn {
                items(album.tracks.items) { track ->
                    TrackRow(track = track)
                }
            }
        }
    }
}

@Composable
fun TrackRow(track: TrackItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = track.trackNumber.toString(),
            modifier = Modifier.width(24.dp),
            color = Color.Gray
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(text = track.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                text = track.artists.joinToString(separator = ", ") { it.name },
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(text = formatDuration(track.durationMs), fontSize = 12.sp, color = Color.Gray)
    }
}

private fun formatDuration(ms: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%d:%02d", minutes, seconds)
}

