package io.keepcoding.instagram_clone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.instagram_clone.databinding.GalleryRowBinding
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class GalleryRecyclerAdapter: RecyclerView.Adapter<GalleryViewHolder>() {

    var imageList : List<Image> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder =
        GalleryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { GalleryViewHolder(this) }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image: Image = imageList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int = imageList.size

}

data class GalleryViewHolder(val binding: GalleryRowBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Image) {

        GlobalScope.launch {
            val bitmap = downloadImage(image)
            withContext(Dispatchers.Main) {
                binding.imageView.setImageBitmap(bitmap)
            }
           /* GlobalScope.launch(Dispatchers.Main) {
                binding.imageView.setImageBitmap(bitmap)
            }*/
        }

        CoroutineScope(Dispatchers.Main).launch {  }
    }

    private suspend fun downloadImage(image: Image): Bitmap {
        return withContext(Dispatchers.IO) {
            val okHttpClient = OkHttpClient()
            val request: Request = Request.Builder().url(image.url).build()
            val response: Response = okHttpClient.newCall(request).execute()
            val bitmap: Bitmap = BitmapFactory.decodeStream(response.body!!.byteStream())
            return@withContext bitmap
        }

    }
}

