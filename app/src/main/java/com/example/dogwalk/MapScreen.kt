package com.example.dogwalk

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.tasks.await

@Composable
fun MapScreen(route: List<LatLng>) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean -> }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(route) {
        if (route.isNotEmpty()) {
            val firstPoint = route.first()
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(firstPoint, 16f)
            )
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val location: Location? = try {
                    fusedLocationClient.lastLocation.await()
                } catch (e: Exception) {
                    null
                }

                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLngZoom(userLatLng, 16f)
                    )
                }
            }
        }
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = true
        )
    ) {
        if (route.isNotEmpty()) {
            Polyline(points = route)
        }
    }
}
