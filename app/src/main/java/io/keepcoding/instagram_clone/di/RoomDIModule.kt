package io.keepcoding.instagram_clone.di

import androidx.room.Room
import io.keepcoding.instagram_clone.room.AppDatabase
import io.keepcoding.instagram_clone.room.ImageDAO
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object RoomDIModule: DIBaseModule("RoomDIModule") {
    override val builder: DI.Builder.() -> Unit = {
        bind<AppDatabase>() with singleton {
            Room.
            databaseBuilder(instance(), AppDatabase::class.java, "insta-clon").build()
        }
        bind<ImageDAO>() with singleton {
            instance<AppDatabase>().imageDao()
        }
    }

}