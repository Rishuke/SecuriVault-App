package com.esgi.securivault.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0D1B2A),
                        Color(0xFF1B263B),
                        Color(0xFF415A77)
                    ),
                    radius = 1200f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header avec titre
            WelcomeSection()

            Spacer(modifier = Modifier.height(60.dp))

            // Coffre-fort principal
            SafeVaultDisplay()

            Spacer(modifier = Modifier.height(40.dp))

            // Statut et informations
            StatusSection()

            Spacer(modifier = Modifier.weight(1f))

            // Footer avec sécurité
            SecurityFooter()
        }
    }
}

@Composable
private fun WelcomeSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SecuriVault",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp
            ),
            color = Color(0xFF64FFDA),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Votre coffre-fort numérique",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SafeVaultDisplay() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        // Cercle externe avec effet de lueur
        Box(
            modifier = Modifier
                .size(280.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    ambientColor = Color(0xFF64FFDA),
                    spotColor = Color(0xFF64FFDA)
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
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
                .size(220.dp)
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
                    .size(80.dp)
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
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Cadenas",
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF64FFDA)
                )
            }

            // Indicateurs autour du coffre
            repeat(8) { index ->
                val angle = (360f / 8) * index
                val isActive = index < 3 // Simule des indicateurs actifs

                Box(
                    modifier = Modifier
                        .offset(
                            x = (95 * kotlin.math.cos(Math.toRadians(angle.toDouble()))).dp,
                            y = (95 * kotlin.math.sin(Math.toRadians(angle.toDouble()))).dp
                        )
                        .align(Alignment.Center)
                        .size(8.dp)
                        .background(
                            color = if (isActive) Color(0xFF64FFDA) else Color.White.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun StatusSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "État du Système",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusIndicator(
                    icon = Icons.Default.Security,
                    label = "Sécurité",
                    status = "Active",
                    color = Color(0xFF4CAF50)
                )

                StatusIndicator(
                    icon = Icons.Default.Bluetooth,
                    label = "Connexion",
                    status = "Connecté",
                    color = Color(0xFF2196F3)
                )

                StatusIndicator(
                    icon = Icons.Default.Battery6Bar,
                    label = "Batterie",
                    status = "90%",
                    color = Color(0xFF64FFDA)
                )
            }
        }
    }
}

@Composable
private fun StatusIndicator(
    icon: ImageVector,
    label: String,
    status: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = color.copy(alpha = 0.2f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = color
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Text(
            text = status,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = color,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SecurityFooter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = "Sécurisé",
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF64FFDA)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Chiffrement AES-256 • Sécurisé et Protégé",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}