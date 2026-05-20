package com.hazrat.location.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.hazrat.location.model.LocationConfig
import com.hazrat.location.model.LocationError
import com.hazrat.location.model.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import kotlin.coroutines.resume
import com.google.android.gms.location.LocationResult as GmsLocationResult


/**
 * @author hazratummar
 * Created on 23/01/26
 */

class FusedLocationDataSource(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val permissionChecker: PermissionChecker
) : LocationDataSource {

//    private var locationCallback: LocationCallback? = null

    override suspend fun getLastKnownLocation(): LocationResult {
        return try {
            when {
                !permissionChecker.hasLocationPermission() -> {
                    LocationResult.Error(LocationError.PermissionDenied)
                }
                !isLocationEnabled() -> {
                    LocationResult.Error(LocationError.LocationDisabled)
                }
                else -> {
                    val location = getLastLocationInternal()
                    if (location != null) {
                        Timber.d("Last known location: ${location.latitude}, ${location.longitude}")
                        LocationResult.Success(location)
                    } else {
                        Timber.w("Last known location is null")
                        LocationResult.Error(LocationError.LocationUnavailable)
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting last known location")
            LocationResult.Error(LocationError.Unknown(e))
        }
    }

    override suspend fun getCurrentLocation(locationConfig: LocationConfig): LocationResult {
        return try {
            when {
                !permissionChecker.hasLocationPermission() -> {
                    LocationResult.Error(LocationError.PermissionDenied)
                }
                !isLocationEnabled() -> {
                    LocationResult.Error(LocationError.LocationDisabled)
                }
                else -> {
                    val location = getCurrentLocationInternal(locationConfig = locationConfig)
                    if (location != null) {
                        Timber.d("Current location: ${location.latitude}, ${location.longitude}")
                        LocationResult.Success(location)
                    } else {
                        Timber.w("Current location is null")
                        LocationResult.Error(LocationError.LocationUnavailable)
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting current location")
            LocationResult.Error(LocationError.Unknown(e))
        }
    }

    override fun observeLocationUpdates(
        locationConfig: LocationConfig
    ): Flow<LocationResult> = callbackFlow {

        if (!permissionChecker.hasLocationPermission()) {

            trySend(
                LocationResult.Error(
                    LocationError.PermissionDenied
                )
            )

            close()

            return@callbackFlow
        }

        if (!isLocationEnabled()) {

            trySend(
                LocationResult.Error(
                    LocationError.LocationDisabled
                )
            )

            close()

            return@callbackFlow
        }

        // IMPORTANT:
        // Local callback per collector
        // Avoid shared mutable callback state
        val callback = object : LocationCallback() {

            override fun onLocationResult(
                result: GmsLocationResult
            ) {

                result.lastLocation?.let { location ->

                    Timber.tag("FusedLocationDataSource")
                        .d("Location update: ${location.latitude}, ${location.longitude} "
                        )

                    trySend(
                        LocationResult.Success(location)
                    )
                }
            }
        }

        val locationRequest = LocationRequest.Builder(
            locationConfig.priority,
            locationConfig.intervalMillis
        ).apply {

            setMinUpdateIntervalMillis(
                locationConfig.minUpdateIntervalMillis
            )

            setMaxUpdateDelayMillis(
                locationConfig.maxWaitTimeMs
            )

            setMinUpdateDistanceMeters(
                locationConfig.minUpdateDistanceMeters
            )

        }.build()

        try {

            requestLocationUpdatesInternal(
                locationRequest = locationRequest,
                callback = callback
            )

        } catch (e: SecurityException) {

            trySend(
                LocationResult.Error(
                    LocationError.PermissionDenied
                )
            )

            close()

        } catch (e: Exception) {

            Timber.tag("FusedLocationDataSource")
                .e(e, "Error requesting location updates")

            trySend(
                LocationResult.Error(
                    LocationError.Unknown(e)
                )
            )

            close()
        }

        awaitClose {

            fusedLocationClient.removeLocationUpdates(callback)

            Timber.tag("FusedLocationDataSource")
                .d("Location updates stopped")
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestLocationUpdatesInternal(
        locationRequest: LocationRequest,
        callback: LocationCallback
    ) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ).await()
    }


    @SuppressLint("MissingPermission")
    private suspend fun getLastLocationInternal(): Location? {
        return fusedLocationClient.lastLocation.await()
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocationInternal(locationConfig: LocationConfig): Location? {
        return suspendCancellableCoroutine { continuation ->
            val cancellationToken = com.google.android.gms.tasks.CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                locationConfig.priority,
                cancellationToken.token
            ).addOnSuccessListener { location ->
                continuation.resume(location)
            }.addOnFailureListener { exception ->
                Timber.e(exception, "Failed to get current location")
                continuation.resume(null)
            }

            continuation.invokeOnCancellation {
                cancellationToken.cancel()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}