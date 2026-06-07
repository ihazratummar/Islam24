package com.hazrat.model

import androidx.annotation.Keep

@Keep
data class UserData(
    var userId: String? = "",
    var fullName: String? = "",
    var email: String? = "",
    var profilePictureUrl: String? = "",
    var bio: String? = "",
    var lastReadSurah: Int? = 0,
    var lastReadAyah: Int? = 0,
    var compassModelId: Int? = 1
)