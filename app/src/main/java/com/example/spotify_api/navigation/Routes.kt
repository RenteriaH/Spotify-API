package com.example.spotify_api.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
    object Search : BottomBarScreen("search", "Search", Icons.Default.Search)
    object Library : BottomBarScreen("library", "Library", Icons.Default.VideoLibrary)
    object Profile : BottomBarScreen("profile", "Profile", Icons.Default.Person)
}

// Helper para codificar y decodificar URLs
fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
fun String.decodeUrl(): String = URLDecoder.decode(this, StandardCharsets.UTF_8.toString())

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Main : Routes("main")

    object Home : Routes(BottomBarScreen.Home.route)
    object Search : Routes("search?query={query}") {
        fun createRoute(query: String? = null): String {
            return if (query != null) {
                "search?query=${query.encodeUrl()}"
            } else {
                "search?query="
            }
        }
    }
    object Library : Routes(BottomBarScreen.Library.route)
    object Profile : Routes(BottomBarScreen.Profile.route)

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
    object TrackDetail : Routes("track_detail/{trackId}") {
        fun createRoute(trackId: String) = "track_detail/$trackId"
    }
    object ShowDetail : Routes("show_detail/{showId}") {
        fun createRoute(showId: String) = "show_detail/$showId"
    }

    // --- ¡NUEVA RUTA PARA DETALLE DE EPISODIO! ---
    object EpisodeDetail : Routes("episode_detail?name={name}&desc={desc}&date={date}&imageUrl={imageUrl}&uri={uri}") {
        fun createRoute(name: String, description: String, releaseDate: String, imageUrl: String, uri: String): String {
            // Codificamos cada parámetro para que el paso sea seguro
            val encodedName = name.encodeUrl()
            val encodedDesc = description.encodeUrl()
            val encodedDate = releaseDate.encodeUrl()
            val encodedImageUrl = imageUrl.encodeUrl()
            val encodedUri = uri.encodeUrl()
            return "episode_detail?name=$encodedName&desc=$encodedDesc&date=$encodedDate&imageUrl=$encodedImageUrl&uri=$encodedUri"
        }
    }
}
