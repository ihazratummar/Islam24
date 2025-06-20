package com.hazrat.utils.result.error


/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-06-2025
 */


sealed interface AppError : RootError {
    object Network : AppError
    object Auth : AppError
    object UserNotFound: AppError
    data class Unknown(val message: String) : AppError
    data class Custom(val message: String) : AppError
    data class DatabaseError(val message: String) : AppError
    data class ExceptionCaught(val message: String) : AppError

}