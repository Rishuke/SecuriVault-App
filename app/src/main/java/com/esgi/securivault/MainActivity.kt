package com.esgi.securivault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.esgi.securivault.composables.NavTopBar
import com.esgi.securivault.destinations.HomeScreen
import com.esgi.securivault.destinations.navTab
import com.esgi.securivault.screens.authent.LoginScreen
import com.esgi.securivault.screens.authent.RegisterScreen
import com.esgi.securivault.ui.theme.NavigationComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isAuthenticated by remember { mutableStateOf(false) }
            var screen by remember { mutableStateOf("login") }

            when {
                !isAuthenticated && screen == "login" -> {
                    LoginScreen(
                        onLoginSuccess = { isAuthenticated = true },
                        onNavigateToRegister = { screen = "register" }
                    )
                }

                !isAuthenticated && screen == "register" -> {
                    RegisterScreen(
                        onRegisterSuccess = { screen = "login" },
                        onNavigateToLogin = { screen = "login" }
                    )
                }

                else -> {
                    SimpleNavigation(
                        modifier = Modifier.fillMaxSize(),
                        onDeconnexion = {
                            isAuthenticated = false
                            screen = "login"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleNavigation(modifier: Modifier = Modifier, onDeconnexion: () -> Unit = {}) {
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
                    onBackPressed =
                    { screen ->
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
            Button(
                onClick = onDeconnexion,
                modifier = Modifier.padding(innerPadding)
            ) { }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview( modifier: Modifier = Modifier) {
    NavigationComposeTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SimpleNavigation(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}