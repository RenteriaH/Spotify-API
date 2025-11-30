package com.example.spotify_api.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotify_api.model.Album
import com.example.spotify_api.model.SimplifiedAlbum

/**
 * Componente para mostrar un álbum en una cuadrícula. 
 * Acepta un [SimplifiedAlbum], ideal para listas donde no se necesita toda la info.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumGridItem(album: SimplifiedAlbum, onAlbumClick: (String) -> Unit) {
    Card(
        onClick = { onAlbumClick(album.id) }, 
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = album.images.firstOrNull()?.url,
                contentDescription = "Album cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = album.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            Text(
                text = album.artists.joinToString { it.name },
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

/**
 * ¡SOBRECARGA AÑADIDA! 
 * Esta es una segunda versión del mismo componente que acepta un [Album] completo.
 * Esto nos da flexibilidad para usar el componente en diferentes pantallas sin errores.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumGridItem(album: Album, onAlbumClick: (String) -> Unit) {
    Card(
        onClick = { onAlbumClick(album.id) },
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = album.images.firstOrNull()?.url,
                contentDescription = "Album cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = album.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            Text(
                text = album.artists.joinToString { it.name },
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}
