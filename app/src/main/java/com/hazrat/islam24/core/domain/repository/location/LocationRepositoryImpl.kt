package com.hazrat.islam24.core.domain.repository.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.hazrat.islam24.core.data.dao.LocationDao
import com.hazrat.islam24.core.data.entity.LocationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationDao: LocationDao
) : LocationRepository {

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    override val currentLocation: LiveData<Location>
        get() = _currentLocation

    private var lastSavedLocation: Location? = null

    override suspend fun saveLocation(latitude: Double, longitude: Double) {
        val location = LocationEntity(latitude = latitude, longitude = longitude)
        Log.d("LocationRepository", "Location saved: ${location.latitude}, ${location.longitude}")
        locationDao.saveLocation(location)
        lastSavedLocation = Location("").apply {
            this.latitude = latitude
            this.longitude = longitude
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) { // Add 'override' keyword here
            locationResult.lastLocation?.let { location ->
                if (hasLocationChanged(location)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        saveLocation(location.latitude, location.longitude)
                    }
                    _currentLocation.postValue(location)
                } else {
                    Log.d("LocationRepository", "Location has not changed, skipping save.")
                    stopLocationUpdates()
                }
            }
        }
    }

    private suspend fun checkAndUpdateLocation() {
        val location: LocationEntity? = locationDao.getLocation()
        if (location == null) {
            startLocationUpdates()
        } else {
            lastSavedLocation = Location("").apply {
                this.latitude = location.latitude
                this.longitude = location.longitude
            }
        }
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            checkAndUpdateLocation()
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 1000 // Update interval in milliseconds
            fastestInterval = 5000 // Fastest update interval in milliseconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    private fun stopLocationUpdates() {
        // Remove location updates
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    private fun hasLocationChanged(newLocation: Location): Boolean {
        return lastSavedLocation == null || lastSavedLocation?.latitude != newLocation.latitude || lastSavedLocation?.longitude != newLocation.longitude
    }

    override suspend fun getLocation(): LocationEntity? {
        return locationDao.getLocation()
    }
}