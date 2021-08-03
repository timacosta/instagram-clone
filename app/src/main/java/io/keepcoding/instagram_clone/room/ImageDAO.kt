package io.keepcoding.instagram_clone.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ImageDAO {

    @Query("SELECT * FROM images WHERE type = :type")
    fun getImages(type: RoomImage.ImageType): List<RoomImage>

}