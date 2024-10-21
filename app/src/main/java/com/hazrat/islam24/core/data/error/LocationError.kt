package com.hazrat.islam24.core.data.error



/**
 * @author Hazrat Ummar Shaikh
 */

sealed class LocationError(message: String): Exception(message) {
    class InvalidRequest(message: String) : LocationError(message)
    class InvalidKey(message: String) : LocationError(message)
    class AccessRestricted(message: String) : LocationError(message)
    class UnableToGeocode(message: String) : LocationError(message)
    class RateLimited(message: String) : LocationError(message)
    class InternalServerError(message: String) : LocationError(message)
    class UnknownError(message: String) : LocationError(message)
}