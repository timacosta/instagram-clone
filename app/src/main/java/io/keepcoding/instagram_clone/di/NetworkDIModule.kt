package io.keepcoding.instagram_clone.di

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.keepcoding.instagram_clone.network.AuthInterceptor
import io.keepcoding.instagram_clone.network.ImgurApi
import io.keepcoding.instagram_clone.network.LoggerInterceptor
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkDIModule: DIBaseModule("NetworkDIModule") {

    override val builder: DI.Builder.() -> Unit = {
        bind<OkHttpClient>() with singleton {
            Log.e("DEBUG", "OkHttpClient")
            OkHttpClient()
                .newBuilder()
                .addInterceptor(LoggerInterceptor())
                .addInterceptor(AuthInterceptor(instance()))
                .build()
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

        bind<ImgurApi>() with singleton {
            instance<Retrofit>().create(ImgurApi::class.java)
        }

    }
}