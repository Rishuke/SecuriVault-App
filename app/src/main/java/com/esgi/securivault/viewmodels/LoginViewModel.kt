// LoginViewModel.kt
package com.esgi.securivault.viewmodels

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val error = mutableStateOf<String?>(null)
    val isLoading = mutableStateOf(false)
    val loginSuccess = mutableStateOf(false)

    fun login() {
        isLoading.value = true
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.value, password.value)
            .addOnSuccessListener { authResult ->
                authResult.user?.getIdToken(true)
                    ?.addOnSuccessListener { tokenResult ->
                        val idToken = tokenResult.token
                        if (idToken != null) {
                            println("✅ TOKEN = $idToken")
                            // Ici appelle ton repository pour faire la requête HTTP avec le token
                            loginSuccess.value = true
                        } else {
                            error.value = "Token introuvable"
                        }
                        isLoading.value = false
                    }
                    ?.addOnFailureListener { e ->
                        error.value = "Erreur token : ${e.localizedMessage}"
                        isLoading.value = false
                    }
            }
            .addOnFailureListener { e ->
                error.value = e.localizedMessage ?: "Erreur connexion"
                isLoading.value = false
            }
    }


    fun launchGoogleSignIn(launcher: ActivityResultLauncher<Intent>, context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(com.esgi.securivault.R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        isLoading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    loginSuccess.value = true
                } else {
                    error.value = "Erreur connexion Google"
                }
            }
    }

    fun setError(message: String) {
        error.value = message
    }
}
