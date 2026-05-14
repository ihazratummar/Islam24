package com.hazrat.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.home.ui.component.HomePageNavIcons
import com.hazrat.home.ui.component.HomeTopCard
import com.hazrat.home.ui.component.QuickAccessMenu
import com.hazrat.model.PrayerTimeModel
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.ui.theme.dimens
import com.hazrat.ui.R

@Composable
fun HomeScreen(
    navigateToPrayerTime: () -> Unit,
    prayerTimes: List<PrayerTimeModel>,
    locationName: LocationName,
    onWidgetClick: (HomePageNavIcons) -> Unit,
    onBenefitsWidgetClick: () -> Unit,
    homeState: HomeState
) {

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimens.space20),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.splash_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(dimens.iconXl)
                            .background(
                                color = Color(0xFF113736),
                                shape = RoundedCornerShape(dimens.cornerLg)
                            ),
                        tint = Color.Unspecified
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Islam 24",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.W700
                            )
                        )
                        Text(
                            text = "Your Daily Companion",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(dimens.cornerMd)
                            )
                    ) {
                        Text(
                            text = locationName.address,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.surfaceTint
                            ),
                            modifier = Modifier.padding(
                                horizontal = dimens.space8,
                                vertical = dimens.space4
                            )
                        )
                    }
                }
            }

            item {
                HomeTopCard(
                    prayerData = homeState.prayerData,
                    onLogPrayerClick = navigateToPrayerTime
                )
            }


            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Quick Access",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    QuickAccessMenu(
                        onClick = { onWidgetClick(it) }
                    )
                }
            }
        }
    }

}
