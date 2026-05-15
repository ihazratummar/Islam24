package com.hazrat.utils.result.error

sealed interface PrayerTimeError : RootError {
    enum class Network : PrayerTimeError {
        NO_CONNECTION,
        HTTP_ERROR,
        TIMEOUT
    }
    enum class Local : PrayerTimeError {
        TODAY_NOT_FOUND,
        DB_READ_FAILED,
        EMPTY_RESPONSE
    }
    enum class Location : PrayerTimeError {
        UNAVAILABLE  // fallback to Mecca used — caller decides how to handle
    }

    data class ErrorMessage(val message: String) : PrayerTimeError
}