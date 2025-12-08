package com.example.spotify_api.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spotify_api.model.AuthState
import com.example.spotify_api.playback.SpotifyPlaybackManager
import com.example.spotify_api.screens.AlbumDetailScreen
import com.example.spotify_api.screens.LibraryScreen
import com.example.spotify_api.screens.ProfileScreen
import com.example.spotify_api.screens.artist.ArtistDetailScreen
import com.example.spotify_api.screens.audiobook.AudiobookDetailScreen
import com.example.spotify_api.screens.category.CategoryPlaylistsScreen
import com.example.spotify_api.screens.playlist.PlaylistDetailScreen
import com.example.spotify_api.screens.show.ShowDetailScreen
import com.example.spotify_api.screens.showimport.EpisodeDetailScreen
import com.example.spotify_api.screens.track.TrackDetailScreen
import com.example.spotify_api.ui.HomeScreen
import com.example.spotify_api.ui.LoginScreen
import com.example.spotify_api.ui.SearchScreen
import com.example.spotify_api.viewModel.LoginViewModel

// --- ¡¡¡TODA LA LÓGICA DE NAVEGACIÓN ESTÁ AHORA AQUÍ!!! ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavManager(playbackManager: SpotifyPlaybackManager) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val authState by loginViewModel.authState.collectAsState()
    val navController: NavHostController = rememberNavController()

    // Determina la pantalla de inicio y si se debe mostrar la barra de navegación
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val startDestination = if (authState is AuthState.Authenticated) BottomBarScreen.Home.route else Routes.Login.route

    val screensWithBottomBar = listOf(
        BottomBarScreen.Home.route,
        BottomBarScreen.Search.route,
        BottomBarScreen.Library.route,
        BottomBarScreen.Profile.route,
        Routes.ArtistDetail.route // <-- ¡¡¡AÑADIDO!!!
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in screensWithBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        // --- ¡¡¡UN ÚNICO NAVHOST PARA TODA LA APP!!! ---
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Ruta de Login
            composable(Routes.Login.route) {
                LoginScreen(navController = navController)
            }

            // Rutas de la Barra de Navegación
            composable(BottomBarScreen.Home.route) { HomeScreen(navController = navController) }
            composable(BottomBarScreen.Search.route) { SearchScreen(navController = navController) }
            composable(BottomBarScreen.Library.route) { LibraryScreen(navController = navController) }
            composable(BottomBarScreen.Profile.route) { ProfileScreen(navController = navController) }

            // Rutas de Detalle
            composable(
                route = Routes.AlbumDetail.route,
                arguments = listOf(navArgument("albumId") { type = NavType.StringType })
            ) { AlbumDetailScreen(navController = navController, playbackManager = playbackManager) }

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
            ) { PlaylistDetailScreen(navController = navController, playbackManager = playbackManager) }

            composable(
                route = Routes.AudiobookDetail.route,
                arguments = listOf(navArgument("audiobookId") { type = NavType.StringType })
            ) { AudiobookDetailScreen(playbackManager = playbackManager) }

            composable(
                route = Routes.CategoryPlaylists.route,
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
            ) { CategoryPlaylistsScreen(navController = navController) }

            composable(
                route = Routes.TrackDetail.route,
                arguments = listOf(navArgument("trackId") { type = NavType.StringType })
            ) { TrackDetailScreen(navController = navController, playbackManager = playbackManager) }

            composable(
                route = Routes.ShowDetail.route,
                arguments = listOf(navArgument("showId") { type = NavType.StringType })
            ) { ShowDetailScreen(navController = navController, playbackManager = playbackManager) }

            composable(
                route = Routes.EpisodeDetail.route,
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("desc") { type = NavType.StringType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("imageUrl") { type = NavType.StringType },
                    navArgument("uri") { type = NavType.StringType }, // Argumento para la URI del episodio
                    navArgument("durationMs") { type = NavType.IntType } // Argumento para la duración
                )
            ) { backStackEntry ->
                EpisodeDetailScreen(
                    navController = navController,
                    name = backStackEntry.arguments?.getString("name")?.decodeUrl(),
                    description = backStackEntry.arguments?.getString("desc")?.decodeUrl(),
                    releaseDate = backStackEntry.arguments?.getString("date")?.decodeUrl(),
                    imageUrl = backStackEntry.arguments?.getString("imageUrl")?.decodeUrl(),
                    uri = backStackEntry.arguments?.getString("uri")?.decodeUrl(), // Se pasa la URI
                    durationMs = backStackEntry.arguments?.getInt("durationMs"), // Se pasa la duración
                    playbackManager = playbackManager // Se pasa el gestor de reproducción
                )
            }
        }
    }
}

// --- La BottomBar ahora vive aquí, junto al resto de la lógica de navegación ---
@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.Library,
        BottomBarScreen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = Color.Black.copy(alpha = 0.8f)
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
