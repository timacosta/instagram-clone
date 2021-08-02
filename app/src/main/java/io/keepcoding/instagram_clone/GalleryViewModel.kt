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
import io.keepcoding.instagram_clone.session.SessionRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import kotlin.random.Random

class GalleryViewModel(
    private val api: ImgurApi,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    val stateMLD: MutableLiveData<GalleryState> = MutableLiveData()
    val state: LiveData<GalleryState>
        get() = stateMLD

    val sessionMLD: MutableLiveData<SessionState> = MutableLiveData()
    val session: LiveData<SessionState>
        get() = sessionMLD

    private var requestJob: Job? = null
    private val handler = CoroutineExceptionHandler { couroutineContext, throwable ->
        Timber.e(throwable)
    }

    init {
        sessionRepository.getSession().let { session ->
            sessionMLD.postValue(
                SessionState(session != null, session?.accountName)
            )
        }
    }


    fun getHotImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(Dispatchers.IO+handler) {
            try {
                val gallery = api.getHotGallery()
                parseGallery(gallery)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


    fun getTopImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(Dispatchers.IO+handler) {
            val gallery = api.getTopGallery()
            Log.d("Tag", "$gallery")

            parseGallery(gallery)

        }

    }

    fun processIntentData(intent: Intent) {
        val url = intent.data.toString()
        "imgram://oauth2.+".toRegex().matches(url).alsoIfTrue {
            val accestoken = "access_token=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "expires_in=(\\w+)".toRegex()
                .find(url)!!.groupValues[1].toLong() + System.currentTimeMillis()
            "token_type=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "refresh_token=(\\w+)".toRegex().find(url)!!.groupValues[1]
            val accountName = "account_username=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "account_id=(\\w+)".toRegex().find(url)!!.groupValues[1]

            Session(accestoken, accountName).also { session ->
                sessionRepository.saveSession(session)
            }.also { session ->
                sessionMLD.postValue(SessionState(true, session.accountName))
            }
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

    suspend fun makeRequest(): String {
        Timber.tag("CCC").d("Start")
        delay(150)
        Timber.tag("CCC").d("End")
        return "${Random.nextLong(1000)}"
    }

    data class GalleryState(val images: List<Image>)
    data class SessionState(val hasSession: Boolean, val accountName: String?)
}