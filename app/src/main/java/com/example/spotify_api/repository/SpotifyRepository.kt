package com.example.spotify_api.repository

import com.example.spotify_api.api.SpotifyApiService
import com.example.spotify_api.data.SpotifyAuthManager
import com.example.spotify_api.model.*
import javax.inject.Inject

class SpotifyRepository @Inject constructor(
    private val spotifyApiService: SpotifyApiService,
    private val authManager: SpotifyAuthManager
) {

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

    suspend fun getRelatedArtists(artistId: String): RelatedArtistsResponse {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getRelatedArtists(token, artistId)
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
        return spotifyApiService.getFeaturedPlaylists(token)
    }

    suspend fun getMyTopTracks(): PagingObject<Track> {
        val token = authManager.getBearerToken() ?: throw Exception("Token not found")
        return spotifyApiService.getMyTopTracks(token)
    }
}
