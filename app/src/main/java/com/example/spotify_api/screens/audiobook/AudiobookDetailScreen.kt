package com.example.spotify_api.screens.audiobook

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.spotify_api.model.Audiobook
import com.example.spotify_api.model.SimplifiedChapter
import com.example.spotify_api.playback.SpotifyPlaybackManager
import com.example.spotify_api.viewModel.AudiobookDetailViewModel
import com.example.spotify_api.viewModel.AudiobookState
import java.util.concurrent.TimeUnit

@Composable
fun AudiobookDetailScreen(
    playbackManager: SpotifyPlaybackManager,
    viewModel: AudiobookDetailViewModel = hiltViewModel()
) {
    val state by viewModel.audiobookState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is AudiobookState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is AudiobookState.Success -> {
                AudiobookDetailContent(
                    audiobook = currentState.audiobook,
                    playbackManager = playbackManager
                )
            }
            is AudiobookState.Error -> {
                Text(text = currentState.message, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun AudiobookDetailContent(
    audiobook: Audiobook,
    playbackManager: SpotifyPlaybackManager
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // --- Cabecera del Audiolibro ---
        item {
            AudiobookHeader(audiobook = audiobook)
        }

        // --- Separador y Lista de Capítulos ---
        item { HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) }

        itemsIndexed(audiobook.chapters.items) { index, chapter ->
            ChapterListItem(
                chapter = chapter,
                index = index + 1,
                onPlayClick = { playbackManager.play(chapter.uri) }
            )
        }
    }
}

@Composable
fun AudiobookHeader(audiobook: Audiobook) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = audiobook.images.firstOrNull()?.url,
            contentDescription = "Portada del audiolibro",
            modifier = Modifier.size(250.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(audiobook.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        // Se muestran los autores
        Text(
            text = "De: ${audiobook.authors.joinToString { it.name }}",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        // Se muestran los narradores
        Text(
            text = "Narrado por: ${audiobook.narrators.joinToString { it.name }}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ChapterListItem(
    chapter: SimplifiedChapter,
    index: Int,
    onPlayClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayClick() } // <-- ¡HACE EL CAPÍTULO CLICKEABLE!
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$index",
            modifier = Modifier.width(32.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chapter.name,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = chapter.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = formatDuration(chapter.durationMs),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        IconButton(onClick = onPlayClick) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir capítulo", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

private fun formatDuration(ms: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%d:%02d", minutes, seconds)
}
