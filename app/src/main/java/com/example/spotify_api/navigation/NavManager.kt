package com.example.spotify_api.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spotify_api.screens.AlbumDetailScreen
import com.example.spotify_api.screens.artist.ArtistDetailScreen
import com.example.spotify_api.screens.audiobook.AudiobookDetailScreen
import com.example.spotify_api.screens.category.CategoryPlaylistsScreen
import com.example.spotify_api.screens.playlist.PlaylistDetailScreen
import com.example.spotify_api.ui.LoginScreen
import com.example.spotify_api.ui.MainScreen

@Composable
fun NavManager() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Login.route) {
        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Routes.Main.route) {
            MainScreen(navController = navController)
        }

        composable(
            route = Routes.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) {
            AlbumDetailScreen(navController = navController)
        }

        composable(
            route = Routes.ArtistDetail.route,
            arguments = listOf(navArgument("artistId") { type = NavType.StringType })
        ) {
            ArtistDetailScreen(
                navController = navController,
                onAlbumClick = { albumId ->
                    navController.navigate(Routes.AlbumDetail.createRoute(albumId))
                }
            )
        }

        composable(
            route = Routes.CategoryPlaylists.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) {
            CategoryPlaylistsScreen(navController = navController)
        }

        composable(
            route = Routes.PlaylistDetail.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.StringType })
        ) {
            PlaylistDetailScreen()
        }

        // ¡AÑADIDO! La nueva pantalla de detalles del audiolibro.
        composable(
            route = Routes.AudiobookDetail.route,
            arguments = listOf(navArgument("audiobookId") { type = NavType.StringType })
        ) {
            AudiobookDetailScreen()
        }
    }
}
