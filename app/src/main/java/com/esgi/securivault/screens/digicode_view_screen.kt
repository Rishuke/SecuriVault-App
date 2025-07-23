package com.esgi.securivault.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.securivault.composables.digicode.CodeChangeSection
import com.esgi.securivault.viewmodels.SuitcaseViewModel
import com.esgi.securivault.composables.digicode.CodeHeader
import com.esgi.securivault.composables.digicode.CodeViewSection
import com.esgi.securivault.composables.digicode.ModeSelector
import com.esgi.securivault.composables.digicode.StatusMessages

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

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null && newCode.isNotEmpty()) {
            currentCode = newCode
            isViewMode = true
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            CodeHeader()

            Spacer(modifier = Modifier.height(24.dp))

            ModeSelector(
                isViewMode = isViewMode,
                onModeChange = { isViewMode = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isViewMode) {
                CodeViewSection(
                    currentCode = currentCode,
                    showCurrentCode = showCurrentCode,
                    onToggleVisibility = { showCurrentCode = !showCurrentCode }
                )
            } else {
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
                        }
                    },
                    isLoading = uiState.isLoading,
                    isValid = newCode == confirmCode && newCode.isNotEmpty()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            StatusMessages(uiState)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}








