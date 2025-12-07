package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class ResumePoint(
    @SerializedName("fully_played") val fullyPlayed: Boolean,
    @SerializedName("resume_position_ms") val resumePositionMs: Int
)
