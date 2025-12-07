package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

data class SimplifiedShow(
    @SerializedName("available_markets") val availableMarkets: List<String>,
    val copyrights: List<Copyright>,
    val description: String,
    val explicit: Boolean,
    @SerializedName("external_urls") val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    @SerializedName("is_externally_hosted") val isExternallyHosted: Boolean,
    val languages: List<String>,
    @SerializedName("media_type") val mediaType: String,
    val name: String,
    val publisher: String,
    val type: String,
    val uri: String,
    @SerializedName("total_episodes") val totalEpisodes: Int,
    @SerializedName("html_description") val htmlDescription: String
)
