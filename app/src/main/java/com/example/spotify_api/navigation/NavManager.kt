package com.example.spotify_api.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spotify_api.screens.AlbumDetailScreen
import com.example.spotify_api.screens.MainScreen

@Composable
fun NavManager() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "MainScreen") {
        composable("MainScreen") {
            MainScreen(navController = navController)
        }

        composable(
            route = "AlbumDetail/{albumId}", 
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) {
            backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId")
            // This screen doesn't need to pass the NavController down anymore
            AlbumDetailScreen(albumId = albumId)
        }
    }
}
