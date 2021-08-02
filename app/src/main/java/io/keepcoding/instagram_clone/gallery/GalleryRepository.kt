package io.keepcoding.instagram_clone.gallery

interface GalleryRepository {

    suspend fun getHotGallery(): Gallery

    suspend fun getTopGallery(): Gallery

    suspend fun getMyGallery(): Gallery

}