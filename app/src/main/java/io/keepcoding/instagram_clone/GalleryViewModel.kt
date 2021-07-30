package io.keepcoding.instagram_clone

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GalleryViewModel(val api: ImgurApi): ViewModel() {
    val stateMLD: MutableLiveData<GalleryState> = MutableLiveData()
    val state: LiveData<GalleryState>
        get() = stateMLD

    private var requestJob: Job? = null


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

        stateMLD.postValue(GalleryState(images))
    }

    data class GalleryState(val images: List<Image>)

}