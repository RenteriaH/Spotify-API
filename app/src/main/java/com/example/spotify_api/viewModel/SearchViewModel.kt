package com.example.spotify_api.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.*
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SearchResult {
    data class TrackResult(val tracks: List<Track>) : SearchResult()
    data class AlbumResult(val albums: List<Album>) : SearchResult()
    data class PlaylistResult(val playlists: List<Playlist>) : SearchResult()
    data class ArtistResult(val artists: List<Artist>) : SearchResult()
    data class AudiobookResult(val audiobooks: List<SimplifiedAudiobook>) : SearchResult()
}

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    data class ShowingCategories(val categories: List<Category>) : SearchScreenState()
    data class ShowSearchResults(val result: SearchResult, val query: String) : SearchScreenState()
    data class Error(val message: String) : SearchScreenState()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _searchText = mutableStateOf("")
    val searchText: State<String> = _searchText

    private val _screenState = mutableStateOf<SearchScreenState>(SearchScreenState.Loading)
    val screenState: State<SearchScreenState> = _screenState

    private val _selectedCategory = mutableStateOf("track")
    val selectedCategory: State<String> = _selectedCategory

    private var searchJob: Job? = null

    init {
        val initialQuery = savedStateHandle.get<String>("query")
        if (!initialQuery.isNullOrBlank()) {
            onSearchTextChanged(initialQuery)
        } else {
            fetchCategories()
        }
    }

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
        searchJob?.cancel()

        if (newText.isBlank()) {
            _selectedCategory.value = "track"
            fetchCategories()
        } else {
            performSearch(category = _selectedCategory.value)
        }
    }

    fun onCategorySelected(category: String) {
        // ¡CORREGIDO! Si se pulsa la misma categoría, se vuelve a "track" (deselección).
        val newCategory = if (_selectedCategory.value == category) "track" else category
        _selectedCategory.value = newCategory
        performSearch(category = newCategory)
    }

    private fun fetchCategories() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _screenState.value = SearchScreenState.Loading
            try {
                val response = repository.getCategories(limit = 50)
                _screenState.value = SearchScreenState.ShowingCategories(response.categories.items)
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = SearchScreenState.Error("No se pudieron cargar las categorías")
            }
        }
    }

    private fun performSearch(category: String) {
        val query = _searchText.value
        if (query.isBlank()) {
            fetchCategories()
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (_screenState.value !is SearchScreenState.ShowSearchResults) {
                _screenState.value = SearchScreenState.Loading
            }

            try {
                val response = repository.search(query, type = category)
                val searchResult: SearchResult = when (category) {
                    "track" -> SearchResult.TrackResult(response.tracks?.items?.filterNotNull() ?: emptyList())
                    "album" -> SearchResult.AlbumResult(response.albums?.items?.filterNotNull() ?: emptyList())
                    "playlist" -> SearchResult.PlaylistResult(response.playlists?.items?.filterNotNull() ?: emptyList())
                    "artist" -> SearchResult.ArtistResult(response.artists?.items?.filterNotNull() ?: emptyList())
                    "audiobook" -> SearchResult.AudiobookResult(response.audiobooks?.items?.filterNotNull() ?: emptyList())
                    else -> throw IllegalArgumentException("Categoría no soportada: $category")
                }
                _screenState.value = SearchScreenState.ShowSearchResults(searchResult, query)
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = SearchScreenState.Error("Error al realizar la búsqueda")
            }
        }
    }
}
