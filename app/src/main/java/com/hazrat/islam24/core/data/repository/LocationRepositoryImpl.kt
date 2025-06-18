package com.hazrat.islam24.core.data.repository

import android.annotation.SuppressLint
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
import com.hazrat.islam24.core.domain.repository.location.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * @author Hazrat Ummar Shaikh
 */


class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationDao: LocationDao
) : LocationRepository {

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    override val currentLocation: LiveData<Location>
        get() = _currentLocation

    private var lastSavedLocation: Location? = null

    /**
     * Saves the specified location coordinates (latitude and longitude).
     *
     * @param latitude The latitude of the location to be saved.
     * @param longitude The longitude of the location to be saved.
     */
    override suspend fun saveLocation(latitude: Double, longitude: Double) {
        val location = LocationEntity(latitude = latitude, longitude = longitude)
        Log.d("LocationRepository", "LocationOnCard saved: ${location.latitude}, ${location.longitude}")
        locationDao.saveLocation(location)
        lastSavedLocation = Location("").apply {
            this.latitude = latitude
            this.longitude = longitude
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                if (hasLocationChanged(location)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        saveLocation(location.latitude, location.longitude)
                    }
                    _currentLocation.postValue(location)
                } else {
                    Log.d("LocationRepository", "LocationOnCard has not changed, skipping save.")
                    stopLocationUpdates()
                }
            }
        }
    }

    /**
     * Checks if a location is already saved and updates the current location if necessary.
     */
    override suspend fun checkAndUpdateLocation() {
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

    /**
     * Starts location updates.
     */
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

    /**
     * Stops location updates.
     */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Checks if the new location has changed from the last saved location.
     *
     * @param newLocation The new location to be checked.
     * @return True if the location has changed, false otherwise.
     */
    private fun hasLocationChanged(newLocation: Location): Boolean {
        return lastSavedLocation == null ||
                lastSavedLocation?.latitude != newLocation.latitude ||
                lastSavedLocation?.longitude != newLocation.longitude
    }

    /**
     * Retrieves the saved location entity from the database.
     *
     * @return A LocationEntity object representing the saved location, or null if no location is saved.
     */
    override suspend fun getLocation(): LocationEntity? {
        return locationDao.getLocation()
    }
}
