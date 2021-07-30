package io.keepcoding.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import io.keepcoding.instagram_clone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        val adapter = GalleryRecyclerAdapter()
        binding.galleryRecyclerView.adapter = adapter

        viewModel.getHotImages()
        viewModel.state.observe(this) { state ->
            adapter.imageList = state.images
        }

        binding.bottomBar.setOnItemSelectedListener { menu ->
            when(menu.itemId) {
                R.id.menu_hot -> {viewModel.getHotImages()}
                R.id.menu_top -> {viewModel.getTopImages()}
            }
            true
        }


    }
}