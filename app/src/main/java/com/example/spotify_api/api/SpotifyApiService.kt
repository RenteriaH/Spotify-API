package com.example.spotify_api.api

import com.example.spotify_api.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApiService {

    // ... (endpoints existentes de Perfil, Artistas, Catálogo, etc.)
    @GET("me")
    suspend fun getUserProfile(@Header("Authorization") token: String): UserProfile

    @GET("me/top/artists")
    suspend fun getTopArtists(
        @Header("Authorization") token: String,
        @Query("time_range") timeRange: String = "medium_term",
        @Query("limit") limit: Int = 20
    ): TopArtistsResponse

    @GET("artists/{id}")
    suspend fun getArtist(
        @Header("Authorization") token: String,
        @Path("id") artistId: String
    ): Artist

    @GET("artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(
        @Header("Authorization") token: String,
        @Path("id") artistId: String,
        @Query("market") market: String = "ES"
    ): ArtistTopTracksResponse

    @GET("artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Header("Authorization") token: String,
        @Path("id") artistId: String,
        @Query("include_groups") includeGroups: String = "album,single",
        @Query("limit") limit: Int = 50
    ): ArtistAlbumsResponse

    @GET("artists/{id}/related-artists")
    suspend fun getRelatedArtists(
        @Header("Authorization") token: String,
        @Path("id") artistId: String
    ): RelatedArtistsResponse

    @GET("albums/{id}")
    suspend fun getAlbum(
        @Header("Authorization") token: String,
        @Path("id") albumId: String,
        @Query("market") market: String = "ES"
    ): Album

    @GET("search")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("market") market: String = "ES",
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("include_external") includeExternal: String? = null
    ): SearchResponse

    @GET("browse/new-releases")
    suspend fun getNewReleases(
        @Header("Authorization") token: String,
        @Query("market") market: String = "ES"
    ): SearchResponse

    @GET("playlists/{playlist_id}")
    suspend fun getPlaylist(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String,
        @Query("market") market: String = "ES",
        @Query("fields") fields: String? = null
    ): Playlist

    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String,
        @Body body: CreatePlaylistRequest
    ): Playlist

    @GET("browse/categories")
    suspend fun getCategories(
        @Header("Authorization") token: String,
        @Query("locale") locale: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): CategoriesResponse

    @GET("browse/categories/{category_id}")
    suspend fun getCategory(
        @Header("Authorization") token: String,
        @Path("category_id") categoryId: String,
        @Query("locale") locale: String? = null
    ): Category

    @GET("browse/categories/{category_id}/playlists")
    suspend fun getCategoryPlaylists(
        @Header("Authorization") token: String,
        @Path("category_id") categoryId: String,
        @Query("limit") limit: Int = 20
    ): CategoryPlaylistsResponse

    // --- AUDIOBOOKS (¡NUEVO!) ---

    @GET("audiobooks/{id}")
    suspend fun getAudiobook(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("market") market: String? = null
    ): Audiobook

    @GET("audiobooks")
    suspend fun getSeveralAudiobooks(
        @Header("Authorization") token: String,
        @Query("ids") ids: String,
        @Query("market") market: String? = null
    ): SeveralAudiobooksResponse

    @GET("audiobooks/{id}/chapters")
    suspend fun getAudiobookChapters(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): PagingObject<SimplifiedChapter>
}
