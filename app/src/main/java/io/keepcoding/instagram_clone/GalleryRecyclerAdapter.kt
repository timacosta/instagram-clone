package io.keepcoding.instagram_clone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.keepcoding.instagram_clone.databinding.GalleryRowBinding
import io.keepcoding.instagram_clone.gallery.Gallery
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class GalleryRecyclerAdapter: RecyclerView.Adapter<GalleryViewHolder>() {

    var imageList : List<Gallery.Image> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder =
        GalleryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { GalleryViewHolder(this) }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image: Gallery.Image = imageList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int = imageList.size

}

data class GalleryViewHolder(val binding: GalleryRowBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Gallery.Image) {
        with(binding) {
            titleTextView.text = image.title ?: "No title"

            authorTextView.text = image.author ?: "Unknown"

            authorAvatarImageView.setImageBitmap(null)
            Glide.with(root)
                .load(image.authorAvatar).also { it.circleCrop() }
                .into(authorAvatarImageView)

            imageView.setImageBitmap(null)
            Glide.with(root)
                .load(image.url)
                .into(imageView)

        }
    }

    private suspend fun downloadImage(image: Gallery.Image): Bitmap {
        return withContext(Dispatchers.IO) {
            val okHttpClient = OkHttpClient()
            val request: Request = Request.Builder().url(image.url).build()
            val response: Response = okHttpClient.newCall(request).execute()
            val bitmap: Bitmap = BitmapFactory.decodeStream(response.body!!.byteStream())
            return@withContext bitmap
        }

    }
}

