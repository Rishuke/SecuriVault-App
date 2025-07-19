// screens/DigicodeViewScreen.kt
package com.esgi.securivault.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.securivault.viewmodels.SuitcaseViewModel


@Composable
fun DigicodeViewScreen(
    modifier: Modifier = Modifier,
    viewModel: SuitcaseViewModel = hiltViewModel()
) {
    var newCode by remember { mutableStateOf("") }
    var confirmCode by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setSuitcaseId("your_suitcase_id") // À remplacer par l'ID réel
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
            .background(color = Color.Blue)
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
                    text = "Changer le Code",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )

                OutlinedTextField(
                    value = newCode,
                    onValueChange = { newCode = it },
                    label = { Text("Nouveau code") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showPassword) "Cacher" else "Afficher"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmCode,
                    onValueChange = { confirmCode = it },
                    label = { Text("Confirmer le code") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier.fillMaxWidth(),
                    isError = confirmCode.isNotEmpty() && newCode != confirmCode
                )

                if (confirmCode.isNotEmpty() && newCode != confirmCode) {
                    Text(
                        text = "Les codes ne correspondent pas",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = {
                        if (newCode == confirmCode && newCode.isNotEmpty()) {
                            viewModel.changeCode(newCode)
                            newCode = ""
                            confirmCode = ""
                        }
                    },
                    enabled = !uiState.isLoading && newCode == confirmCode && newCode.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Changer le Code")
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