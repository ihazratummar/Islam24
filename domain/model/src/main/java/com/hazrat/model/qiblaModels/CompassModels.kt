package com.hazrat.model.qiblaModels


data class CompassModels(
    val id: Int,
    val name: String,
    val compassImage: String,
    val isLoggedInRequired: Boolean,
    val compassNeedle: Int,
    val compassMiddle: Int
)