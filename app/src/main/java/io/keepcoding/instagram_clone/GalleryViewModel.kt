package io.keepcoding.instagram_clone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GalleryViewModel: ViewModel() {

    val state: MutableLiveData<GalleryState> = MutableLiveData()

    fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient().newBuilder().build()
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val api: ImgurApi = retrofit.create(ImgurApi::class.java)
            val gallery = api.getHotGallery()
            Log.d("Tag","$gallery")

            val images = gallery.data.mapNotNull { image ->
                image.images?.first()?.link
            }.filter { link ->
                link.contains(".jpg") || link.contains(".png")
            }.map { link ->
                Image(link)
            }

            state.postValue(GalleryState(images))

        }
    }

    data class GalleryState(val images: List<Image>)

}