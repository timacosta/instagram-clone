package io.keepcoding.instagram_clone.network

import retrofit2.http.GET
import retrofit2.http.Headers


interface ImgurApi {
    @Headers("Authorization: Client-ID e0e489a0e367b90")
    @GET("gallery/hot")
    suspend fun getHotGallery(): NetworkGallery

    @Headers("Authorization: Client-ID e0e489a0e367b90")
    @GET("gallery/top")
    suspend fun getTopGallery(): NetworkGallery

    @GET("gallery/me/images")
    suspend fun getMeGallery(): NetworkGallery
}
