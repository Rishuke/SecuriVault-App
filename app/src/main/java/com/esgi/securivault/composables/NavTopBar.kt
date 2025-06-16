package com.esgi.securivault.composables


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esgi.securivault.destinations.AppNavigation
import com.esgi.securivault.destinations.HomeScreen

private var tabHeight = 56.dp
@Composable
fun NavTopBar(
    title: String,
    destination: List<AppNavigation>,
    onBackPressed: (AppNavigation) -> Unit,
    currentDestination: AppNavigation,
) {

    Surface (
        modifier = Modifier.fillMaxWidth(),
    ){
        Row (
            horizontalArrangement =  Arrangement.SpaceEvenly,
        ){
            destination.forEach { screen ->
                CustomNavTab(
                    title = screen.name,
                    icon = screen.icon,
                    onSelected = {
                        if (currentDestination != screen) {
                            onBackPressed(screen)
                        }
                    },
                    selected = currentDestination == screen
                )
            }
        }
    }

}

@Composable
fun CustomNavTab(
    title: String,
    icon : ImageVector,
    onSelected: () -> Unit,
    selected: Boolean) {

    val tabTintColor = if (selected) {
        Color.Black
    } else {
        Color.Gray
    }

    Column (
        modifier =  Modifier.padding(16.dp).height(tabHeight)
            .selectable(
            selected = selected,
            onClick = onSelected,
            role = Role.Tab,),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tabTintColor,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            color = tabTintColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

@Preview
@Composable
fun CustomNavBarPreview() {
    CustomNavTab(
        title = "Basic Nav Bar",
        icon = Icons.Default.Person,
        onSelected = {},
        selected = true

    )
}

@Preview
@Composable
fun CustomNavTopBarPreview() {
    NavTopBar(
        title = "Basic Nav Bar",
        destination = listOf(),
        onBackPressed = {},
        currentDestination = HomeScreen
    )
}