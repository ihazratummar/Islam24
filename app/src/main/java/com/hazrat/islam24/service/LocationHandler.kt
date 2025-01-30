package com.hazrat.islam24.service

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
import com.hazrat.islam24.core.domain.repository.location.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationHandler @Inject constructor (
    @ApplicationContext private val context: Context,
    private val locationRepository: LocationRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

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
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        onLocationError()
                        Log.e("LocationHandler", "Error getting location: Last location is null")
                    } else {
                        onLocationReceived(location)
                        Log.d(
                            "LocationHandler",
                            "LocationOnCard received: ${location.latitude}, ${location.longitude}"
                        )
                    }
                }
            } else {
                onLocationError()
            }
        } else {
            onLocationError()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun showLocationPermissionDialog(activity: ComponentActivity) {
        val sharedPreferences = context.getSharedPreferences("PermissionStatus", Context.MODE_PRIVATE)
        val permissionRequested = sharedPreferences.getBoolean("PermissionRequested", false)
        if (!permissionRequested) {
            if (!isLocationEnabled()) {
                AlertDialog.Builder(activity)
                    .setMessage("LocationOnCard is disabled. Do you want to enable it?")
                    .setPositiveButton("Yes") { _, _ ->
                        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    PERMISSION_REQUEST_ACCESS_LOCATION
                )
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
        const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    init {
        CoroutineScope(Dispatchers.Main).launch {
            getCurrentLocation(
                onLocationReceived = { location ->
                    CoroutineScope(Dispatchers.IO).launch {
                        locationRepository.saveLocation(location.latitude, location.longitude)
                        Log.d("LocationHandler", "Saved")
                    }
                },
                onLocationError = {
                    Log.e("LocationHandler", "Error getting location")
                }
            )
        }
    }
}
