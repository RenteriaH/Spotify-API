package com.example.spotify_api.model

/**
 * Objeto de respuesta para el endpoint de nuevos lanzamientos (/browse/new-releases).
 * Contiene un objeto 'albums' que a su vez tiene la lista de álbumes.
 */
data class NewReleasesResponse(
    val albums: AlbumSearchResult
)
