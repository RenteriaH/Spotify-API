package com.example.spotify_api.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Artist
import com.example.spotify_api.model.SimplifiedAlbum
import com.example.spotify_api.model.Track
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- Estado de la Interfaz ---
sealed class ArtistDetailState {
    object Loading : ArtistDetailState()
    data class Success(
        val artist: Artist,
        val topTracks: List<Track>,
        val albums: List<SimplifiedAlbum>
    ) : ArtistDetailState()
    data class Error(val message: String) : ArtistDetailState()
}

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val artistId: String = savedStateHandle.get<String>("artistId")!!

    private val _artistDetailState = MutableStateFlow<ArtistDetailState>(ArtistDetailState.Loading)
    val artistDetailState = _artistDetailState.asStateFlow()

    init {
        loadArtistDetails()
    }

    private fun loadArtistDetails() {
        viewModelScope.launch {
            _artistDetailState.value = ArtistDetailState.Loading
            try {
                // Peticiones principales (si fallan, la pantalla muestra un error).
                val artistDeferred = async { repository.getArtist(artistId) }
                val topTracksDeferred = async { repository.getArtistTopTracks(artistId) }
                val albumsDeferred = async { repository.getArtistAlbums(artistId) }

                // Esperamos a que todas las peticiones terminen.
                val artist = artistDeferred.await()
                val topTracks = topTracksDeferred.await().tracks
                val albums = albumsDeferred.await().items

                _artistDetailState.value = ArtistDetailState.Success(artist, topTracks, albums)

            } catch (e: Exception) {
                // Esto atrapa los errores de las peticiones principales
                Log.e("ArtistDetailViewModel", "Fallo al cargar detalles principales del artista", e)
                _artistDetailState.value = ArtistDetailState.Error("No se pudieron cargar los detalles del artista.")
            }
        }
    }
}
