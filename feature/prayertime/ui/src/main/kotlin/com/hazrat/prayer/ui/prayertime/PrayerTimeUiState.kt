package com.hazrat.prayer.ui.prayertime

import androidx.compose.runtime.Immutable
import com.hazrat.model.PrayerTimeModel

@Immutable
data class PrayerTimeUiState(
    val isLoading : Boolean = false,
    val isRefreshing: Boolean = false,
    val prayerTimes: List<PrayerTimeModel> = emptyList(),
    val error: String? = null
)
