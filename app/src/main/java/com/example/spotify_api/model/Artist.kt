package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String,
    val images: List<Image>? = null,
    val genres: List<String>? = null,
    val popularity: Int? = null,
    val followers: Followers? = null
)

data class ArtistSearchResult(
    val items: List<Artist>,
    val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int
)

data class ArtistTopTracksResponse(val tracks: List<Track>)

data class ArtistAlbumsResponse(val items: List<SimplifiedAlbum>)

data class RelatedArtistsResponse(
    @SerializedName("artists")
    val artists: List<Artist>
)
