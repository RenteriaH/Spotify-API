package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class SavedShowObject(
    @SerializedName("added_at") val addedAt: String,
    val show: SimplifiedShow
)
