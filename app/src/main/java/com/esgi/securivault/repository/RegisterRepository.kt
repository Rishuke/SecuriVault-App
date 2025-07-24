package com.esgi.securivault.repository

import android.util.Log
import com.esgi.securivault.data.dto.RegisterDTO
import com.esgi.securivault.networking.RetrofitHttpClient
import com.esgi.securivault.networking.services.RegisterServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository {

    private val registerServices = RetrofitHttpClient.createRetrofit { null }
        .create(RegisterServices::class.java)

    fun registerUser(email: String, password: String, confirmpassword: String, callback: (Boolean, String?) -> Unit) {
        val request = RegisterDTO(email, password, confirmpassword)
        registerServices.createUser(request).enqueue(object : Callback<RegisterDTO> {
            override fun onResponse(call: Call<RegisterDTO>, response: Response<RegisterDTO>) {
                if (response.isSuccessful) {
                    Log.d("RegisterRepository", "Utilisateur créé avec succès")
                    callback(true, null)
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> {
                            val errorBody = response.errorBody()?.string()
                            when {
                                errorBody?.contains("User already exists") == true -> "Cet email est déjà utilisé"
                                else -> "Données invalides"
                            }
                        }
                        else -> "Erreur lors de la création du compte"
                    }
                    Log.e("RegisterRepository", "Erreur HTTP ${response.code()}: ${response.errorBody()?.string()}")
                    callback(false, errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterDTO>, t: Throwable) {
                Log.e("RegisterRepository", "Erreur réseau: ${t.localizedMessage}", t)
                callback(false, "Erreur de connexion")
            }
        })
    }
}