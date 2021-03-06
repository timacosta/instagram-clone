package io.keepcoding.instagram_clone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.instagram_clone.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.direct
import org.kodein.di.instance
import retrofit2.Retrofit


class MainActivity : AppCompatActivity(), DIAware {

    override val di: DI by di()
    private val viewModel: GalleryViewModel by lazy {
        ViewModelProvider(this, direct.instance()).get(GalleryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.processIntentData(intent)

        val binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val adapter = GalleryRecyclerAdapter()
        binding.galleryRecyclerView.adapter = adapter

        viewModel.getHotImages()


        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                adapter.imageList = state.images
                if(state.hasError) {
                    Snackbar.make(binding.root,"Error", 5000).show()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.session.collect { sessionState ->
                binding.bottomBar.menu.findItem(R.id.menu_login).apply {
                    title = when(sessionState.hasSession) {
                        true -> sessionState.accountName
                        false -> "Login"
                    }
                    isEnabled = !sessionState.hasSession
                }

                binding.bottomBar.menu.findItem(R.id.menu_my_pics).isVisible = sessionState.hasSession
            }
        }


        binding.bottomBar.setOnItemSelectedListener { menu ->
            when(menu.itemId) {
                R.id.menu_hot -> viewModel.getHotImages()
                R.id.menu_top -> viewModel.getTopImages()
                R.id.menu_my_pics -> viewModel.getMyPics()
                R.id.menu_login -> {oauth2Flow()}
            }
            true
        }

    }

    private fun oauth2Flow() {
        val url = "https://api.imgur.com/oauth2/authorize?client_id=e0e489a0e367b90&response_type=token"
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }.also {
            startActivity(it)
        }
    }
}