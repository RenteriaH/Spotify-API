package com.example.spotify_api.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.api.SpotifyApiService
import com.example.spotify_api.data.SpotifyAuthManager
import com.example.spotify_api.model.Album
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de nuevos lanzamientos (NewReleasesScreen).
 * Se encarga de obtener y gestionar la lista de nuevos álbumes de Spotify.
 */
@HiltViewModel
class NewReleasesViewModel @Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {

    // StateFlow para el estado de la carga de nuevos lanzamientos.
    private val _releasesState = MutableStateFlow<ReleasesState>(ReleasesState.Loading)
    val releasesState: StateFlow<ReleasesState> = _releasesState

    // El bloque init se ejecuta cuando el ViewModel se crea por primera vez.
    init {
        fetchNewReleases()
    }

    /**
     * Inicia una corrutina para obtener los nuevos lanzamientos desde la API de Spotify.
     */
    private fun fetchNewReleases() {
        viewModelScope.launch {
            _releasesState.value = ReleasesState.Loading
            try {
                val response = repository.getNewReleases()
                _releasesState.value = response.albums?.items?.let { albums ->
                    ReleasesState.Success(albums)
                } ?: ReleasesState.Error("No new releases found")
            } catch (e: Exception) {
                e.printStackTrace()
                _releasesState.value = ReleasesState.Error("Failed to fetch new releases")
            }
        }
    }
}

/**
 * Representa los diferentes estados de la pantalla de nuevos lanzamientos.
 */
sealed class ReleasesState {
    object Loading : ReleasesState()
    data class Success(val albums: List<Album>) : ReleasesState()
    data class Error(val message: String) : ReleasesState()
}
