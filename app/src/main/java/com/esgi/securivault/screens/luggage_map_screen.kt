package com.esgi.securivault.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun LuggageMapScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Configuration OSMDroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = "SecuriVault/1.0"
    }

    // Carte OSM
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                // Position initiale sur Singapour
                val singaporePoint = GeoPoint(1.3521, 103.8198)
                controller.setZoom(12.0)
                controller.setCenter(singaporePoint)

                // Ajouter un marqueur par défaut
                val marker = Marker(this)
                marker.position = singaporePoint
                marker.title = "Singapour"
                marker.snippet = "Position par défaut"
                overlays.add(marker)
            }
        },
        update = { view ->
            view.onResume()
        }
    )
}



@Preview
@Composable
fun LuggageMapScreenPreview() {
    MaterialTheme {
        Scaffold { innerPadding ->
            LuggageMapScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}