package com.example.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Representa una única categoría de Spotify (p. ej., "Rock", "Pop", "Para dormir").
 */
data class Category(
    val href: String,       // Un enlace al endpoint de la API que devuelve los detalles completos de la categoría.
    val id: String,         // El ID de Spotify de la categoría.
    val name: String,       // El nombre de la categoría.
    val icons: List<Image>  // El icono de la categoría en varios tamaños.
)

/**
 * Representa la respuesta del endpoint que devuelve una lista de categorías.
 */
data class CategoriesResponse(
    val categories: Categories // El objeto que contiene la lista paginada de categorías.
)

/**
 * Contiene la lista paginada de categorías.
 */
data class Categories(
    val items: List<Category>,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
)

/**
 * Representa la respuesta del endpoint que devuelve las playlists de una categoría específica.
 */
data class CategoryPlaylistsResponse(
    val playlists: PlaylistSearchResult // La API devuelve un objeto `playlists` que contiene la lista paginada.
)
