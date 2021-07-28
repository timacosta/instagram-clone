package io.keepcoding.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.keepcoding.instagram_clone.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val adapter = GalleryRecyclerAdapter()
        binding.galleryRecyclerView.adapter = adapter

        adapter.imageList = listOf(Image("https://images.theconversation.com/files/290710/original/file-20190903-175663-lqb3z6.jpg"))

        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient().newBuilder().build()
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val api: ImgurApi = retrofit.create(ImgurApi::class.java)
            val response = api.getHotGallery()
            Log.d("Tag","$response")
        }


    }
}