package com.example.spotify_api.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Playlist
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LibraryState {
    object Loading : LibraryState()
    data class Success(val playlists: List<Playlist>) : LibraryState()
    data class Error(val message: String) : LibraryState()
}

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {

    private val _libraryState = MutableStateFlow<LibraryState>(LibraryState.Loading)
    val libraryState = _libraryState.asStateFlow()

    init {
        loadMyPlaylists()
    }

    private fun loadMyPlaylists() {
        viewModelScope.launch {
            _libraryState.value = LibraryState.Loading
            try {
                val playlists = repository.getMyPlaylists().items
                _libraryState.value = LibraryState.Success(playlists)
            } catch (e: Exception) {
                e.printStackTrace()
                _libraryState.value = LibraryState.Error("No se pudieron cargar tus playlists.")
            }
        }
    }
}
