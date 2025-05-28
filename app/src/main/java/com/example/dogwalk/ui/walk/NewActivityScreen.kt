package com.example.dogwalk.ui.walk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlin.math.*
import android.location.Location
import android.content.pm.PackageManager
import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.example.dogwalk.WalkViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import com.example.dogwalk.ui.components.TopBarWithLogo



@Parcelize
data class WalkDraft(
    val route: List<LatLng>,
    val startTime: Long,
    val duration: Int,
    val distance: Double
) : Parcelable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewActivityScreen(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val walkViewModel: WalkViewModel = viewModel()

    var isTracking by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(0) }
    var distance by remember { mutableStateOf(0f) }
    var locations by remember { mutableStateOf(listOf<Location>()) }
    var startTime by remember { mutableStateOf<Long?>(null) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(isTracking) {
        if (isTracking) startTime = System.currentTimeMillis()

        while (isTracking) {
            delay(1000L)
            timer++
            getLastLocation(fusedLocationClient)?.let { location ->
                locations = locations + location
                if (locations.size >= 2) {
                    val last = locations[locations.size - 2]
                    val current = locations.last()
                    distance += calculateDistance(
                        last.latitude, last.longitude,
                        current.latitude, current.longitude
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                title = "Nowy spacer",
                showMenu = true,
                navController = navController,
                onMenuItemClick = { route -> navController.navigate(route) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Czas: ${timer / 60} min ${timer % 60} sek", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dystans: ${"%.2f".format(distance)} km", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (isTracking) {
                        isTracking = false

                        val route = locations.map { LatLng(it.latitude, it.longitude) }
                        val draft = WalkDraft(
                            route = route,
                            startTime = startTime ?: System.currentTimeMillis(),
                            duration = maxOf(1, timer / 60),
                            distance = (distance * 100).roundToInt() / 100.0
                        )
                        walkViewModel.currentRoute = route
                        navController.currentBackStackEntry?.savedStateHandle?.set("walkDraft", draft)
                        navController.navigate("walk_summary")
                    } else {
                        isTracking = true
                        timer = 0
                        distance = 0f
                        locations = listOf()
                    }
                }
            ) {
                Text(if (isTracking) "Zakończ spacer" else "Rozpocznij spacer")
            }
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun getLastLocation(fusedLocationClient: FusedLocationProviderClient): Location? {
    return try {
        val locationTask = fusedLocationClient.lastLocation
        suspendCancellableCoroutine { cont ->
            locationTask.addOnSuccessListener { location ->
                cont.resume(location) { }
            }
            locationTask.addOnFailureListener {
                cont.resume(null) { }
            }
        }
    } catch (e: Exception) {
        null
    }
}

fun calculateDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Float {
    val R = 6371e3 // promień Ziemi w metrach
    val φ1 = lat1 * PI / 180
    val φ2 = lat2 * PI / 180
    val Δφ = (lat2 - lat1) * PI / 180
    val Δλ = (lon2 - lon1) * PI / 180

    val a = sin(Δφ / 2).pow(2.0) +
            cos(φ1) * cos(φ2) *
            sin(Δλ / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    val distance = R * c
    return (distance / 1000).toFloat() // w kilometrach
}

