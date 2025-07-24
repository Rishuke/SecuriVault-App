package com.esgi.securivault.networking.services


import com.esgi.securivault.data.dto.FireBaseLogInResponse
import com.esgi.securivault.data.dto.FirebaseLogInRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface LoginServices {
    @POST("auth/login")
    fun loginUsr(@Body loginRequest: FirebaseLogInRequest): Call<FireBaseLogInResponse>
}