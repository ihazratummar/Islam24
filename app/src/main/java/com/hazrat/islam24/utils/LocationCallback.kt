//LocationCalback.kt


package com.hazrat.islam24.utils

data class LocationData(val latitude: Double, val longitude: Double)

object LocationStorage {
    private var currentLocation: LocationData? = null

    fun setCurrentLocation(latitude: Double, longitude: Double) {
        currentLocation = LocationData(latitude, longitude)
        (currentLocation)
    }

    fun getCurrentLocation(): LocationData? {
        return currentLocation
    }

}
