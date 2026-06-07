package com.hazrat.prayer.ui.prayertime

import androidx.compose.runtime.Immutable
import com.hazrat.model.MinimalPrayerData
import com.hazrat.model.PrayerTimeModel

@Immutable
data class PrayerTimeUiState(
    val isLoading : Boolean = false,
    val isRefreshing: Boolean = false,
    val prayerTimes: MinimalPrayerData? = null,
    val error: String? = null,
    val locationNane: String? = null,
    val pages: List<MinimalPrayerData> = emptyList(),
    val selectedIndex : Int = -1,
    val isFetchingNextYear : Boolean = false,
)
