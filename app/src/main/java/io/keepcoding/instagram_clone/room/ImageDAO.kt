package io.keepcoding.instagram_clone.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.keepcoding.instagram_clone.room.RoomImage.ImageType

@Dao
interface ImageDAO {

    @Query("SELECT * FROM images WHERE type = :type")
    fun getImages(type: ImageType): List<RoomImage>

    @Insert(onConflict = REPLACE)
    fun insertImages(imagesList: List<RoomImage>)

}