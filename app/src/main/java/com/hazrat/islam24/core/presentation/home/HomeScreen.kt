package com.hazrat.islam24.core.presentation.home

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
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.presentation.al_quran.QuranState
import com.hazrat.islam24.core.presentation.home.component.BenefitsOfRecitingWidget
import com.hazrat.islam24.core.presentation.home.component.DailyQuranAyat
import com.hazrat.islam24.core.presentation.home.component.HomePageNavIcons
import com.hazrat.islam24.core.presentation.home.component.HomeScreenTopBoxLoading
import com.hazrat.islam24.core.presentation.home.component.LazyHorizontalMenyIcons
import com.hazrat.islam24.core.presentation.home.component.RamadanCard
import com.hazrat.islam24.core.presentation.home.component.TimeLocationCard
import com.hazrat.islam24.ui.theme.dimens


@Composable
fun HomeScreen(
    navigateToPrayerTime: () -> Unit,
    prayerTimes: List<PrayerTimeEntity>,
    locationName: List<LocationDetailsEntity>,
    onWidgetClick: (HomePageNavIcons) -> Unit,
    onBenefitsWidgetClick: () -> Unit,
    quranState: QuranState,
    homeState: HomeState,
    onDailyQuranClick: (Int, Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = dimens.size20, horizontal = dimens.size10),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(dimens.size80))
                if (prayerTimes.isNotEmpty() && locationName.isNotEmpty()) {
                    TimeLocationCard(
                        prayerTimes,
                        navigateToPrayerTime,
                        locationDetailsEntity = locationName.first()
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
                LazyHorizontalMenyIcons(
                    onClick = { onWidgetClick(it) }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(dimens.size30))
            BenefitsOfRecitingWidget(
                onClick = { onBenefitsWidgetClick() }
            )
            Spacer(modifier = Modifier.height(dimens.size30))
        }
        item {
            DailyQuranAyat(
                quranState = quranState,
                homeState = homeState,
                onClick = { surah, ayah ->
                    onDailyQuranClick(surah, ayah)
                }
            )
        }
    }
}

