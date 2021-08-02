package io.keepcoding.instagram_clone.network

import retrofit2.http.GET
import retrofit2.http.Headers


interface ImgurApi {
    @Headers("Authorization: Client-ID 9cffc969562a2f2")
    @GET("gallery/hot")
    suspend fun getHotGallery(): NetworkGallery

    @Headers("Authorization: Client-ID 9cffc969562a2f2")
    @GET("gallery/top")
    suspend fun getTopGallery(): NetworkGallery

    @GET("gallery/me/images")
    suspend fun getMeGallery(): NetworkGallery
}
