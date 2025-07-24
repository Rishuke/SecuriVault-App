package com.esgi.securivault.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.esgi.securivault.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    private var onRegisterSuccess: (() -> Unit)? = null

    fun setOnRegisterSuccess(callback: () -> Unit) {
        onRegisterSuccess = callback
    }

    fun register() {
        // Reset de l'erreur précédente
        error.value = null

        // Validation des champs
        when {
            email.value.isBlank() -> {
                error.value = "L'email est requis"
                return
            }
            !isValidEmail(email.value) -> {
                error.value = "L'email n'est pas valide"
                return
            }
            password.value.isBlank() -> {
                error.value = "Le mot de passe est requis"
                return
            }
            confirmPassword.value.isBlank() -> {
                error.value = "La confirmation du mot de passe est requise"
                return
            }
            password.value != confirmPassword.value -> {
                error.value = "Les mots de passe ne correspondent pas"
                return
            }
            password.value.length < 6 -> {
                error.value = "Le mot de passe doit contenir au moins 6 caractères"
                return
            }
        }

        isLoading.value = true

        registerRepository.registerUser(email.value, password.value, confirmPassword.value) { success, errorMessage ->
            isLoading.value = false

            if (success) {
                // Reset des champs en cas de succès
                clearFields()
                onRegisterSuccess?.invoke()
            } else {
                error.value = errorMessage ?: "Erreur lors de la création du compte"
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun clearFields() {
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
        error.value = null
    }
}