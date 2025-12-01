package com.example.spotify_api.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Album
import com.example.spotify_api.model.Playlist
import com.example.spotify_api.model.Track
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState

    init {
        fetchHomeScreenData()
    }

    private fun fetchHomeScreenData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            // supervisorScope asegura que el fallo de una corrutina hija (un async) no cancele a las demás.
            supervisorScope {
                val newReleasesDeferred = async { repository.getNewReleases() }
                val featuredPlaylistsDeferred = async { repository.getFeaturedPlaylists() }
                val topTracksDeferred = async { repository.getMyTopTracks() }

                val albums = try {
                    newReleasesDeferred.await().albums.items
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching new releases", e)
                    emptyList<Album>()
                }

                val playlists = try {
                    featuredPlaylistsDeferred.await().playlists.items
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching featured playlists", e)
                    emptyList<Playlist>()
                }

                val tracks = try {
                    topTracksDeferred.await().items
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching top tracks", e)
                    emptyList<Track>()
                }

                // Emite el estado de éxito. La app ya no se cerrará y mostrará los datos que sí se hayan podido cargar.
                _homeState.value = HomeState.Success(albums, playlists, tracks)
            }
        }
    }
}

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val newReleases: List<Album>,
        val featuredPlaylists: List<Playlist>,
        val topTracks: List<Track>
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}
