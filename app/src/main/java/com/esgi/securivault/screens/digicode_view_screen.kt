package com.esgi.securivault.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var isViewMode by remember { mutableStateOf(true) }
    var showCurrentCode by remember { mutableStateOf(false) }

    // État local pour le code actuel qui sera mis à jour
    var currentCode by remember { mutableStateOf("1234") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setSuitcaseId("valise002")
    }

    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        if (uiState.successMessage != null || uiState.errorMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }

    // Observer les changements de code réussis
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null && newCode.isNotEmpty()) {
            currentCode = newCode // Met à jour le code affiché
            isViewMode = true // Retourne automatiquement en mode consultation
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A237E),
                        Color(0xFF3F51B5),
                        Color(0xFF5C6BC0)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Header
            CodeHeader()

            Spacer(modifier = Modifier.height(40.dp))

            // Mode selector
            ModeSelector(
                isViewMode = isViewMode,
                onModeChange = { isViewMode = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isViewMode) {
                // Mode consultation
                CodeViewSection(
                    currentCode = currentCode,
                    showCurrentCode = showCurrentCode,
                    onToggleVisibility = { showCurrentCode = !showCurrentCode }
                )
            } else {
                // Mode modification
                CodeChangeSection(
                    newCode = newCode,
                    confirmCode = confirmCode,
                    showPassword = showPassword,
                    onNewCodeChange = { newCode = it },
                    onConfirmCodeChange = { confirmCode = it },
                    onToggleVisibility = { showPassword = !showPassword },
                    onSaveCode = {
                        if (newCode == confirmCode && newCode.isNotEmpty()) {
                            viewModel.changeCode(newCode)
                            // Note: currentCode sera mis à jour dans LaunchedEffect ci-dessus
                            // après confirmation du succès
                        }
                    },
                    isLoading = uiState.isLoading,
                    isValid = newCode == confirmCode && newCode.isNotEmpty()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Messages d'état
            StatusMessages(uiState)
        }
    }
}

@Composable
private fun CodeHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Pin,
            contentDescription = "Code PIN",
            modifier = Modifier.size(48.dp),
            tint = Color(0xFF64FFDA)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Code d'Accès",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Gérez votre code de sécurité",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ModeSelector(
    isViewMode: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            ModeButton(
                text = "Consulter",
                icon = Icons.Default.Visibility,
                isSelected = isViewMode,
                onClick = { onModeChange(true) },
                modifier = Modifier.weight(1f)
            )

            ModeButton(
                text = "Modifier",
                icon = Icons.Default.Edit,
                isSelected = !isViewMode,
                onClick = { onModeChange(false) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ModeButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF64FFDA) else Color.Transparent,
            contentColor = if (isSelected) Color.Black else Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun CodeViewSection(
    currentCode: String,
    showCurrentCode: Boolean,
    onToggleVisibility: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Code Actuel",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Affichage style carte bancaire
            BankCardCodeDisplay(
                code = currentCode,
                isVisible = showCurrentCode
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onToggleVisibility,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64FFDA),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (showCurrentCode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (showCurrentCode) "Masquer le Code" else "Afficher le Code",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun BankCardCodeDisplay(
    code: String,
    isVisible: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        code.forEachIndexed { index, digit ->
            Card(
                modifier = Modifier.size(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = if (isVisible) digit.toString() else "•",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isVisible) 24.sp else 32.sp
                        ),
                        color = Color(0xFF64FFDA)
                    )
                }
            }
        }
    }
}

@Composable
private fun CodeChangeSection(
    newCode: String,
    confirmCode: String,
    showPassword: Boolean,
    onNewCodeChange: (String) -> Unit,
    onConfirmCodeChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onSaveCode: () -> Unit,
    isLoading: Boolean,
    isValid: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Nouveau Code",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = newCode,
                onValueChange = onNewCodeChange,
                label = { Text("Nouveau code (4 chiffres)", color = Color.White.copy(alpha = 0.7f)) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                trailingIcon = {
                    IconButton(onClick = onToggleVisibility) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) "Masquer" else "Afficher",
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF64FFDA),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                )
            )

            OutlinedTextField(
                value = confirmCode,
                onValueChange = onConfirmCodeChange,
                label = { Text("Confirmer le code", color = Color.White.copy(alpha = 0.7f)) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth(),
                isError = confirmCode.isNotEmpty() && newCode != confirmCode,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF64FFDA),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    errorBorderColor = Color.Red
                )
            )

            if (confirmCode.isNotEmpty() && newCode != confirmCode) {
                Text(
                    text = "⚠️ Les codes ne correspondent pas",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Button(
                onClick = onSaveCode,
                enabled = !isLoading && isValid,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64FFDA),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Enregistrer le Code",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusMessages(uiState: Any) {
    // Remplacer par les propriétés réelles de votre UiState
    val successMessage: String? = null // uiState.successMessage
    val errorMessage: String? = null // uiState.errorMessage

    successMessage?.let { message ->
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Green.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    errorMessage?.let { message ->
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Red.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

