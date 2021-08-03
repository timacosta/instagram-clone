package io.keepcoding.instagram_clone.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomImage::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDAO
}