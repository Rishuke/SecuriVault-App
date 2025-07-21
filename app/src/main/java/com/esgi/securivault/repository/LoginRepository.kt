package com.esgi.securivault.repository

import android.util.Log
import com.esgi.securivault.data.dto.LoginRequest
import com.esgi.securivault.data.dto.LoginResponse
import com.esgi.securivault.networking.RetrofitHttpClient
import com.esgi.securivault.networking.services.LoginServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    private val loginServices = RetrofitHttpClient.createRetrofit { null }
        .create(LoginServices::class.java)

    fun loginUser(email: String, password: String, callback: (String?) -> Unit) {
        val request = LoginRequest(email, password)
        loginServices.loginUsr(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.idToken)
                } else {
                    Log.e("LoginRepository", "Erreur HTTP ${response.code()}: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginRepository", "Erreur réseau: ${t.localizedMessage}", t)
                callback(null)
            }
        })
    }
}
