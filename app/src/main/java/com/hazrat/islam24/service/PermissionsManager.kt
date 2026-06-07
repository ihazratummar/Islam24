package com.hazrat.islam24.service

/**
 * @author Hazrat Ummar Shaikh
 */

// PermissionsManager.kt


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsManager (private val activity: ComponentActivity) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                onPermissionGranted?.invoke()
                requestNextPermission() // Call to request the next permission
            } else {
                onPermissionDenied?.invoke()
            }
        }

    var onPermissionGranted: (() -> Unit)? = null
    private var onPermissionDenied: (() -> Unit)? = null

    // Variable to track if location permission was requested
    private var isLocationPermissionGranted = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestPermission() {
        checkAndRequestLocationPermission() // Start by requesting location permission
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkAndRequestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                isLocationPermissionGranted = true
                onPermissionGranted?.invoke()
                requestNextPermission() // Proceed to request notification permission
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            when {
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    onPermissionGranted?.invoke()
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNextPermission() {
        if (!isLocationPermissionGranted) {
            // First, request location permission
            checkAndRequestLocationPermission()
        } else {
            // Then, request notification permission
            requestNotificationPermission()
        }
    }
    fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
                    REQUEST_CODE_SCHEDULE_EXACT_ALARM
                )
            }
        }
    }
}

const val  REQUEST_CODE_SCHEDULE_EXACT_ALARM = 100
