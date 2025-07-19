// screens/BuzzerSoundScreen.kt
package com.esgi.securivault.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.securivault.viewmodels.SuitcaseViewModel
import kotlin.math.roundToInt

@Composable
fun BuzzerSoundScreen(
    modifier: Modifier = Modifier,
    viewModel: SuitcaseViewModel = hiltViewModel()
) {
    var frequency by remember { mutableFloatStateOf(1000f) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setSuitcaseId("your_suitcase_id") // À remplacer par l'ID réel
    }

    // Mettre à jour la fréquence depuis l'état si disponible
    LaunchedEffect(uiState.suitcase?.buzzerFreq) {
        uiState.suitcase?.buzzerFreq?.let { freq ->
            frequency = freq.toFloat()
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
            .background(color = Color.Yellow)
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
                    text = "Configuration Buzzer",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )

                Text(
                    text = "Fréquence: ${frequency.roundToInt()} Hz",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                Slider(
                    value = frequency,
                    onValueChange = { frequency = it },
                    valueRange = 200f..5000f,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("200 Hz", style = MaterialTheme.typography.bodySmall)
                    Text("5000 Hz", style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = {
                        viewModel.updateBuzzerFrequency(frequency.roundToInt())
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Appliquer la Fréquence")
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