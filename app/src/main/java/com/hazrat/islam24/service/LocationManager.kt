package com.hazrat.islam24.service

/**
 * @author Hazrat Ummar Shaikh
 */

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    var onLocationReceived: ((Location) -> Unit)? = null

    fun getLastKnownLocation() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("LocationManager", "Location permissions not granted")
            return
        }

        // Request the last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived?.invoke(location)
                Log.d("LocationManager", "Location received: ${location.latitude}, ${location.longitude}")
            } else {
                Log.d("LocationManager", "Location is null")
            }
        }.addOnFailureListener { e ->
            Log.d("LocationManager", "Failed to get location: ${e.message}")
        }
    }
}