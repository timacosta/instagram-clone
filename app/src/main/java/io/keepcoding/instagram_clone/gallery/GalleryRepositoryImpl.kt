package io.keepcoding.instagram_clone.gallery

import android.net.Network
import io.keepcoding.instagram_clone.GalleryViewModel
import io.keepcoding.instagram_clone.gallery.Gallery.Image
import io.keepcoding.instagram_clone.network.ImgurApi
import io.keepcoding.instagram_clone.network.NetworkGallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryRepositoryImpl(private val imgurApi: ImgurApi): GalleryRepository {
    override suspend fun getHotGallery() =
        withContext(Dispatchers.IO) {
            imgurApi.getHotGallery().toDomain()
        }

    override suspend fun getTopGallery() =
        withContext(Dispatchers.IO) {
            imgurApi.getTopGallery().toDomain()
        }


    private fun NetworkGallery.toDomain(): Gallery {
        val images = data.mapNotNull { image ->
            image.images?.first()
        }.filter { image ->
            image.link.contains(".jpg") || image.link.contains(".png")
        }.map { image ->
            Image(
                id = image.id,
                title = image.title,
                url = image.link,
                likes = image.favorite_count ?: 0,
                datetime = image.datetime,
                author = image.account_url
            )
        }
        return Gallery(images)
    }


}