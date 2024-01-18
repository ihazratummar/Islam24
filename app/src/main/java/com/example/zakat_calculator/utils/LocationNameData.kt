package com.example.zakat_calculator.utils

data class LocationNameData(val locationName: String?)

object LocationNameStorage {
    private var currentLocationName: LocationNameData? = null

    fun setCurrentLocationName(locationName: String?) {
        currentLocationName = LocationNameData(locationName)
    }

    fun getCurrentLocationName(): LocationNameData? {
        return currentLocationName
    }
}