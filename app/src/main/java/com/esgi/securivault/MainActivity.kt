package com.esgi.securivault

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.esgi.securivault.composables.NavTopBar
import com.esgi.securivault.destinations.HomeScreen
import com.esgi.securivault.destinations.navTab
import com.esgi.securivault.screens.authent.LoginScreen
import com.esgi.securivault.screens.authent.RegisterScreen
import com.esgi.securivault.ui.theme.NavigationComposeTheme
import com.esgi.securivault.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isAuthenticated by remember { mutableStateOf(false) }
            var screen by remember { mutableStateOf("login") }

            val viewModel: LoginViewModel = hiltViewModel()

            val googleSignInLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account = task.result
                    account.idToken?.let { idToken ->
                        viewModel.firebaseAuthWithGoogle(idToken)
                    }
                }
            }

            if (viewModel.loginSuccess.value) {
                isAuthenticated = true
            }

            when {
                !isAuthenticated && screen == "login" -> {
                    LoginScreen(
                        onLoginSuccess = { isAuthenticated = true },
                        onNavigateToRegister = { screen = "register" },
                        onGoogleLogin = {
                            viewModel.launchGoogleSignIn(googleSignInLauncher, this)
                        }
                    )
                }
                !isAuthenticated && screen == "register" -> {
                    RegisterScreen(
                        onRegisterSuccess = { screen = "login" },
                        onNavigateToLogin = { screen = "login" }
                    )
                }
                isAuthenticated -> {
                    SimpleNavigation()
                }
            }
        }
    }
}

@Composable
fun SimpleNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()

    val currentDestination = navTab.find { it.route == currentBackStack?.destination?.route } ?: HomeScreen

    NavigationComposeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavTopBar(
                    title = "Navigation Compose",
                    destination = navTab,
                    onBackPressed = { screen ->
                        navController.navigateToTop(screen.route)
                    },
                    currentDestination = currentDestination
                )
            }
        ) { innerPadding ->
            AppNavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
            )
        }
    }
}
