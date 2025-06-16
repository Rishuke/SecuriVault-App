package com.esgi.securivault

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.esgi.securivault.destinations.HomeScreen
import com.esgi.securivault.destinations.DigicodeScreen
import com.esgi.securivault.destinations.LuggageMapScreen
import com.esgi.securivault.destinations.LedColorScreen
import com.esgi.securivault.destinations.BuzzerSoundScreen
import com.esgi.securivault.screens.HomeScreen
import com.esgi.securivault.screens.DigicodeViewScreen
import com.esgi.securivault.screens.LuggageMapScreen
import com.esgi.securivault.screens.LedColorScreen
import com.esgi.securivault.screens.BuzzerSoundScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination =
        HomeScreen.route,
        modifier = modifier
    ) {
        composable(route = HomeScreen.route) {
            HomeScreen(

            )
        }
        composable(route = DigicodeScreen.route) {
            DigicodeViewScreen(
            )
        }

        composable(route = LuggageMapScreen.route) {
            LuggageMapScreen(

            )
        }
        composable(route = LedColorScreen.route) {
            LedColorScreen(

            )
        }


        composable(route = BuzzerSoundScreen.route) {
            BuzzerSoundScreen(

            )
        }


    }

}


// 1 - Ne pas recréer plusieurs fois le meme ecran si on clique plusieurs fois sur le meme meme NavTab
// 2 - Restaurer le dernier etat de l 'ecran ou on va
// 3 - Sauvegarder le dernier etat de l'ecan duquel on part

fun NavHostController.navigateToTop(route : String) {
    this.navigate(route) {
        popUpTo(
            this@navigateToTop.graph.findStartDestination().id // Nettoyer lap ile jusqu'à l'écran de "startDestination"
        ) {
            saveState = true // Sauvegarde de l'état du screen (scroll, saisie de texte, etc.)
        }
        launchSingleTop = true // Pour ne pas recréer l'ecran sur lequel on est
        restoreState = true // Restore le dernier etat de l'ecran ou je vais naviguer

    }
}