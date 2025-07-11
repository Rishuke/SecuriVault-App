package com.esgi.securivault.networking.services

import com.esgi.securivault.data.dto.LoginRequest
import com.esgi.securivault.data.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface LoginServices {
    @POST("auth/login")
    fun loginUsr(@Body loginRequest: LoginRequest): Call<LoginResponse>
}