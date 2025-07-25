package com.esgi.securivault.composables.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SafeVaultDisplay(currentSpeed: Double) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(vertical = 20.dp)
    ) {
        val isShaking = currentSpeed >= 1.15
        val mainColor = if (isShaking) Color(0xFFFF5722) else Color(0xFF64FFDA)
        val secondaryColor = if (isShaking) Color(0xFF8B0000) else Color(0xFF1E3A5F)

        Box(
            modifier = Modifier
                .size(300.dp)
                .shadow(
                    elevation = if (isShaking) 35.dp else 25.dp,
                    shape = CircleShape,
                    ambientColor = mainColor,
                    spotColor = mainColor
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = if (isShaking) listOf(
                            Color(0xFF5F1E1E),
                            Color(0xFF6B2D2D),
                            Color(0xFF470F0F)
                        ) else listOf(
                            Color(0xFF1E3A5F),
                            Color(0xFF2D4A6B),
                            Color(0xFF0F2C47)
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Cercle interne du coffre
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF263238),
                            Color(0xFF37474F),
                            Color(0xFF1C2833)
                        )
                    ),
                    shape = CircleShape
                )
        ) {
            // Poignée du coffre
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(90.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF546E7A),
                                Color(0xFF37474F),
                                Color(0xFF263238)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isShaking) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = "Cadenas",
                    modifier = Modifier.size(36.dp),
                    tint = mainColor
                )
            }

            // Indicateurs animés autour du coffre
            repeat(12) { index ->
                val angle = (360f / 12) * index
                val isActive = if (isShaking) (index < 8) else (index < 4)
                val size = if (isShaking) 14.dp else 10.dp

                Box(
                    modifier = Modifier
                        .offset(
                            x = (105 * kotlin.math.cos(Math.toRadians(angle.toDouble()))).dp,
                            y = (105 * kotlin.math.sin(Math.toRadians(angle.toDouble()))).dp
                        )
                        .align(Alignment.Center)
                        .size(size)
                        .background(
                            color = if (isActive) mainColor else Color.White.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
            }
        }

        // Affichage des données sous le coffre
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${String.format("%.3f", currentSpeed)} G",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = mainColor
            )
            Text(
                text = "Accélération",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}
