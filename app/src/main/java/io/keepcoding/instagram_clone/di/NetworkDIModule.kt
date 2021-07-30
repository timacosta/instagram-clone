package io.keepcoding.instagram_clone.di

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkDIModule {
    fun create() = DI.Module(name = this::class.simpleName!!, allowSilentOverride = false, init = builder)

    private val builder: DI.Builder.() -> Unit = {
        bind<OkHttpClient>() with singleton {
            Log.e("DEBUG", "OkHttpClient")
            OkHttpClient().newBuilder().build()
        }

        bind<Moshi>() with singleton {
            Log.e("DEBUG", "Moshi")
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        }

        bind<Retrofit>() with singleton {
            Log.e("DEBUG", "Retrofi")
            Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .client(instance())
                .addConverterFactory(MoshiConverterFactory.create(instance()))
                .build()
        }
    }
}