package com.example.spotify_api.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Define las rutas que SÍ aparecen en la barra de navegación inferior.
sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
    object Search : BottomBarScreen("search", "Search", Icons.Default.Search)
    object Profile : BottomBarScreen("profile", "Profile", Icons.Default.Person)
}

// Define TODAS las rutas de la aplicación.
sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Main : Routes("main") // Pantalla principal que contendrá la barra de navegación

    // Rutas de la barra inferior
    object Home : Routes(BottomBarScreen.Home.route)
    object Search : Routes("search?query={query}") {
        fun createRoute(query: String? = null): String {
            return if (query != null) {
                val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
                "search?query=$encodedQuery"
            } else {
                "search?query="
            }
        }
    }
    object Profile : Routes(BottomBarScreen.Profile.route)

    // Rutas de detalle (sin barra inferior)
    object AlbumDetail : Routes("album_detail/{albumId}") {
        fun createRoute(albumId: String) = "album_detail/$albumId"
    }
    object ArtistDetail : Routes("artist_detail/{artistId}") {
        fun createRoute(artistId: String) = "artist_detail/$artistId"
    }
    object CategoryPlaylists : Routes("category_playlists/{categoryId}") {
        fun createRoute(categoryId: String) = "category_playlists/$categoryId"
    }
    object PlaylistDetail : Routes("playlist_detail/{playlistId}") {
        fun createRoute(playlistId: String) = "playlist_detail/$playlistId"
    }
    object AudiobookDetail : Routes("audiobook_detail/{audiobookId}") {
        fun createRoute(audiobookId: String) = "audiobook_detail/$audiobookId"
    }
}
