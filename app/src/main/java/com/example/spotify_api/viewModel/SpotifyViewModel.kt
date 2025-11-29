package com.example.spotify_api.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_api.model.AlbumFullDetails
import com.example.spotify_api.model.AlbumMetadata
import com.example.spotify_api.model.SearchResult
import com.example.spotify_api.repository.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SpotifyViewModel @Inject constructor(
    private val repository: SpotifyRepository
) : ViewModel() {

    private val _searchResult = MutableStateFlow<SearchResult?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _albumDetailsResult = MutableStateFlow<AlbumFullDetails?>(null)
    val albumDetailsResult = _albumDetailsResult.asStateFlow()

    private val _albumMetadata = MutableStateFlow<AlbumMetadata?>(null)

    // New state for the calculated duration string
    private val _albumDuration = MutableStateFlow<String?>(null)
    val albumDuration = _albumDuration.asStateFlow()


    fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.search(query)
                if (response.isSuccessful) {
                    _searchResult.value = response.body()
                } else {
                    _searchResult.value = null
                    Log.e("Spotify_JSON", "Search Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _searchResult.value = null
                Log.e("Spotify_JSON", "Search Exception: ${e.message}")
            }
        }
    }

    fun getAlbumDetails(ids: String) {
        // Reset states when fetching new details
        _albumDetailsResult.value = null
        _albumDuration.value = null
        _albumMetadata.value = null

        viewModelScope.launch {
            try {
                val response = repository.getAlbumDetails(ids)
                if (response.isSuccessful) {
                    _albumDetailsResult.value = response.body()?.albums?.firstOrNull()
                } else {
                    Log.e("Spotify_JSON", "Album Details Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Spotify_JSON", "Album Details Exception: ${e.message}")
            }
        }
    }

    fun getAlbumMetadata(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAlbumMetadata(id)
                if (response.isSuccessful) {
                    val metadata = response.body()?.data?.album
                    _albumMetadata.value = metadata
                    calculateAndSetAlbumDuration(metadata)
                } else {
                    Log.e("Spotify_JSON", "Album Metadata Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Spotify_JSON", "Album Metadata Exception: ${e.message}")
            }
        }
    }

    private fun calculateAndSetAlbumDuration(metadata: AlbumMetadata?) {
        if (metadata == null) return

        val totalMilliseconds = metadata.tracks.items.sumOf { it.track.duration.totalMilliseconds }
        _albumDuration.value = formatAlbumDuration(totalMilliseconds.toLong())
    }

    private fun formatAlbumDuration(ms: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(ms)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(hours)

        return if (hours > 0) {
            String.format("%d h %d min", hours, minutes)
        } else {
            String.format("%d min", minutes)
        }
    }
}
