package com.esgi.securivault.repository

import android.util.Log

import com.esgi.securivault.data.dto.FireBaseLogInResponse
import com.esgi.securivault.data.dto.FirebaseLogInRequest
import com.esgi.securivault.networking.RetrofitHttpClient
import com.esgi.securivault.networking.services.LoginServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    private val loginServices = RetrofitHttpClient.createRetrofit { null }
        .create(LoginServices::class.java)

    fun loginUser(email: String, password: String, callback: (String?) -> Unit) {
        val request = FirebaseLogInRequest(email, password)
        loginServices.loginUsr(request).enqueue(object : Callback<FireBaseLogInResponse> {
            override fun onResponse(call: Call<FireBaseLogInResponse>, response: Response<FireBaseLogInResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.idToken)
                } else {
                    Log.e("LoginRepository", "Erreur HTTP ${response.code()}: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<FireBaseLogInResponse>, t: Throwable) {
                Log.e("LoginRepository", "Erreur réseau: ${t.localizedMessage}", t)
                callback(null)
            }
        })
    }
}
