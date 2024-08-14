package com.hazrat.islam24.auth.model

data class UserData(
    val userId: String? = "",
    val fullName: String? ="",
    val email: String? = "",
    val profilePictureUrl: String? = "",
    val bio: String ? = ""
)
