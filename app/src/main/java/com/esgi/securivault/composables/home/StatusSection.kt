package com.esgi.securivault.composables.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp



@Composable
fun StatusSection(
    isConnected: Boolean,
    batteryLevel: String,
    securityStatus: String,
    lastUpdateTime: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.06f)
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
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusIndicator(
                    icon = Icons.Default.Security,
                    label = "Sécurité",
                    status = securityStatus,
                    color = if (securityStatus.contains("Mouvement") || securityStatus.contains("⚠️"))
                        Color(0xFFFF5722) else Color(0xFF4CAF50)
                )

                StatusIndicator(
                    icon = Icons.Default.Wifi,
                    label = "Connexion",
                    status = if (isConnected) "Connecté" else "Déconnecté",
                    color = if (isConnected) Color(0xFF2196F3) else Color(0xFFFF5722)
                )

                StatusIndicator(
                    icon = Icons.Default.Battery6Bar,
                    label = "Batterie",
                    status = batteryLevel,
                    color = Color(0xFF64FFDA)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Dernière mise à jour: $lastUpdateTime",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
