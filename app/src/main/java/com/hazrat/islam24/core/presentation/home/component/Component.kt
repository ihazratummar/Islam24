package com.hazrat.islam24.core.presentation.home.component

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.domain.model.ui_text_model.benefitsOfRecitingDataList
import com.hazrat.islam24.core.presentation.al_quran.QuranState
import com.hazrat.islam24.core.presentation.common.LocationOnCard
import com.hazrat.islam24.core.presentation.home.HomeState
import com.hazrat.utils.DateUtil.getCurrentDate
import com.hazrat.islam24.util.getSystemLanguage
import com.hazrat.ui.R
import com.hazrat.ui.theme.Hidaya
import com.hazrat.ui.theme.Kitab
import com.hazrat.ui.theme.Poppins
import com.hazrat.ui.theme.Uthmani
import com.hazrat.ui.theme.dimens
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 */

fun isPrayerTime(
    data: PrayerTimeEntity
): Boolean {

    val currentDayPrayerTimes = data
    val currentTime = System.currentTimeMillis()
    val isFajrTime =
        currentTime in currentDayPrayerTimes.fajrTime..currentDayPrayerTimes.sunriseTime.minus(
            300000
        )
    val isSunriseTime =
        currentTime in currentDayPrayerTimes.sunriseTime.minus(300000)..currentDayPrayerTimes.sunriseTime.plus(
            300000
        )
    val isDhuhrTime =
        currentTime in currentDayPrayerTimes.dhuhrTime..(currentDayPrayerTimes.dhuhrTime.plus(
            3600000
        ))
    val isAsrTime =
        currentTime in currentDayPrayerTimes.asrTime..(currentDayPrayerTimes.maghribTime.minus(
            3600000
        ))
    val isMaghribTime =
        currentTime in currentDayPrayerTimes.maghribTime..(currentDayPrayerTimes.ishaTime.minus(
            1800000
        ))
    val isIshaTime =
        currentTime in currentDayPrayerTimes.ishaTime..currentDayPrayerTimes.midnightTime

    return isFajrTime || isSunriseTime || isDhuhrTime || isAsrTime || isMaghribTime || isIshaTime
}

/// TIME LOCATION CARD
@Composable
fun TimeLocationCard(
    prayerTimeEntity: List<PrayerTimeEntity>,
    navigateToPrayerTime: () -> Unit,
    locationDetailsEntity: LocationDetailsEntity,
) {
    val currentGregorianDay = getCurrentDate()
    val prayerTimeIndex = prayerTimeEntity.indexOfFirst {
        it.gregorianDate == currentGregorianDay
    }
    val prayerTimes = prayerTimeEntity[prayerTimeIndex]

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
            if (prayerTimeIndex in prayerTimeEntity.indices) {
                HomePrayerTimeCardAnimation(
                    modifier = Modifier
                        .fillMaxSize(),
                    prayerTimeEntity = prayerTimes

                )
                Log.d("Today MainScreen", prayerTimes.gregorianDate)

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
                        data = prayerTimes,
                    )

                    Text(
                        text = stringResource(R.string.view_salat_times),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
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

                    LocationOnCard(
                        locationDetailsEntity = locationDetailsEntity
                    )
                    Spacer(modifier = Modifier.height(dimens.size8))
                    if (isPrayerTime(prayerTimes)) {
                        Text(
                            text = stringResource(id = R.string.now),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    } else {
                        DisplayCurrentPrayerTime(prayerTimes)
                    }
                }
            }

        }
    }
}


@Composable
fun RamadanCard() {
    val currentDay = LocalDate.now()
    val targetDate = LocalDate.of(2025, 3, 2)
    val daysRemaining = ChronoUnit.DAYS.between(currentDay, targetDate)
    val locale = LocalConfiguration.current.locales[0]
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
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = stringResource(R.string.days, formattedDaysRemaining),
                style = MaterialTheme.typography.bodyLarge,
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
            .padding(horizontal = dimens.size20)
            .clip(
                shape = RoundedCornerShape(dimens.size10)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.size20, vertical = dimens.size10)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.benefits_of_reciting_the_quran),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.arrowright),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(onClick = { onClick() })
                        .padding(horizontal = dimens.size20),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(dimens.size30))

            benefitsOfRecitingDataList.take(5).forEach {
                Text(
                    text = "${it.number}. ${stringResource(it.title)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.padding(horizontal = dimens.size10, vertical = dimens.size5)
                )
            }
        }
    }
}

@Composable
fun DailyQuranAyat(
    modifier: Modifier = Modifier,
    quranState: QuranState,
    homeState: HomeState,
    onClick: (Int, Int) -> Unit
) {
    val systemLanguage = getSystemLanguage()
    val arquran = quranState.arQuranData
    val enQuran = quranState.quranEnData
    val bnQuran = quranState.quranBnData

    val allArAyah = arquran?.flatMap { it.ayahs } ?: emptyList()
    val arAyah = allArAyah.find { it.number == homeState.randomAyatNumber }
    val surah =
        arquran?.find { surah -> surah.ayahs.any { it.number == homeState.randomAyatNumber } }

    val arNumber = arAyah?.numberInSurah?.let {
        NumberFormat.getInstance(Locale.forLanguageTag("ar")).format(it)
    } ?: "N/A"

    val enAllAyah = enQuran?.find { it.id == surah?.number }
    val enAyah = enAllAyah?.verses?.find { it.id == arAyah?.numberInSurah }

    val bnAllAyah = bnQuran?.find { it.id == surah?.number }
    val bnAyah = bnAllAyah?.verses?.find { it.id == arAyah?.numberInSurah }


    if (!arquran.isNullOrEmpty() && !enQuran.isNullOrEmpty() && !bnQuran.isNullOrEmpty()) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.size20),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            onClick = {
                onClick.invoke(surah?.number ?: 1, arAyah?.numberInSurah ?: 1)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.size10, vertical = dimens.size5)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.size10, horizontal = dimens.size10)
                ) {
                    Text(
                        text = "Daily Quran",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = Kitab
                            )
                        ) {
                            append(arAyah?.text)
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily = Uthmani
                            )
                        ) {
                            append(" $arNumber")
                        }
                    },
                    style = MaterialTheme.typography.displayMedium.copy(
                        textDirection = TextDirection.Rtl
                    )
                )
                Spacer(Modifier.height(dimens.size10))

                Text(
                    text = when (systemLanguage) {
                        "bn" -> {
                            "${bnAyah?.translation} - ${bnAyah?.id}"
                        }

                        else -> {
                            "${enAyah?.translation} - ${enAyah?.id}"
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(dimens.size30))
                Text(text = "${surah?.number}:${enAyah?.id}")
            }
        }
    }
}


