package com.hazrat.islam24.core.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.presentation.home.component.BackGroundCard
import com.hazrat.islam24.core.presentation.home.component.HomePageNavIcons
import com.hazrat.islam24.core.presentation.home.component.LazyVerticalGridCardIcons
import com.hazrat.islam24.core.presentation.home.component.RamadanCard
import com.hazrat.islam24.core.presentation.home.component.TimeLocationCard
import com.hazrat.islam24.core.presentation.home.component.shimmerEffect
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.core.presentation.home.component.BenefitsOfRecitingWidget


@Composable
fun HomeScreen(
    navigateToPrayerTime: () -> Unit,
    prayerTimes: List<PrayerTimeEntity>,
    locationName: List<LocationDetailsEntity>,
    onWidgetClick: (HomePageNavIcons) -> Unit,
    onBenefitsWidgetClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
    ) {
        item {
            Surface(
                modifier = Modifier
            ) {
                BackGroundCard()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = dimens.size40, horizontal = dimens.size10),
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimens.size200)
                                .shimmerEffect()
                                .clip(shape = RoundedCornerShape(dimens.size30)),
                        )
                    }
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
                LazyVerticalGridCardIcons(
                    onClick = { onWidgetClick(it) }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(dimens.size15))
            RamadanCard()
            Spacer(modifier = Modifier.height(dimens.size30))
        }
        item {
            BenefitsOfRecitingWidget(
                onClick = { onBenefitsWidgetClick() }
            )
        }
    }
}

