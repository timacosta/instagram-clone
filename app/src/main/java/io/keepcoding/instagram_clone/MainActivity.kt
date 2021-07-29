package io.keepcoding.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.keepcoding.instagram_clone.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        val viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        val adapter = GalleryRecyclerAdapter()
        binding.galleryRecyclerView.adapter = adapter

        viewModel.getImages()
        viewModel.state.observe(this) { state ->
            adapter.imageList = state.images
        }


    }
}