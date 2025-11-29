package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class AlbumMetadataResponse(
    @SerializedName("data") val data: MetadataData
)

data class MetadataData(
    @SerializedName("album") val album: AlbumMetadata
)

data class AlbumMetadata(
    @SerializedName("tracks") val tracks: AlbumTracksMetadata,
    @SerializedName("label") val label: String?,
    @SerializedName("copyright") val copyright: Copyright?
)

data class AlbumTracksMetadata(
    @SerializedName("items") val items: List<MetadataTrackItem>
)

data class MetadataTrackItem(
    @SerializedName("track") val track: MetadataTrack
)

data class MetadataTrack(
    @SerializedName("duration") val duration: MetadataDuration
)

data class MetadataDuration(
    @SerializedName("totalMilliseconds") val totalMilliseconds: Int
)

data class Copyright(
    @SerializedName("items") val items: List<CopyrightItem>
)

data class CopyrightItem(
    @SerializedName("text") val text: String
)
