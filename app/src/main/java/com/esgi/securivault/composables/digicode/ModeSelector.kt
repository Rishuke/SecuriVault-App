package com.esgi.securivault.composables.digicode

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ModeSelector(
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