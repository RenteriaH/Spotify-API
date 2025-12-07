package com.example.spotify_api.screens.show

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify_api.model.Show
import com.example.spotify_api.model.SimplifiedEpisode
import com.example.spotify_api.navigation.Routes
import com.example.spotify_api.utils.formatDurationWithHours
import com.example.spotify_api.viewModel.ShowDetailState
import com.example.spotify_api.viewModel.ShowDetailViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailScreen(
    navController: NavController,
    viewModel: ShowDetailViewModel = hiltViewModel()
) {
    val state by viewModel.showDetailState.collectAsState()
    var dominantColor by remember { mutableStateOf(Color.Black) }
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    when (val currentState = state) {
        is ShowDetailState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ShowDetailState.Success -> {
            val imageUrl = currentState.show.images.firstOrNull()?.url

            LaunchedEffect(imageUrl) {
                if (imageUrl == null) {
                    dominantColor = Color.Black
                    return@LaunchedEffect
                }
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .target { result ->
                        val bitmap = (result as BitmapDrawable).bitmap
                        Palette.from(bitmap).generate { palette ->
                            val color = palette?.vibrantSwatch?.rgb ?: palette?.dominantSwatch?.rgb ?: 0
                            dominantColor = Color(color)
                        }
                    }
                    .build()
                imageLoader.enqueue(request)
            }

            val gradientBrush = Brush.verticalGradient(
                colors = listOf(dominantColor.copy(alpha = 0.5f), Color.Black)
            )

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Buscar en el podcast...", color = Color.LightGray, fontSize = 14.sp) },
                                modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 16.dp),
                                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                                singleLine = true,
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.White) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                                    cursorColor = Color.White,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Volver", tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                Box(modifier = Modifier.fillMaxSize().background(gradientBrush).padding(paddingValues)) {
                    ShowDetailContent(show = currentState.show, navController = navController, searchQuery = searchQuery)
                }
            }
        }
        is ShowDetailState.Error -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = Color.White)
            }
        }
    }
}

@Composable
fun ShowDetailContent(show: Show, navController: NavController, searchQuery: String) {
    val filteredEpisodes = remember(searchQuery, show.episodes) {
        if (searchQuery.isBlank()) {
            show.episodes.items
        } else {
            show.episodes.items.filter { episode ->
                episode.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
        if (searchQuery.isBlank()) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { ShowHeader(show) }
            item { HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = Color.White.copy(alpha = 0.1f)) }
            item { Text("Episodios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp), color = Color.White) }
        }

        items(filteredEpisodes) { episode ->
            EpisodeListItem(episode = episode) {
                 val encodedName = URLEncoder.encode(episode.name, "UTF-8")
                 val encodedDesc = URLEncoder.encode(episode.description, "UTF-8")
                 val encodedDate = URLEncoder.encode(episode.releaseDate, "UTF-8")
                 val encodedImageUrl = URLEncoder.encode(episode.images.firstOrNull()?.url ?: "", "UTF-8")

                navController.navigate(
                    Routes.EpisodeDetail.createRoute(
                        name = encodedName,
                        description = encodedDesc,
                        releaseDate = encodedDate,
                        imageUrl = encodedImageUrl
                    )
                )
            }
        }
    }
}

@Composable
fun ShowHeader(show: Show) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = show.images.firstOrNull()?.url,
            contentDescription = "Portada del Show",
            modifier = Modifier.size(180.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(show.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
        Text(show.publisher, style = MaterialTheme.typography.bodyLarge, color = Color.LightGray)
    }
}

@Composable
fun EpisodeListItem(episode: SimplifiedEpisode, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = episode.images.firstOrNull()?.url,
            contentDescription = "Portada del episodio",
            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = episode.name, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, color = Color.White)
            Text(
                text = episode.releaseDate,
                fontSize = 12.sp,
                color = Color.LightGray,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = formatDurationWithHours(episode.durationMs.toLong()),
            fontSize = 12.sp,
            color = Color.LightGray
        )
    }
}
