package com.hazrat.islam24.data

import android.util.Log

data class LocationData(val latitude: Double, val longitude: Double)

object LocationDataHolder {
    private var currentLocation: LocationData? = null

    fun saveLocation(latitude: Double, longitude: Double) {
        currentLocation = LocationData(latitude, longitude)
        Log.d("LocationDataHolder", "Location saved: $latitude, $longitude")
    }

    fun getCurrentLocation(): LocationData? {
        return currentLocation
    }
}