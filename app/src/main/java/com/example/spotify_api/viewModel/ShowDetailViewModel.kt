package com.example.spotify_api.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Show
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ShowDetailState {
    object Loading : ShowDetailState()
    data class Success(val show: Show) : ShowDetailState()
    data class Error(val message: String) : ShowDetailState()
}

@HiltViewModel
class ShowDetailViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val showId: String = savedStateHandle.get<String>("showId")!!

    private val _showDetailState = MutableStateFlow<ShowDetailState>(ShowDetailState.Loading)
    val showDetailState = _showDetailState.asStateFlow()

    init {
        loadShowDetails()
    }

    private fun loadShowDetails() {
        viewModelScope.launch {
            _showDetailState.value = ShowDetailState.Loading
            try {
                val show = repository.getShow(showId)
                _showDetailState.value = ShowDetailState.Success(show)
            } catch (e: Exception) {
                e.printStackTrace()
                _showDetailState.value = ShowDetailState.Error("Failed to load show details.")
            }
        }
    }
}
