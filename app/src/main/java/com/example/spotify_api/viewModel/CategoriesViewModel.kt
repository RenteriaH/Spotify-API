package com.example.spotify_api.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.Category
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de categorías de búsqueda.
 * Se encarga de obtener y gestionar la lista de categorías de Spotify.
 */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<CategoriesState>(CategoriesState.Loading)
    val categoriesState: StateFlow<CategoriesState> = _categoriesState

    init {
        fetchCategories()
    }

    /**
     * Inicia una corrutina para obtener las categorías desde la API de Spotify.
     */
    private fun fetchCategories() {
        viewModelScope.launch {
            _categoriesState.value = CategoriesState.Loading
            try {
                val response = repository.getCategories(limit = 50) // Pedimos las 50 categorías para tener variedad
                _categoriesState.value = response.categories.items.let { categories ->
                    CategoriesState.Success(categories)
                } ?: CategoriesState.Error("No categories found")
            } catch (e: Exception) {
                e.printStackTrace()
                _categoriesState.value = CategoriesState.Error("Failed to fetch categories")
            }
        }
    }
}

/**
 * Representa los diferentes estados de la pantalla de categorías.
 */
sealed class CategoriesState {
    object Loading : CategoriesState()
    data class Success(val categories: List<Category>) : CategoriesState()
    data class Error(val message: String) : CategoriesState()
}
