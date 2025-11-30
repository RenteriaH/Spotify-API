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

sealed class PlaylistDetailState {
    object Loading : PlaylistDetailState()
    data class Success(val playlist: Playlist) : PlaylistDetailState()
    data class Error(val message: String) : PlaylistDetailState()
}

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playlistId: String = savedStateHandle.get<String>("playlistId")!!

    private val _playlistState = MutableStateFlow<PlaylistDetailState>(PlaylistDetailState.Loading)
    val playlistState = _playlistState.asStateFlow()

    init {
        loadPlaylistDetails()
    }

    private fun loadPlaylistDetails() {
        viewModelScope.launch {
            _playlistState.value = PlaylistDetailState.Loading
            try {
                val playlist = repository.getPlaylist(playlistId)
                _playlistState.value = PlaylistDetailState.Success(playlist)
            } catch (e: Exception) {
                e.printStackTrace()
                _playlistState.value = PlaylistDetailState.Error("No se pudo cargar la playlist.")
            }
        }
    }
}
