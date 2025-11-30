package com.example.spotify_api.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Playlist
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log // Añade esta importación

// --- Estado de la Interfaz ---
sealed class CategoryPlaylistsState {
    object Loading : CategoryPlaylistsState()
    data class Success(val playlists: List<Playlist>) : CategoryPlaylistsState()
    data class Error(val message: String) : CategoryPlaylistsState()
}

@HiltViewModel
class CategoryPlaylistsViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // --- LÍNEA QUE FALTA ---
    private val _state = MutableStateFlow<CategoryPlaylistsState>(CategoryPlaylistsState.Loading)
    val state = _state.asStateFlow()

    // Se obtiene el ID de la categoría de los argumentos de navegación.
    private val categoryId: String = savedStateHandle.get<String>("categoryId")!!

    private val _playlistsState = MutableStateFlow<CategoryPlaylistsState>(CategoryPlaylistsState.Loading)
    val playlistsState = _playlistsState.asStateFlow()

    init {
        val categoryId = savedStateHandle.get<String>("categoryId") ?: ""
        if (categoryId.isNotBlank()) {
            // ¡ESTA ES LA CORRECCIÓN CLAVE!
            // No usamos getPlaylistsForCategory, usamos la búsqueda.
            fetchPlaylistsByGenre(categoryId)
        } else {
            _state.value = CategoryPlaylistsState.Error("No se especificó un género.")
        }
    }
    private fun fetchPlaylistsByGenre(genre: String) {
        viewModelScope.launch {
            _state.value = CategoryPlaylistsState.Loading
            // Build the special query to search by genre
            val searchQuery = "genre:\"$genre\""

            try {
                // Call the search function which returns the response directly
                val searchResponse = repository.search(query = searchQuery, type = "playlist")

                // The search response is nested inside a "playlists" object.
                // We access searchResponse.playlists.items to get the list.
                val playlists = searchResponse.playlists?.items?.filterNotNull() ?: emptyList()
                _state.value = CategoryPlaylistsState.Success(playlists)

            } catch (e: Exception) {
                // If repository.search() fails, it will throw an exception.
                Log.e("CategoryPlaylistsVM", "Failed to search playlists for genre: $genre", e)
                _state.value = CategoryPlaylistsState.Error("Could not load playlists for this genre.")
            }
        }
    }
    private fun loadPlaylists() {
        viewModelScope.launch {
            _playlistsState.value = CategoryPlaylistsState.Loading
            try {
                val response = repository.getCategoryPlaylists(categoryId)
                _playlistsState.value = CategoryPlaylistsState.Success(response.playlists.items)
            } catch (e: Exception) {
                e.printStackTrace()
                _playlistsState.value = CategoryPlaylistsState.Error("No se pudieron cargar las playlists de la categoría.")
            }
        }
    }
}
