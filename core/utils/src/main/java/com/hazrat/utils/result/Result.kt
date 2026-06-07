package com.hazrat.utils.result

import com.hazrat.utils.result.error.PrayerTimeError
import com.hazrat.utils.result.error.RootError

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-06-2025
 */



sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E>(val error: E) : Result<Nothing, E>
}