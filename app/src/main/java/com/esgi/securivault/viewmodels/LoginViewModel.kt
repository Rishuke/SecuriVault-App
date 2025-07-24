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
// AJOUTE CETTE IMPORT
import com.esgi.securivault.repository.LoginRepository

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {
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
                            println("✅ FIREBASE TOKEN = $idToken")


                            loginRepository.loginUser(email.value, password.value) { serverToken ->
                                if (serverToken != null) {
                                    println("✅ SERVER TOKEN = $serverToken")
                                    // Stocke le token serveur si nécessaire
                                    loginSuccess.value = true
                                } else {
                                    error.value = "Erreur de connexion au serveur"
                                }
                                isLoading.value = false
                            }

                        } else {
                            error.value = "Token Firebase introuvable"
                            isLoading.value = false
                        }
                    }
                    ?.addOnFailureListener { e ->
                        error.value = "Erreur token Firebase : ${e.localizedMessage}"
                        isLoading.value = false
                    }
            }
            .addOnFailureListener { e ->
                error.value = e.localizedMessage ?: "Erreur connexion Firebase"
                isLoading.value = false
            }
    }

    fun launchGoogleSignIn(launcher: ActivityResultLauncher<Intent>, context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(com.esgi.securivault.R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        isLoading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Pour Google aussi, tu peux ajouter l'appel au serveur ici si nécessaire
                    val firebaseUser = task.result?.user
                    firebaseUser?.getIdToken(true)?.addOnSuccessListener { tokenResult ->
                        val firebaseToken = tokenResult.token
                        if (firebaseToken != null) {
                            // Optionnel : appel serveur pour Google login aussi
                            loginSuccess.value = true
                        }
                        isLoading.value = false
                    }
                } else {
                    error.value = "Erreur connexion Google"
                    isLoading.value = false
                }
            }
    }

    fun setError(message: String) {
        error.value = message
    }
}