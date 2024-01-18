//LocationCalback.kt


package com.example.zakat_calculator.utils

import android.util.Log
import kotlin.math.log

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
