package com.esgi.securivault.networking.services

import com.esgi.securivault.data.dto.RegisterDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterServices{
    @POST("localhost:8080/auth")
    fun createUser(@Body registerDTO: RegisterDTO): Call<RegisterDTO>
}