package io.keepcoding.instagram_clone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GalleryViewModel: ViewModel() {

    val state: MutableLiveData<GalleryState> = MutableLiveData()

    private val api: ImgurApi
    private var requestJob: Job? = null

    init {
        val client = OkHttpClient().newBuilder().build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/3/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(ImgurApi::class.java)
    }

    fun getHotImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(Dispatchers.IO) {
            val gallery = api.getHotGallery()
            Log.d("Tag","$gallery")

            parseGallery(gallery)

        }
    }


    fun getTopImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(Dispatchers.IO) {
            val gallery = api.getTopGallery()
            Log.d("Tag","$gallery")

            parseGallery(gallery)

        }
    }

    private fun parseGallery(gallery: Gallery) {
        val images = gallery.data.mapNotNull { image ->
            image.images?.first()?.link
        }.filter { link ->
            link.contains(".jpg") || link.contains(".png")
        }.map { link ->
            Image(link)
        }

        state.postValue(GalleryState(images))
    }

    data class GalleryState(val images: List<Image>)

}