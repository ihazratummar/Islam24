package com.hazrat.islam24.util

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.hazrat.islam24.core.domain.repository.location.LocationRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocationHandler(
    private val context: Context,
    private val locationRepository: LocationRepositoryImpl
) {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private fun initializeLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    private fun getCurrentLocation(onLocationReceived: (Location) -> Unit, onLocationError: () -> Unit) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(OnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        onLocationError()
                        Log.e("LocationHandler", "Error getting location: Last location is null")
                    } else {
                        onLocationReceived(location)
                        Log.d(
                            "LocationHandler",
                            "Location received: ${location.latitude}, ${location.longitude}"
                        )
                    }
                })
            } else {
                onLocationError()
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private var permissionRequested = false

    private fun requestPermission() {
        val sharedPreferences = context.getSharedPreferences("PermissionStatus", Context.MODE_PRIVATE)
        val permissionRequested = sharedPreferences.getBoolean("PermissionRequested", false)
        if (!permissionRequested) {
            val locationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!isLocationEnabled()) {
                // Location is not enabled, prompt the user to enable it
                // You can customize the message as needed
                AlertDialog.Builder(context)
                    .setMessage("Location is disabled. Do you want to enable it?")
                    .setPositiveButton("Yes") { _, _ ->
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                // Location is enabled, request permissions
                ActivityCompat.requestPermissions(
                    context as ComponentActivity,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    PERMISSION_REQUEST_ACCESS_LOCATION
                )
                // Update the permission request status in SharedPreferences
                sharedPreferences.edit().putBoolean("PermissionRequested", true).apply()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    init {
        initializeLocationClient()
        CoroutineScope(Dispatchers.Main).launch {
            getCurrentLocation(
                onLocationReceived = { location ->
                    CoroutineScope(Dispatchers.IO).launch {
                        locationRepository.saveLocation(location.latitude, location.longitude)
                    }
                },
                onLocationError = {
                    Log.e("MainActivity", "Error getting location")
                }
            )
        }
    }
}
