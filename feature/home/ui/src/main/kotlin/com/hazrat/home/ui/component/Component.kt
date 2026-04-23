package com.hazrat.home.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.PrayerTimeModel
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.ui.R
import com.hazrat.ui.common.LocationOnCard
import com.hazrat.ui.theme.Hidaya
import com.hazrat.ui.theme.Poppins
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil.getCurrentDate
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * @author Hazrat Ummar Shaikh
 */

fun isPrayerTime(
    data: PrayerTimeModel
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
    prayerTimeModel: List<PrayerTimeModel>,
    navigateToPrayerTime: () -> Unit,
    locationName: LocationName?,
) {
    val currentGregorianDay = getCurrentDate()
    val prayerTimeIndex = prayerTimeModel.indexOfFirst {
        it.gregorianDate == currentGregorianDay
    }
    val prayerTimes = prayerTimeModel[prayerTimeIndex]

    val isDark = isSystemInDarkTheme()
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val gradientColors =
        if (isDark) {
            listOf(primaryColor, Color.Black) // or secondary
        } else {
            listOf(primaryColor, secondaryColor)

        }

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
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.size5),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientColors
                    )
                )
        ) {
            // Background Texture (Optional, reused from previous logic but transparent)
            if (prayerTimeIndex in prayerTimeModel.indices) {
                // Opacity reduced for texture
                Box(modifier = Modifier.alpha(0.1f)) {
                    HomePrayerTimeCardAnimation(
                        modifier = Modifier.fillMaxSize(),
                        prayerTimeEntity = prayerTimes
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimens.size15)
            ) {
                // LEFT: Next Prayer / Timeline
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.6f)
                        .padding(start = dimens.size10, bottom = dimens.size10),
                    verticalArrangement = Arrangement.Center
                ) {
                    DisplayCurrentPrayerName(
                        data = prayerTimes,
                        textColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(dimens.size5))

                    Text(
                        text = stringResource(R.string.view_salat_times),
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // RIGHT: Location & Details
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .padding(top = dimens.size15, end = dimens.size10, bottom = dimens.size15),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {

                    LocationOnCard(
                        locationName = locationName?.address?: "Mecca",
                        textColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(dimens.size8))

                    if (isPrayerTime(prayerTimes)) {
                        Text(
                            text = stringResource(id = R.string.now),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    } else {
                        DisplayCurrentPrayerTime(
                            prayerTimes,
                            textColor = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RamadanCard() {
    val currentDay = LocalDate.now()
    val targetDate = LocalDate.of(2026, 2, 18)
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
        ),
        onClick = onClick
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

//@Composable
//fun DailyQuranAyat(
//    modifier: Modifier = Modifier,
//    quranState: QuranState,
//    homeState: HomeState,
//    onClick: (Int, Int) -> Unit
//) {
//    val systemLanguage = getSystemLanguage()
//    val arquran = quranState.arQuranData
//    val enQuran = quranState.quranEnData
//    val bnQuran = quranState.quranBnData
//
//    val allArAyah = arquran?.flatMap { it.ayahs } ?: emptyList()
//    val arAyah = allArAyah.find { it.number == homeState.randomAyatNumber }
//    val surah =
//        arquran?.find { surah -> surah.ayahs.any { it.number == homeState.randomAyatNumber } }
//
//    val arNumber = arAyah?.numberInSurah?.let { number ->
//        val numberStr = number.toString()
//        val arabicDigits = numberStr.map { char ->
//            when (char) {
//                '0' -> '٠'
//                '1' -> '١'
//                '2' -> '٢'
//                '3' -> '٣'
//                '4' -> '٤'
//                '5' -> '٥'
//                '6' -> '٦'
//                '7' -> '٧'
//                '8' -> '٨'
//                '9' -> '٩'
//                else -> char
//            }
//        }.joinToString("")
//        "\u06DD$arabicDigits"
//    } ?: "N/A"
//
//    val enAllAyah = enQuran?.find { it.id == surah?.number }
//    val enAyah = enAllAyah?.verses?.find { it.id == arAyah?.numberInSurah }
//
//    val bnAllAyah = bnQuran?.find { it.id == surah?.number }
//    val bnAyah = bnAllAyah?.verses?.find { it.id == arAyah?.numberInSurah }
//
//
//    if (!arquran.isNullOrEmpty() && !enQuran.isNullOrEmpty() && !bnQuran.isNullOrEmpty()) {
//        val isDark = isSystemInDarkTheme()
//        val borderColor = MaterialTheme.colorScheme.tertiary
//        val containerColor = MaterialTheme.colorScheme.surface
//        val titleColor = MaterialTheme.colorScheme.tertiary
//
//        Box(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(horizontal = dimens.size20)
//                .clip(RoundedCornerShape(dimens.size15))
//                .border(
//                    width = dimens.size1,
//                    color = borderColor,
//                    shape = RoundedCornerShape(dimens.size15)
//                )
//                .clickable {
//                    onClick.invoke(surah?.number ?: 1, arAyah?.numberInSurah ?: 1)
//                }
//                .background(containerColor.copy(alpha = if (isDark) 0.5f else 1f))
//        ) {
//            // Texture
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .paint(
//                        painter = painterResource(id = R.drawable.ramadan),
//                        alpha = if (isDark) 0.1f else 0.05f,
//                        contentScale = ContentScale.Crop
//                    )
//            )
//
//            // Content
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(dimens.size20),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                // Header
//                Text(
//                    text = "Daily Quran",
//                    style = MaterialTheme.typography.titleMedium.copy(
//                        fontWeight = FontWeight.Bold,
//                        color = titleColor,
//                        letterSpacing = 1.sp
//                    ),
//                    modifier = Modifier.padding(bottom = dimens.size15)
//                )
//
//                // Quran Text
//                Text(
//                    modifier = Modifier.fillMaxWidth(),
//                    text = buildAnnotatedString {
//                        withStyle(
//                            style = SpanStyle(
//                                fontFamily = Kitab
//                            )
//                        ) {
//                            append(arAyah?.text)
//                        }
//                    },
//                    style = MaterialTheme.typography.headlineMedium.copy(
//                        textDirection = TextDirection.Rtl,
//                        color = MaterialTheme.colorScheme.onSurface,
//                        lineHeight = 40.sp
//                    ),
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(Modifier.height(dimens.size20))
//
//                // Translation
//                Text(
//                    text = when (systemLanguage) {
//                        "bn" -> "${bnAyah?.translation}"
//                        else -> "${enAyah?.translation}"
//                    },
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        fontStyle = FontStyle.Italic,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    ),
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(Modifier.height(dimens.size15))
//
//                // Reference
//                Text(
//                    text = "${surah?.name} ${surah?.number}:${enAyah?.id}",
//                    style = MaterialTheme.typography.labelMedium.copy(
//                        color = titleColor
//                    )
//                )
//            }
//        }
//    }
//}
//

