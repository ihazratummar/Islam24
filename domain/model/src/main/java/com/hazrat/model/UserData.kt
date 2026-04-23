package com.hazrat.model

data class UserData(
    val userId: String? = "",
    val fullName: String? ="",
    val email: String? = "",
    val profilePictureUrl: String? = "",
    val bio: String ? = "",
    val lastReadSurah: Int? = 0,
    val lastReadAyah: Int? = 0,
    val compassModelId : Int ? = 1
)