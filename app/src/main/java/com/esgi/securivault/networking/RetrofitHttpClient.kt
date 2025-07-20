package com.esgi.securivault.networking


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHttpClient{
    private const val LOGIN_URL  = "http://10.0.2.2:8080/"


    val loginRetrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(LOGIN_URL) // Configured HTTp client's base url
            .addConverterFactory(GsonConverterFactory.create())
            .build()// Added a converter for JSON --> DataClass
    }
}

