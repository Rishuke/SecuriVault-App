package com.esgi.securivault.screens
import androidx.compose.ui.res.stringResource
import com.esgi.securivault.R

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.esgi.securivault.composables.home.SafeVaultDisplay
import com.esgi.securivault.composables.home.SecurityFooter
import com.esgi.securivault.composables.home.StatusSection
import com.esgi.securivault.composables.home.WelcomeSection
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }

    // États pour les alertes et données
    var showMovementAlert by remember { mutableStateOf(false) }
    var currentSpeed by remember { mutableDoubleStateOf(0.0) }
    var isConnected by remember { mutableStateOf(false) }
    var batteryLevel by remember { mutableStateOf("--") }
    var securityStatus by remember { mutableStateOf("Connexion en cours...") }
    var lastUpdateTime by remember { mutableStateOf("Jamais") }


    // Configuration Firestore
    val db = FirebaseFirestore.getInstance()
    val suitcaseRef = db.collection("suitcases").document("valise002")

    fun parseSpeedValue(speedValue: Any?): Double {
        return when (speedValue) {
            is Double -> speedValue
            is Float -> speedValue.toDouble()
            is Long -> speedValue.toDouble()
            is Int -> speedValue.toDouble()
            is String -> speedValue.toDoubleOrNull() ?: 0.0
            null -> 0.0
            else -> {
                try {
                    speedValue.toString().toDoubleOrNull() ?: 0.0
                } catch (e: Exception) {
                    0.0
                }
            }
        }
    }

    // Listener Firestore
    DisposableEffect(Unit) {
        val listener = suitcaseRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
            if (error != null) {
                Log.e("FirestoreListener", "Erreur du listener", error)
                isConnected = false
                securityStatus = "Erreur de connexion"
                return@addSnapshotListener
            }

            snapshot?.let { doc ->
                if (doc.exists()) {
                    val data = doc.data
                    data?.let { docData ->
                        // Parse de la vitesse
                        val speed = parseSpeedValue(docData["speed"])
                        currentSpeed = speed

                        // Gestion des alertes de mouvement
                        val movementThreshold = 1.15
                        if (speed >= movementThreshold && !showMovementAlert) {
                            showMovementAlert = true
                        }

                        // Mise à jour des statuts
                        isConnected = true
                        securityStatus = if (speed >= movementThreshold) {
                            context.getString(R.string.motion_alert_title)
                        } else {
                            context.getString(R.string.status_secure)

                        }

                        // Mise à jour du timestamp
                        lastUpdateTime = java.text.SimpleDateFormat(
                            "HH:mm:ss",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date())

                        // Mise à jour du niveau de batterie
                        docData["battery"]?.let { battery ->
                            batteryLevel = when (battery) {
                                is Number -> "${battery.toInt()}%"
                                is String -> battery
                                else -> battery.toString()
                            }
                        }


                    }
                } else {
                    isConnected = false
                    securityStatus = "Document introuvable"
                }
            }
        }

        onDispose {
            listener.remove()
        }
    }

    // Auto-dismiss de l'alerte après 5 secondes
    LaunchedEffect(showMovementAlert) {
        if (showMovementAlert) {
            delay(5000)
            showMovementAlert = false
        }
    }

    // Gestion du bouton retour
    BackHandler {
        navController.navigate("login") {
            popUpTo(0) { inclusive = true }
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    },
                navigationIcon = {

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    // Indicateur de connexion
                    Icon(
                        imageVector = if (isConnected) Icons.Default.CloudDone else Icons.Default.CloudOff,
                        contentDescription = if (isConnected) "Connecté" else "Déconnecté",
                        tint = if (isConnected) Color(0xFF4CAF50) else Color(0xFFFF5722),
                        modifier = Modifier.size(20.dp)
                    )
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Se déconnecter") },
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.height(68.dp)
            )
        },
        containerColor = Color.Transparent,
        snackbarHost = {
            // Alerte de mouvement personnalisée
            if (showMovementAlert) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF5722).copy(alpha = 0.95f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alerte",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.motion_alert_title),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Text(
                                text = stringResource(
                                    R.string.motion_alert_acceleration,
                                    String.format("%.3f", currentSpeed)
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = stringResource(R.string.motion_alert_time, lastUpdateTime),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        IconButton(
                            onClick = { showMovementAlert = false }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0D1B2A),
                            Color(0xFF1B263B),
                            Color(0xFF415A77)
                        ),
                        radius = 1400f
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { WelcomeSection() }
                item { Spacer(Modifier.height(20.dp)) }
                item { SafeVaultDisplay(currentSpeed) }
                item { Spacer(Modifier.height(16.dp)) }
                item { StatusSection(isConnected, batteryLevel, securityStatus, lastUpdateTime) }


                item { Spacer(Modifier.height(20.dp)) }
                item { SecurityFooter() }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        HomeScreen(modifier = Modifier, navController = navController)
    }
}