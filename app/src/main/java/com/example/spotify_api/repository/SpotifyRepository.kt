package com.example.spotify_api.repository

import com.example.spotify_api.api.SpotifyApiService
import com.example.spotify_api.data.SpotifyAuthManager
import com.example.spotify_api.model.*
import javax.inject.Inject

class SpotifyRepository @Inject constructor(
    private val spotifyApiService: SpotifyApiService,
    private val authManager: SpotifyAuthManager
) {

    // --- ¡NUEVA FUNCIÓN PARA RECOMENDACIONES! ---
    suspend fun getRecommendations(seedArtists: String, seedTracks: String): RecommendationsResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getRecommendations(token, seedArtists, seedTracks)
    }

    suspend fun getUserProfile(): UserProfile {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getUserProfile(token)
    }

    suspend fun getTopArtists(timeRange: String = "medium_term", limit: Int = 20): TopArtistsResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getTopArtists(token, timeRange, limit)
    }

    suspend fun getArtist(artistId: String): Artist {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getArtist(token, artistId)
    }

    suspend fun getArtistTopTracks(artistId: String): ArtistTopTracksResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getArtistTopTracks(token, artistId)
    }

    suspend fun getArtistAlbums(artistId: String): ArtistAlbumsResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getArtistAlbums(token, artistId)
    }

    suspend fun getAlbum(albumId: String): Album {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getAlbum(token, albumId)
    }

    suspend fun search(
        query: String,
        type: String,
        limit: Int? = null,
        offset: Int? = null,
        includeExternal: String? = null
    ): SearchResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.search(token, query, type, "ES", limit, offset, includeExternal)
    }

    suspend fun getNewReleases(): NewReleasesResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getNewReleases(token)
    }

    suspend fun getPlaylist(playlistId: String, fields: String? = null): Playlist {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getPlaylist(token, playlistId, "ES", fields)
    }

    suspend fun createPlaylist(userId: String, request: CreatePlaylistRequest): Playlist {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.createPlaylist(token, userId, request)
    }

    suspend fun getCategories(locale: String? = null, limit: Int? = null, offset: Int? = null): CategoriesResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getCategories(token, locale, limit, offset)
    }

    suspend fun getCategory(categoryId: String, locale: String? = null): Category {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getCategory(token, categoryId, locale)
    }

    suspend fun getCategoryPlaylists(categoryId: String): CategoryPlaylistsResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getCategoryPlaylists(token, categoryId)
    }

    suspend fun getAudiobook(id: String, market: String? = null): Audiobook {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getAudiobook(token, id, market)
    }

    suspend fun getSeveralAudiobooks(ids: String, market: String? = null): SeveralAudiobooksResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getSeveralAudiobooks(token, ids, market)
    }

    suspend fun getAudiobookChapters(id: String, market: String? = null, limit: Int? = null, offset: Int? = null): PagingObject<SimplifiedChapter> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getAudiobookChapters(token, id, market, limit, offset)
    }

    suspend fun getFeaturedPlaylists(): FeaturedPlaylistsResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getFeaturedPlaylists(token, locale = "es_ES")
    }

    // --- ¡FUNCIÓN CORREGIDA! ---
    suspend fun getMyTopTracks(limit: Int = 20): PagingObject<Track> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getMyTopTracks(token, limit = limit)
    }

    suspend fun getTrack(trackId: String): Track {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getTrack(token, trackId)
    }

    suspend fun getMySavedAlbums(limit: Int = 50): PagingObject<SavedAlbum> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getMySavedAlbums(token, limit)
    }

    suspend fun getMyPlaylists(limit: Int = 50): PagingObject<Playlist> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getMyPlaylists(token, limit)
    }

    suspend fun getRecentlyPlayed(limit: Int = 20): PagingObject<PlayHistoryObject> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getRecentlyPlayed(token, limit)
    }

    // --- SHOWS ---
    suspend fun getShow(id: String, market: String? = null): Show {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getShow(token, id, market)
    }

    suspend fun getSeveralShows(ids: String, market: String? = null): SeveralShowsResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getSeveralShows(token, ids, market)
    }

    suspend fun getShowEpisodes(id: String, market: String? = null, limit: Int? = null, offset: Int? = null): PagingObject<SimplifiedEpisode> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getShowEpisodes(token, id, market, limit, offset)
    }

    suspend fun getMySavedShows(limit: Int? = null, offset: Int? = null): PagingObject<SavedShowObject> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getMySavedShows(token, limit, offset)
    }

    suspend fun saveShowsForCurrentUser(ids: String) {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        spotifyApiService.saveShowsForCurrentUser(token, ids)
    }

    suspend fun removeUsersSavedShows(ids: String, market: String? = null) {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        spotifyApiService.removeUsersSavedShows(token, ids, market)
    }

    suspend fun checkUsersSavedShows(ids: String): List<Boolean> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.checkUsersSavedShows(token, ids)
    }
}