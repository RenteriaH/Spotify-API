package com.example.spotify_api.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Artist
import com.example.spotify_api.model.UserProfile
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado para la pantalla de perfil
sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val userProfile: UserProfile, val topArtists: List<Artist>) : ProfileState()
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
                // Se lanzan ambas peticiones en paralelo para más eficiencia
                val userProfile = repository.getUserProfile()
                val topArtistsResponse = repository.getTopArtists(limit = 10) // Pedimos solo los 10 principales

                _profileState.value = ProfileState.Success(
                    userProfile = userProfile,
                    topArtists = topArtistsResponse.items
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _profileState.value = ProfileState.Error("No se pudo cargar el perfil.")
            }
        }
    }
}
