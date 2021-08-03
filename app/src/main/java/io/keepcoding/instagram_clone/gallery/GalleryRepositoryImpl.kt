package io.keepcoding.instagram_clone.gallery

import io.keepcoding.instagram_clone.gallery.Gallery.Image
import io.keepcoding.instagram_clone.network.ImgurApi
import io.keepcoding.instagram_clone.network.NetworkGallery
import io.keepcoding.instagram_clone.room.ImageDAO
import io.keepcoding.instagram_clone.room.RoomImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class GalleryRepositoryImpl(
    private val imgurApi: ImgurApi,
    private val imageDAO: ImageDAO
    ): GalleryRepository {

    override suspend fun getHotGallery() =
        withContext(Dispatchers.IO) {
            try {
                imgurApi.getHotGallery().toDomain().also { gallery ->
                    imageDAO.insertImages(gallery.toRoom())
                }
            } catch (e: Exception) {
                Timber.e(e)
                imageDAO.getImages(RoomImage.ImageType.HOT).toDomain()
            }
        }

    override suspend fun getTopGallery() =
        withContext(Dispatchers.IO) {
            imgurApi.getTopGallery().toDomain()
        }

    override suspend fun getMyGallery(): Gallery =
        withContext(Dispatchers.IO) {
            imgurApi.getMeGallery().toDomain()
        }

    private fun NetworkGallery.toDomain(): Gallery {
        val images = data.filter { image ->
            val imageLink = image.images?.first()?.link ?: image.link
            imageLink?.contains(".jpg") || imageLink?.contains(".png")
        }.mapNotNull { image ->
            val imageLink = image.images?.first()?.link ?: image.link
            Image(
                id = image.id,
                title = image.title,
                url = imageLink,
                likes = image.favorite_count ?: 0,
                datetime = image.datetime,
                author = image.account_url
            )
        }
        return Gallery(images)
    }

    private fun List<RoomImage>.toDomain(): Gallery {
        val images = map { roomImage ->
            Image(
                id = roomImage.id ,
                title = roomImage.title,
                url = roomImage.url,
                likes = roomImage.likes,
                datetime = roomImage.datetime,
                author = roomImage.author
            )
        }
        return Gallery(images)
    }

    private fun Gallery.toRoom(imageType: RoomImage.ImageType): List<RoomImage> {
        return images.map { image ->
            RoomImage(
                id = image.id,
                title = image.title,
                url = image.url,
                likes = image.likes,
                datetime = image.datetime,
                author = image.author,
                type = imageType
            )
        }
    }

}