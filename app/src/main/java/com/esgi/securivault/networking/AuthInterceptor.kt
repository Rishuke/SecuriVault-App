package com.esgi.securivault.networking

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    companion object {
        private var authToken: String? = null

        fun setToken(token: String) {
            authToken = token
        }

        fun clearToken() {
            authToken = null
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        return if (authToken != null) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}