package io.keepcoding.instagram_clone.di

import io.keepcoding.instagram_clone.gallery.GalleryRepository
import io.keepcoding.instagram_clone.gallery.GalleryRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object GalleryDIModule: DIBaseModule("GalleryDIModule") {
    override val builder: DI.Builder.() -> Unit = {
        bind<GalleryRepository>() with singleton {
            GalleryRepositoryImpl(instance())
        }
    }

}