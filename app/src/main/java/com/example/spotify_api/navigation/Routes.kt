package com.example.spotify_api.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Routes(val route: String) {
    object Login : Routes("Login")
    object Main : Routes("Main")
    object NewReleases : Routes("NewReleases")
    object Profile : Routes("Profile")

    object Search : Routes("Search?query={query}") {
        fun createRoute(query: String? = null): String {
            return if (query != null) {
                val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
                "Search?query=$encodedQuery"
            } else {
                "Search?query="
            }
        }
    }

    object AlbumDetail : Routes("AlbumDetail/{albumId}") {
        fun createRoute(albumId: String) = "AlbumDetail/$albumId"
    }

    object ArtistDetail : Routes("ArtistDetail/{artistId}") {
        fun createRoute(artistId: String) = "ArtistDetail/$artistId"
    }

    object CategoryPlaylists : Routes("CategoryPlaylists/{categoryId}") {
        fun createRoute(categoryId: String) = "CategoryPlaylists/$categoryId"
    }

    object PlaylistDetail : Routes("PlaylistDetail/{playlistId}") {
        fun createRoute(playlistId: String) = "PlaylistDetail/$playlistId"
    }

    // ¡AÑADIDO! La nueva ruta para el detalle de un audiolibro.
    object AudiobookDetail : Routes("AudiobookDetail/{audiobookId}") {
        fun createRoute(audiobookId: String) = "AudiobookDetail/$audiobookId"
    }
}
