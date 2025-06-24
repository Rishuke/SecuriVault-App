package com.esgi.securivault.repository

import com.esgi.securivault.data.dto.LoginRequest
import com.esgi.securivault.data.dto.LoginResponse
import com.esgi.securivault.networking.services.LoginServices
import com.esgi.securivault.networking.services.RetrofitHttpClient
import retrofit2.Call

class LoginRepository {
    private val loginServices = RetrofitHttpClient.loginRetrofit.create<LoginServices>(LoginServices::class.java)
        fun loginUser(email: String, password: String, callback: (String?) -> Unit) {
            val request = LoginRequest(email, password)
            loginServices.loginUsr(request).enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: retrofit2.Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        callback(response.body()?.idtoken)
                    } else {
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    callback(null)
                }
            })
        }
    }
