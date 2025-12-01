package com.example.spotify_api.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.data.SpotifyAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de inicio de sesión (LoginScreen).
 * Gestiona el estado y la lógica del flujo de autenticación de Spotify.
 */
@HiltViewModel // Anotación para que Hilt pueda crear y proporcionar este ViewModel.
class LoginViewModel @Inject constructor(
    private val authManager: SpotifyAuthManager // Se inyecta el gestor de autenticación.
) : ViewModel() {

    // StateFlow privado para gestionar el estado de la autenticación internamente.
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    // StateFlow público y de solo lectura para que la UI observe los cambios de estado.
    val authState: StateFlow<AuthState> = _authState

    /**
     * Obtiene la URL de autorización de Spotify que se debe cargar en el WebView.
     */
    fun getAuthUrl(): String {
        return authManager.getAuthUrl()
    }

    /**
     * Maneja el código de autorización recibido después de que el usuario inicia sesión.
     * Inicia una corrutina para intercambiar el código por un token de acceso.
     * @param code El código de autorización extraído de la URL de redirección.
     */
    fun handleAuthCode(code: String) {
        // Se inicia una corrutina en el scope del ViewModel.
        viewModelScope.launch {
            // Se actualiza el estado a "Cargando" para que la UI pueda mostrar un indicador.
            _authState.value = AuthState.Loading
            // Se solicita el intercambio del código por un token.
            val success = authManager.exchangeCodeForToken(code)
            if (success) {
                // Si el intercambio es exitoso, el estado cambia a "Autenticado".
                _authState.value = AuthState.Authenticated
            } else {
                // Si falla, el estado cambia a "Error".
                _authState.value = AuthState.Error("Failed to get access token")
            }
        }
    }
}

/**
 * Clase sellada (sealed class) que representa los diferentes estados posibles del flujo de autenticación.
 * Esto permite a la UI reaccionar de forma segura y predecible a cada estado.
 */
sealed class AuthState {
    object Idle : AuthState()          // Estado inicial, no se ha hecho nada.
    object Loading : AuthState()      // Se está procesando la autenticación.
    object Authenticated : AuthState()  // El usuario se ha autenticado correctamente.
    data class Error(val message: String) : AuthState() // Ha ocurrido un error.
}
