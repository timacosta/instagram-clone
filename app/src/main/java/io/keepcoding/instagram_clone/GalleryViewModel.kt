package io.keepcoding.instagram_clone

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.keepcoding.instagram_clone.network.Gallery
import io.keepcoding.instagram_clone.network.ImgurApi
import io.keepcoding.instagram_clone.session.Session
import io.keepcoding.instagram_clone.session.SessionLocalDataSource
import kotlinx.coroutines.*

class GalleryViewModel(val api: ImgurApi, val localDataSource: SessionLocalDataSource): ViewModel() {
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

    fun processIntentData(intent: Intent) {
        val url = intent.data.toString()
        "imgram://oauth2.+".toRegex().matches(url).alsoIfTrue {
            val accestoken = "access_token=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "expires_in=(\\w+)".toRegex().find(url)!!.groupValues[1].toLong() + System.currentTimeMillis()
            "token_type=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "refresh_token=(\\w+)".toRegex().find(url)!!.groupValues[1]
            val accountName = "account_username=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "account_id=(\\w+)".toRegex().find(url)!!.groupValues[1]

            localDataSource.saveSession(Session(accestoken, accountName))


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