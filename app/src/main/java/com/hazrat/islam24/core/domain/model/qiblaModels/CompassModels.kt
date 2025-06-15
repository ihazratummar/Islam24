package com.hazrat.islam24.core.domain.model.qiblaModels

import com.hazrat.ui.R

data class CompassModels(
    val id: Int,
    val name: String,
    val compassImage: String,
    val isLoggedInRequired: Boolean,
    val compassNeedle: Int,
    val compassMiddle: Int
)

val compassList = listOf<CompassModels>(
    CompassModels(
        id = 1,
        name = "Cyan Compass",
        compassImage = "file:///android_asset/compass/blue_compass.svg",
        compassNeedle = R.drawable.blue_needle,
        isLoggedInRequired = false,
        compassMiddle = R.drawable.blue_middle_compass
    ),
    CompassModels(
        id = 2,
        name = "Cyan Compass",
        compassImage = "file:///android_asset/compass/gold_compass.svg",
        compassNeedle = R.drawable.gold_niddle,
        isLoggedInRequired = true,
        compassMiddle = R.drawable.niddle_middle
    )
)