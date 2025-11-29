package com.example.spotify_api.data

import com.example.spotify_api.model.AlbumDetailsResponse
import com.example.spotify_api.model.AlbumMetadataResponse
import com.example.spotify_api.model.SearchResult
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyApi {

    @GET("search/")
    suspend fun search(
        @Query("q") query: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20,
        @Query("numberOfTopResults") topResults: Int = 5
    ): Response<SearchResult>

    @GET("browse_all/")
    suspend fun browseAll(): Response<ResponseBody>

    @GET("albums/")
    suspend fun getAlbumDetails(
        @Query("ids") ids: String
    ): Response<AlbumDetailsResponse>

    @GET("album_metadata/")
    suspend fun getAlbumMetadata(
        @Query("id") id: String
    ): Response<AlbumMetadataResponse> // Use the new data model

}
