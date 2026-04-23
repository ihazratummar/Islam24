package com.hazrat.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hazrat.home.ui.component.BenefitsOfRecitingWidget
import com.hazrat.home.ui.component.HomePageNavIcons
import com.hazrat.home.ui.component.HomeScreenTopBoxLoading
import com.hazrat.home.ui.component.LazyHorizontalManyIcons
import com.hazrat.home.ui.component.TimeLocationCard
import com.hazrat.model.PrayerTimeModel
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.ui.theme.dimens


@Composable
fun HomeScreen(
    navigateToPrayerTime: () -> Unit,
    prayerTimes: List<PrayerTimeModel>,
    locationName: LocationName,
    onWidgetClick: (HomePageNavIcons) -> Unit,
    onBenefitsWidgetClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimens.size30)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = dimens.size20, horizontal = dimens.size10),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(dimens.size80))
                if (prayerTimes.isNotEmpty()) {
                    TimeLocationCard(
                        prayerTimeModel = prayerTimes,
                        navigateToPrayerTime,
                        locationName = locationName
                    )
                } else {
                    HomeScreenTopBoxLoading()
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.size10),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyHorizontalManyIcons(
                    onClick = { onWidgetClick(it) }
                )
            }
        }
        item {
            BenefitsOfRecitingWidget(
                onClick = { onBenefitsWidgetClick() }
            )
        }
//        item {
//            DailyQuranAyat(
//                quranState = quranState,
//                homeState = homeState,
//                onClick = { surah, ayah ->
//                    onDailyQuranClick(surah, ayah)
//                }
//            )
//        }
    }
}

