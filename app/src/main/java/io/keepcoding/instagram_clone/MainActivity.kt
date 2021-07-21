package io.keepcoding.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.keepcoding.instagram_clone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val adapter = GalleryRecyclerAdapter()
        binding.galleryRecyclerView.adapter = adapter


        adapter.imageList = listOf(Image("url"))


    }
}