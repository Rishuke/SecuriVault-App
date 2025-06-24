package com.esgi.securivault.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.esgi.securivault.AppNavHost
import com.esgi.securivault.composables.NavTopBar
import com.esgi.securivault.destinations.navTab
import com.esgi.securivault.navigateToTop
import com.esgi.securivault.ui.theme.NavigationComposeTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()

    val currentDestination = navTab.find { it.route == currentBackStack?.destination?.route } ?: com.esgi.securivault.destinations.HomeScreen

    Box(
        modifier = Modifier.fillMaxSize().background(color = Color.Red),
        contentAlignment = Alignment.Center// Use your desired color
    ) {

        Text("Home Screen",
            modifier = Modifier.padding(50.dp),
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )
    }

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

        }

    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    Scaffold { innerPadding ->
        HomeScreen(
            modifier = Modifier.padding(innerPadding)
        )

    }
}