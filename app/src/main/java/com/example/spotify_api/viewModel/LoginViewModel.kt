package com.example.spotify_api.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.data.SpotifyAuthManager
import com.example.spotify_api.model.AuthState // <-- ¡¡¡IMPORT AÑADIDO Y CLASE INTERNA ELIMINADA!!!
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: SpotifyAuthManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun getAuthUrl(): String {
        return authManager.getAuthUrl()
    }

    fun handleAuthCode(code: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val success = authManager.exchangeCodeForToken(code)
            if (success) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error("Failed to get access token")
            }
        }
    }
}
