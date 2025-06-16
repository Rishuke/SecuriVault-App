package com.esgi.securivault.destinations


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

interface AppNavigation {
    val icon : ImageVector
    val name : String
    val route : String
}

object HomeScreen : AppNavigation {
    override val icon = Icons.Filled.Home
    override val name = "Home"
    override val route = "home/screen"
}

object DigicodeScreen : AppNavigation {
    override val icon = Icons.Filled.Lock
    override val name = "Digicode"
    override val route = "digicode/screen"
}

object LuggageMapScreen : AppNavigation {
    override val icon = Icons.Filled.LocationOn
    override val name = "Luggage"
    override val route = "luggage/screen"
}

object LedColorScreen : AppNavigation {
    override val icon = Icons.Filled.Star
    override val name = "Led"
    override val route = "led/screen"
}

object BuzzerSoundScreen : AppNavigation {
    override val icon = Icons.Filled.Notifications
    override val name = "Buzzer"
    override val route = "buzzer/screen"
}

val navTab = listOf(
    HomeScreen,
    DigicodeScreen,
    LuggageMapScreen,
    LedColorScreen,
    BuzzerSoundScreen
)