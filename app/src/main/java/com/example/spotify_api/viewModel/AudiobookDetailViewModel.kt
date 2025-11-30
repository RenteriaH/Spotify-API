package com.example.spotify_api.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Audiobook
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Define los posibles estados de la pantalla de detalle de un audiolibro.
 */
sealed interface AudiobookState {
    object Loading : AudiobookState
    data class Success(val audiobook: Audiobook) : AudiobookState
    data class Error(val message: String) : AudiobookState
}

@HiltViewModel
class AudiobookDetailViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _audiobookState = MutableStateFlow<AudiobookState>(AudiobookState.Loading)
    val audiobookState = _audiobookState.asStateFlow()

    init {
        // Se obtiene el ID del audiolibro de los argumentos de navegación.
        val audiobookId = savedStateHandle.get<String>("audiobookId")
        if (audiobookId != null) {
            getAudiobookDetails(audiobookId)
        } else {
            _audiobookState.value = AudiobookState.Error("No se encontró el ID del audiolibro.")
        }
    }

    /**
     * Obtiene los detalles completos de un audiolibro desde el repositorio.
     */
    private fun getAudiobookDetails(id: String) {
        viewModelScope.launch {
            _audiobookState.value = AudiobookState.Loading
            try {
                // Llama al repositorio para obtener los datos del audiolibro.
                val audiobook = repository.getAudiobook(id)
                _audiobookState.value = AudiobookState.Success(audiobook)
            } catch (e: Exception) {
                e.printStackTrace()
                _audiobookState.value = AudiobookState.Error("Error al cargar los detalles del audiolibro.")
            }
        }
    }
}
