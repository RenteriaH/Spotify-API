package com.example.spotify_api.api

import com.example.spotify_api.model.*
import retrofit2.http.*

interface SpotifyApiService {

    // ... (endpoints existentes)

    // --- ¡ENDPOINT CORREGIDO Y DEFINITIVO! ---
    @GET("recommendations")
    suspend fun getRecommendations(
        @Header("Authorization") token: String,
        @Query("seed_artists") seedArtists: String,
        @Query("seed_tracks") seedTracks: String,
        @Query("limit") limit: Int = 20
    ): RecommendationsResponse

    @GET("me")
    suspend fun getUserProfile(@Header("Authorization") token: String): UserProfile

    @GET("me/top/artists")
    suspend fun getTopArtists(
        @Header("Authorization") token: String,
        @Query("time_range") timeRange: String = "medium_term",
        @Query("limit") limit: Int = 20
    ): TopArtistsResponse

    @GET("me/playlists")
    suspend fun getMyPlaylists(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 50
    ): PagingObject<Playlist>

    @GET("me/player/recently-played")
    suspend fun getRecentlyPlayed(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 20
    ): PagingObject<PlayHistoryObject>

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
        @Header("Authorization") token: String
    ): NewReleasesResponse

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

    @GET("me/albums")
    suspend fun getMySavedAlbums(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 50
    ): PagingObject<SavedAlbum>

    // --- AUDIOBOOKS ---
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

    // --- HOME SCREEN ---
    @GET("browse/featured-playlists")
    suspend fun getFeaturedPlaylists(
        @Header("Authorization") token: String,
        @Query("locale") locale: String?,
        @Query("limit") limit: Int = 20
    ): FeaturedPlaylistsResponse

    @GET("me/top/tracks")
    suspend fun getMyTopTracks(
        @Header("Authorization") token: String,
        @Query("time_range") timeRange: String = "medium_term",
        @Query("limit") limit: Int = 20
    ): PagingObject<Track>

    @GET("tracks/{id}")
    suspend fun getTrack(
        @Header("Authorization") token: String,
        @Path("id") trackId: String
    ): Track

    // --- SHOWS ---
    @GET("shows/{id}")
    suspend fun getShow(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("market") market: String? = null
    ): Show

    @GET("shows")
    suspend fun getSeveralShows(
        @Header("Authorization") token: String,
        @Query("ids") ids: String,
        @Query("market") market: String? = null
    ): SeveralShowsResponse

    @GET("shows/{id}/episodes")
    suspend fun getShowEpisodes(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("market") market: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): PagingObject<SimplifiedEpisode>

    @GET("me/shows")
    suspend fun getMySavedShows(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): PagingObject<SavedShow>

    @PUT("me/shows")
    suspend fun saveShowsForCurrentUser(
        @Header("Authorization") token: String,
        @Query("ids") ids: String
    ): Unit

    @DELETE("me/shows")
    suspend fun removeUsersSavedShows(
        @Header("Authorization") token: String,
        @Query("ids") ids: String,
        @Query("market") market: String? = null
    ): Unit

    @GET("me/shows/contains")
    suspend fun checkUsersSavedShows(
        @Header("Authorization") token: String,
        @Query("ids") ids: String
    ): List<Boolean>
}
