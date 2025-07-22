package com.esgi.securivault.screens.authent

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.esgi.securivault.networking.AuthInterceptor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


fun launchGoogleSignIn(launcher: ActivityResultLauncher<Intent>, context: Context) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(com.esgi.securivault.R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)
}

fun handleGoogleSignInResult(data: Intent?) {
    try {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)

        // Récupérer l'ID token
        val idToken = account.idToken

        if (idToken != null) {
            // Stocker le token dans l'interceptor
            AuthInterceptor.setToken(idToken)

            // Optionnel : sauvegarder le token localement
            // SharedPreferences ou autre système de persistence
        }

    } catch (e: ApiException) {
        // Gérer l'erreur
        e.printStackTrace()
    }
}

fun signOut(context: Context) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    googleSignInClient.signOut().addOnCompleteListener {
        // Effacer le token
        AuthInterceptor.clearToken()
    }
}