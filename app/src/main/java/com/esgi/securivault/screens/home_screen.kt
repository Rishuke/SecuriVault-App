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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.delay
import com.esgi.securivault.composables.home.SafeVaultDisplay
import com.esgi.securivault.composables.home.SecurityFooter
import com.esgi.securivault.composables.home.StatusSection
import com.esgi.securivault.composables.home.WelcomeSection


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val activity = context as? Activity
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
                                Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout")
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

@Composable
private fun GpsSection(latitude: Double, longitude: Double, gpsSpeed: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "GPS",
                    tint = Color(0xFF64FFDA),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.gps_section_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White
                )
            }

            Text(
                text = stringResource(R.string.gps_latitude, String.format("%.6f", latitude)),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = stringResource(R.string.gps_longitude, String.format("%.6f", longitude)),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            if (gpsSpeed > 0) {
                Text(
                    text = stringResource(R.string.gps_speed, String.format("%.2f", gpsSpeed)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64FFDA)
                )
            }
        }
    }
}

@Composable
private fun DebugSection(
    debugInfo: String,
    firestoreError: String?,
    rawSpeedValue: Any?,
    currentSpeed: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BugReport,
                    contentDescription = "Debug",
                    tint = Color(0xFF64FFDA),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.debug_section_title),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF64FFDA)
                )
            }

            Text(
                text = debugInfo,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = stringResource(R.string.debug_raw_value, rawSpeedValue.toString()),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = stringResource(R.string.debug_final_speed, String.format("%.3f", currentSpeed)),
                style = MaterialTheme.typography.bodySmall,
                color = if (currentSpeed >= 1.15) Color(0xFFFF5722) else Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            if (firestoreError != null) {
                Text(
                    text = "$firestoreError",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFF5722)
                )
            }
        }
    }
}

@Composable
private fun WelcomeSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            ),
            color = Color(0xFF64FFDA),
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun SafeVaultDisplay(currentSpeed: Double) {
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

@Composable
private fun StatusSection(
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
                text = stringResource(R.string.system_status),
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
                    label = stringResource(R.string.label_security),
                    status = securityStatus,
                    color = if (securityStatus.contains("Mouvement") || securityStatus.contains("⚠️"))
                        Color(0xFFFF5722) else Color(0xFF4CAF50)
                )

                StatusIndicator(
                    icon = Icons.Default.Wifi,
                    label = stringResource(R.string.label_connection),
                    status = if (isConnected) stringResource(R.string.status_connected) else stringResource(R.string.status_disconnected),
                    color = if (isConnected) Color(0xFF2196F3) else Color(0xFFFF5722)
                )

                StatusIndicator(
                    icon = Icons.Default.Battery6Bar,
                    label = stringResource(R.string.label_battery),
                    status = batteryLevel,
                    color = Color(0xFF64FFDA)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.last_update, lastUpdateTime),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
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
                .size(52.dp)
                .background(
                    color = color.copy(alpha = 0.2f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(26.dp),
                tint = color
            )
        }

        Spacer(modifier = Modifier.height(8.dp))



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
            modifier = Modifier.size(18.dp),
            tint = Color(0xFF64FFDA)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.footer_secure),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )
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