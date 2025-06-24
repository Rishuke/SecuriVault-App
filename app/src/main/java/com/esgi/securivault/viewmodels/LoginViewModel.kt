package com.esgi.securivault.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.esgi.securivault.repository.LoginRepository

class LoginViewModel : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var error = mutableStateOf<String?>(null)
    var isLoading = mutableStateOf(false)
    var loginSuccess = mutableStateOf(false)

    private val loginRepository = LoginRepository()

    fun login() {
        if (email.value.isBlank() || password.value.isBlank()) {
            error.value = "Veuillez remplir tous les champs"
            return
        }
        isLoading.value = true
        loginRepository.loginUser(email.value, password.value) { token ->
            isLoading.value = false
            if (token != null) {
                loginSuccess.value = true
                error.value = null
            } else {
                error.value = "Email ou mot de passe incorrect"
            }
        }
    }
}