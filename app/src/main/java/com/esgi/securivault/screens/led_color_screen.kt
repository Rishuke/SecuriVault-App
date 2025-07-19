// screens/LedColorScreen.kt
package com.esgi.securivault.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.securivault.viewmodels.SuitcaseViewModel


@Composable
fun LedColorScreen(
    modifier: Modifier = Modifier,
    viewModel: SuitcaseViewModel = hiltViewModel()
) {
    val availableColors = listOf(
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "purple" to Color.Magenta,
        "cyan" to Color.Cyan,
        "white" to Color.White,
        "orange" to Color(0xFFFFA500)
    )

    var selectedColor by remember { mutableStateOf("red") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setSuitcaseId("your_suitcase_id") // À remplacer par l'ID réel
    }

    // Mettre à jour la couleur sélectionnée depuis l'état si disponible
    LaunchedEffect(uiState.suitcase?.ledColor) {
        uiState.suitcase?.ledColor?.let { color ->
            selectedColor = color
        }
    }

    // Gérer l'affichage des messages
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        if (uiState.successMessage != null || uiState.errorMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Magenta)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Couleur LED",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )

                Text(
                    text = "Couleur sélectionnée: ${selectedColor.uppercase()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(availableColors) { (colorName, colorValue) ->
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(colorValue)
                                .border(
                                    width = if (selectedColor == colorName) 4.dp else 2.dp,
                                    color = if (selectedColor == colorName) Color.Black else Color.Gray,
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedColor = colorName
                                }
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.updateLedColor(selectedColor)
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Appliquer la Couleur")
                    }
                }

                // Messages d'état
                uiState.successMessage?.let { message ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = message,
                            color = Color.Green.copy(red = 0.2f),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                uiState.errorMessage?.let { message ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = message,
                            color = Color.Red,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DigicodeViewScreenPreview() {
    Scaffold { innerPadding ->
        DigicodeViewScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Preview
@Composable
fun BuzzerSoundScreenPreview() {
    Scaffold { innerPadding ->
        BuzzerSoundScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Preview
@Composable
fun LedColorScreenPreview() {
    Scaffold { innerPadding ->
        LedColorScreen(modifier = Modifier.padding(innerPadding))
    }
}