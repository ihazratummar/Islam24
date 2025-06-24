package com.hazrat.utils.result

import com.hazrat.utils.result.error.RootError

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-06-2025
 */



sealed interface Result <out D, out E: RootError> {

    data class Success<out D, out E: RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E: RootError>(val error: E) : Result<D, E>

}