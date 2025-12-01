package com.example.spotify_api.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spotify_api.screens.AlbumDetailScreen
import com.example.spotify_api.screens.ProfileScreen
import com.example.spotify_api.screens.artist.ArtistDetailScreen
import com.example.spotify_api.screens.audiobook.AudiobookDetailScreen
import com.example.spotify_api.screens.category.CategoryPlaylistsScreen
import com.example.spotify_api.screens.playlist.PlaylistDetailScreen
import com.example.spotify_api.ui.HomeScreen
import com.example.spotify_api.ui.LoginScreen
import com.example.spotify_api.ui.MainScreen
import com.example.spotify_api.ui.SearchScreen

@Composable
fun NavManager() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Login.route) {
        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Routes.Main.route) {
            MainScreen()
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = BottomBarScreen.Home.route, modifier = modifier) {

        // --- Pantallas de la Barra de Navegación ---
        composable(BottomBarScreen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Routes.Search.route,
            arguments = listOf(navArgument("query") { type = NavType.StringType; nullable = true })
        ) {
            SearchScreen(navController = navController)
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        // --- Pantallas de Detalle ---
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
            route = Routes.PlaylistDetail.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.StringType })
        ) {
            PlaylistDetailScreen()
        }

        composable(
            route = Routes.AudiobookDetail.route,
            arguments = listOf(navArgument("audiobookId") { type = NavType.StringType })
        ) {
            AudiobookDetailScreen()
        }

        composable(
            route = Routes.CategoryPlaylists.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) {
            CategoryPlaylistsScreen(navController = navController)
        }
    }
}
