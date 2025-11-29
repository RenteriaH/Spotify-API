package com.example.spotify_api.repository

import com.example.spotify_api.data.SpotifyApi
import com.example.spotify_api.model.AlbumDetailsResponse
import com.example.spotify_api.model.AlbumMetadataResponse
import com.example.spotify_api.model.SearchResult
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class SpotifyRepository @Inject constructor(
    private val spotifyApi: SpotifyApi
) {

    suspend fun search(query: String): Response<SearchResult> {
        return spotifyApi.search(query = query)
    }

    suspend fun browseAll(): Response<ResponseBody> {
        return spotifyApi.browseAll()
    }

    suspend fun getAlbumDetails(ids: String): Response<AlbumDetailsResponse> {
        return spotifyApi.getAlbumDetails(ids = ids)
    }

    suspend fun getAlbumMetadata(id: String): Response<AlbumMetadataResponse> {
        return spotifyApi.getAlbumMetadata(id = id)
    }

}
