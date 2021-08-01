package io.keepcoding.instagram_clone.network

import io.keepcoding.instagram_clone.session.SessionLocalDataSource
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val localDataSource: SessionLocalDataSource): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = localDataSource.retrieveSession()?.accessToken

        return if (accessToken == null) {
            chain.proceed(request)
        } else {
            chain.proceed(
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken.").build()
            )
        }
    }
}