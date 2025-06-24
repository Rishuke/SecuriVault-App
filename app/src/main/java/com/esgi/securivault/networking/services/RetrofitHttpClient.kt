package com.esgi.securivault.networking.services


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHttpClient{
    private const val BASE_URL = "http://localhost:8080/"
    private const val REGISTER_URL = BASE_URL+ "auth"
    private const val LOGIN_URL = REGISTER_URL + "login"


    val loginRetrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(LOGIN_URL) // Configured HTTp client's base url
            .addConverterFactory(GsonConverterFactory.create())
            .build()// Added a converter for JSON --> DataClass
    }
    val registerRetrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(REGISTER_URL) // Configured HTTp client's base url
            .addConverterFactory(GsonConverterFactory.create())
            .build()// Added a converter for JSON --> DataClass
    }
}

