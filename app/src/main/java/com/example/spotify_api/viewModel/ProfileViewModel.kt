package com.example.spotify_api.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Artist
import com.example.spotify_api.model.SavedAlbum
import com.example.spotify_api.model.UserProfile
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- ¡CAMBIO AQUÍ! ---
sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val userProfile: UserProfile, val topArtists: List<Artist>, val savedAlbums: List<SavedAlbum>) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState = _profileState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                // --- ¡CAMBIO AQUÍ! ---
                // Se lanzan las tres peticiones en paralelo para más eficiencia
                val userProfileDeferred = async { repository.getUserProfile() }
                val topArtistsDeferred = async { repository.getTopArtists(limit = 10) }
                val savedAlbumsDeferred = async { repository.getMySavedAlbums(limit = 20) }

                _profileState.value = ProfileState.Success(
                    userProfile = userProfileDeferred.await(),
                    topArtists = topArtistsDeferred.await().items,
                    savedAlbums = savedAlbumsDeferred.await().items
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _profileState.value = ProfileState.Error("No se pudo cargar el perfil.")
            }
        }
    }
}
