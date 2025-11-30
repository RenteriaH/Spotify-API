package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class CreatePlaylistRequest(
    val name: String,
    val description: String? = null,
    val public: Boolean? = null,
    val collaborative: Boolean? = null
)
