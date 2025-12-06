package com.example.spotify_api.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Track
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TrackDetailState {
    object Loading : TrackDetailState()
    data class Success(val track: Track) : TrackDetailState()
    data class Error(val message: String) : TrackDetailState()
}

@HiltViewModel
class TrackDetailViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _trackState = MutableStateFlow<TrackDetailState>(TrackDetailState.Loading)
    val trackState: StateFlow<TrackDetailState> = _trackState

    init {
        val trackId = savedStateHandle.get<String>("trackId")
        if (trackId != null) {
            fetchTrackDetails(trackId)
        }
    }

    private fun fetchTrackDetails(trackId: String) {
        viewModelScope.launch {
            _trackState.value = TrackDetailState.Loading
            try {
                val track = repository.getTrack(trackId)
                _trackState.value = TrackDetailState.Success(track)
            } catch (e: Exception) {
                _trackState.value = TrackDetailState.Error("No se pudieron cargar los detalles de la canción.")
            }
        }
    }
}
