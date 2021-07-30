package io.keepcoding.instagram_clone

import retrofit2.http.GET
import retrofit2.http.Headers


interface ImgurApi {
    @Headers("Authorization: Client-ID 9cffc969562a2f2")
    @GET("gallery/hot")
    suspend fun getHotGallery(): Gallery

    @Headers("Authorization: Client-ID 9cffc969562a2f2")
    @GET("gallery/top")
    suspend fun getTopGallery(): Gallery

}
