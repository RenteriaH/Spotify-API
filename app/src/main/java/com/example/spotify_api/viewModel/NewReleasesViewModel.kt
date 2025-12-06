package com.example.spotify_api.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.*
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.Calendar
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

            supervisorScope {
                val userProfileDeferred = async { repository.getUserProfile() }
                val newReleasesDeferred = async { repository.getNewReleases() }
                val topArtistsDeferred = async { repository.getTopArtists() }
                val topTracksDeferred = async { repository.getMyTopTracks() }
                val myPlaylistsDeferred = async { repository.getMyPlaylists(limit = 8) }
                val recentlyPlayedDeferred = async { repository.getRecentlyPlayed(limit = 10) }

                val userProfile = try {
                    userProfileDeferred.await()
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching user profile", e)
                    null
                }

                val albums = try {
                    newReleasesDeferred.await().albums.items
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching new releases", e)
                    emptyList<Album>()
                }

                val artists = try {
                    topArtistsDeferred.await().items
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching top artists", e)
                    emptyList<Artist>()
                }

                val tracks = try {
                    topTracksDeferred.await().items
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching top tracks", e)
                    emptyList<Track>()
                }

                val playlists = try {
                    myPlaylistsDeferred.await().items
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching my playlists", e)
                    emptyList<Playlist>()
                }

                val recentlyPlayed = try {
                    recentlyPlayedDeferred.await().items
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error fetching recently played", e)
                    emptyList<PlayHistoryObject>()
                }

                _homeState.value = HomeState.Success(userProfile, playlists, albums, artists, tracks, recentlyPlayed, getGreeting())
            }
        }
    }

    private fun getGreeting(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 6..11 -> "Buenos días"
            in 12..19 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }
}

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val userProfile: UserProfile?,
        val myPlaylists: List<Playlist>,
        val newReleases: List<Album>,
        val topArtists: List<Artist>,
        val topTracks: List<Track>,
        val recentlyPlayed: List<PlayHistoryObject>,
        val greeting: String
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}
