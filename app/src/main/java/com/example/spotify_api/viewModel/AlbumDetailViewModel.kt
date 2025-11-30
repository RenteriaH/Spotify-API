package com.example.spotify_api.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.api.SpotifyApiService
import com.example.spotify_api.data.SpotifyAuthManager
import com.example.spotify_api.model.Album
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalles del álbum (AlbumDetailScreen).
 * Se encarga de obtener y gestionar los datos de un álbum específico.
 */
@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val spotifyApiService: SpotifyApiService, // Servicio para hacer llamadas a la API principal de Spotify.
    private val authManager: SpotifyAuthManager,     // Gestor para obtener el token de autenticación.
    savedStateHandle: SavedStateHandle              // Objeto para acceder a los argumentos pasados por navegación.
) : ViewModel() {

    // StateFlow privado para gestionar el estado de la carga del álbum.
    private val _albumState = MutableStateFlow<AlbumState>(AlbumState.Loading)
    // StateFlow público para que la UI observe los cambios de estado.
    val albumState: StateFlow<AlbumState> = _albumState

    // Se obtiene el ID del álbum del argumento de navegación. El "!!" asegura que no es nulo.
    private val albumId: String = savedStateHandle.get<String>("albumId")!!

    // El bloque init se ejecuta cuando se crea el ViewModel por primera vez.
    init {
        fetchAlbumDetails() // Se inicia la carga de los detalles del álbum.
    }

    /**
     * Inicia una corrutina para obtener los detalles del álbum desde la API de Spotify.
     */
    private fun fetchAlbumDetails() {
        viewModelScope.launch {
            // Se actualiza el estado a "Cargando".
            _albumState.value = AlbumState.Loading
            // Se obtiene el token de acceso formateado ("Bearer <token>").
            val token = authManager.getBearerToken()

            if (token != null) {
                try {
                    // Se realiza la llamada a la API para obtener el álbum.
                    val album = spotifyApiService.getAlbum(token, albumId)
                    // Si la llamada es exitosa, se actualiza el estado con los datos del álbum.
                    _albumState.value = AlbumState.Success(album)
                } catch (e: Exception) {
                    // Si ocurre un error en la llamada, se actualiza el estado a "Error".
                    e.printStackTrace()
                    _albumState.value = AlbumState.Error("Failed to fetch album details")
                }
            } else {
                // Si no hay token, no se puede hacer la llamada. Se actualiza el estado a "Error".
                _albumState.value = AlbumState.Error("Not authenticated")
            }
        }
    }
}

/**
 * Clase sellada que representa los diferentes estados de la pantalla de detalles del álbum.
 */
sealed class AlbumState {
    object Loading : AlbumState() // El contenido se está cargando.
    data class Success(val album: Album) : AlbumState() // Los datos se cargaron con éxito.
    data class Error(val message: String) : AlbumState() // Ocurrió un error al cargar los datos.
}
