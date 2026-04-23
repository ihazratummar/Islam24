package com.hazrat.islam24.auth.presentation.profileScreen

import com.hazrat.model.UserData

data class ProfileState(
    val userData: UserData? = null,
    val isNameDialogOpen: Boolean = false,
    val isBioDialogOpen: Boolean = false
)



