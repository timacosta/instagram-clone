package io.keepcoding.instagram_clone

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.keepcoding.instagram_clone.gallery.Gallery
import io.keepcoding.instagram_clone.gallery.GalleryRepository
import io.keepcoding.instagram_clone.session.Session
import io.keepcoding.instagram_clone.session.SessionRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import kotlin.random.Random

class GalleryViewModel(
    private val galleryRepository: GalleryRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val stateFlow: MutableStateFlow<GalleryState> = MutableStateFlow(GalleryState.empty())
    val state: StateFlow<GalleryState>
        get() = stateFlow

    private val sessionMLD: MutableStateFlow<SessionState> = MutableStateFlow(SessionState.empty())
    val session: StateFlow<SessionState>
        get() = sessionMLD

    private var requestJob: Job? = null
    private val handler = CoroutineExceptionHandler { couroutineContext, throwable ->
        Timber.e(throwable)
        stateFlow.value = (GalleryState(emptyList(), true))
    }

    init {
        sessionRepository.getSession().let { session ->
            sessionMLD.value = (
                SessionState(session != null, session?.accountName)
            )
        }
    }


    fun getHotImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(handler) {
            val gallery = galleryRepository.getHotGallery()
            stateFlow.value = (GalleryState(gallery.images))
        }
    }


    fun getTopImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(handler) {
            val gallery = galleryRepository.getTopGallery()
            stateFlow.value = (GalleryState(gallery.images))
        }

    }


    fun getMyPics() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(handler) {
            val myGallery = galleryRepository.getMyGallery()
            stateFlow.value = (GalleryState(myGallery.images))
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
                sessionMLD.value = (SessionState(true, session.accountName))
            }
        }
    }



    suspend fun makeRequest(): String {
        Timber.tag("CCC").d("Start")
        delay(150)
        Timber.tag("CCC").d("End")
        return "${Random.nextLong(1000)}"
    }

    data class GalleryState(val images: List<Gallery.Image>, val hasError: Boolean = false) {
        companion object {
            fun empty() = GalleryState(emptyList(), false)
        }
    }
    data class SessionState(val hasSession: Boolean, val accountName: String?) {
        companion object {
            fun empty() = SessionState(false, null)
        }
    }
}