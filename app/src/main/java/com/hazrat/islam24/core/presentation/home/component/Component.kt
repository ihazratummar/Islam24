package com.hazrat.islam24.core.presentation.home.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.model.ui_text_model.benefitsOfRecitingDataList
import com.hazrat.islam24.core.presentation.common.LocationName
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.Hidaya
import com.hazrat.islam24.ui.theme.Poppins
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * @author Hazrat Ummar Shaikh
 */

fun isPrayerTime(
    data: List<PrayerTimeEntity>,
    gregorianDay: Int,
    hijriDay: String
): Boolean {
    val currentDayPrayerTimes =
        data.find { it.gregorianDay == gregorianDay.toString() && it.hijriDay == hijriDay }
    val currentTime = System.currentTimeMillis()
    val isFajrTime = currentTime in (currentDayPrayerTimes?.fajrTime
        ?: 0)..(currentDayPrayerTimes?.sunriseTime?.minus(300000) ?: 0)
    val isSunriseTime = currentTime in (currentDayPrayerTimes?.sunriseTime?.minus(300000)
        ?: 0)..(currentDayPrayerTimes?.sunriseTime?.plus(300000) ?: 0)
    val isDhuhrTime = currentTime in (currentDayPrayerTimes?.dhuhrTime
        ?: 0)..((currentDayPrayerTimes?.dhuhrTime?.plus(3600000)) ?: 0)
    val isAsrTime = currentTime in (currentDayPrayerTimes?.asrTime
        ?: 0)..((currentDayPrayerTimes?.maghribTime?.minus(3600000)) ?: 0)
    val isMaghribTime = currentTime in (currentDayPrayerTimes?.maghribTime
        ?: 0)..((currentDayPrayerTimes?.ishaTime?.minus(1800000)) ?: 0)
    val isIshaTime = currentTime in (currentDayPrayerTimes?.ishaTime
        ?: 0)..(currentDayPrayerTimes?.midnightTime ?: 0)

    return isFajrTime || isSunriseTime || isDhuhrTime || isAsrTime || isMaghribTime || isIshaTime
}

/// TIME LOCATION CARD
@Composable
fun TimeLocationCard(
    prayerTimeEntity: List<PrayerTimeEntity>,
    navigateToPrayerTime: () -> Unit,
    locationDetailsEntity: LocationDetailsEntity,
    viewModel: MainViewModel = hiltViewModel(),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimens.size5)
            .height(dimens.size250)
            .clickable(
                onClick = { navigateToPrayerTime() },
                onClickLabel = stringResource(id = R.string.prayertimesandlocation)
            ),
        shape = RoundedCornerShape(dimens.size30),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val today = getCurrentDay()
            val index = today - 1
            Log.d("Today", "$index")
            if (index in prayerTimeEntity.indices) {
                HomePrayerTimeCardAnimation(
                    modifier = Modifier
                        .fillMaxSize(),
                    prayerTimeEntity = prayerTimeEntity[index]

                )

            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimens.size10)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .padding(
                            start = dimens.size20,
                            bottom = dimens.size15
                        ),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    DisplayCurrentPrayerName(
                        prayerTimeEntity,
                    )

                    Text(
                        text = stringResource(R.string.view_salat_times),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .padding(
                            start = dimens.size5,
                            bottom = dimens.size15,
                            end = dimens.size10,
                            top = dimens.size20
                        ),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {

                    LocationName(locationDetailsEntity)
                    Spacer(modifier = Modifier.height(dimens.size8))
                    val grday = getCurrentDay()
                    val hijriday = viewModel.getHijriDay()
                    if (isPrayerTime(prayerTimeEntity, grday, hijriday)) {
                        Text(
                            text = stringResource(id = R.string.now),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        DisplayCurrentPrayerTime(prayerTimeEntity, hijriday)
                    }
                }
            }

        }
    }
}

////BACKGROUND CARD WITH MASJID ICON
//@Preview
@Composable
fun BackGroundCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            bottomEnd = dimens.size50,
            bottomStart = dimens.size50
        ),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.group),
            contentDescription = "masjidimage",
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = dimens.size50,
                        bottomEnd = dimens.size50
                    )
                )
                .size(dimens.size300),
            colorFilter = ColorFilter.colorMatrix(colorMatrix = ColorMatrix())
        )
    }
}


@Composable
fun RamadanCard() {
    val currentDay = LocalDate.now()
    val targetDate = LocalDate.of(2025, 3, 2)
    val daysRemaining = ChronoUnit.DAYS.between(currentDay, targetDate)
    // Retrieve the current device locale
    val locale = LocalContext.current.resources.configuration.locales[0]
    val formattedDaysRemaining = NumberFormat.getInstance(locale).format(daysRemaining)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.size100)
            .padding(horizontal = dimens.size15),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.ramadan),
                    alpha = 0.3f,
                    alignment = Alignment.Center,
                    sizeToIntrinsics = true,
                    contentScale = ContentScale.FillHeight
                )
                .padding(horizontal = dimens.size30)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = stringResource(R.string.ramadan),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = stringResource(R.string.days, formattedDaysRemaining),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontFamily = Hidaya
            )
        }
    }
}

@Composable
fun BenefitsOfRecitingWidget(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size20),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.benefits_of_reciting_the_quran))
                Text(
                    text = "Details",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.clickable(onClick = {onClick()})
                )
            }
            Spacer(modifier = Modifier.height(dimens.size30))

            benefitsOfRecitingDataList.take(5).forEach {
                Text(
                    text = "${it.number}. ${stringResource(it.title)}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(horizontal = dimens.size10, vertical = dimens.size5)
                )
            }
        }
    }
}


