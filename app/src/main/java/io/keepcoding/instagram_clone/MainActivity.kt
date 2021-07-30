package io.keepcoding.instagram_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import io.keepcoding.instagram_clone.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance
import retrofit2.Retrofit


class MainActivity : AppCompatActivity(), DIAware {

    override val di: DI by di()

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

        val client = di.instance<Retrofit>()
        Log.e("DEBUG", client.toString())

    }
}