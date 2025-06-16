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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LedColorScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize().background(color = Color.Magenta),
        contentAlignment = Alignment.Center// Use your desired color
    ) {

        Text("Led Color Screen",
            modifier = Modifier.padding(50.dp),
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge)
    }
}

@Preview
@Composable
fun LedColorScreenPreview() {
    Scaffold { innerPadding ->
        LedColorScreen(
            modifier = Modifier.padding(innerPadding)
        )

    }
}