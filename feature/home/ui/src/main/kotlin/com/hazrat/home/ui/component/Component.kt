package com.hazrat.home.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.hazrat.ui.common.DateFormatter
import com.hazrat.ui.common.SurahSvgImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.home.ui.DailyDuaData
import com.hazrat.home.ui.DailyVerseData
import com.hazrat.home.ui.WeeklyPrayerStats
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.MinimalPrayerData
import com.hazrat.ui.R
import com.hazrat.ui.common.PulsingLiveDot
import com.hazrat.ui.common.rememberPrayerState
import com.hazrat.ui.theme.IslamicTypography
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.usecase.UpcomingIslamicEvent
import com.hazrat.utils.DateUtil
import com.hazrat.utils.IslamicCalendarUtils

/**
 * Next Prayer Hero Card matching Screenshot 1 Top Red Box.
 */
@Composable
fun NextPrayerHeroCard(
    prayerData: MinimalPrayerData,
    onViewScheduleClick: () -> Unit
) {
    val prayerState = rememberPrayerState(prayerTimes = prayerData)
    val isNow = prayerState.isNow

    val prayerName = if (isNow) {
        prayerState.currentPrayer?.let { stringResource(it.nameRes) } ?: "Dhuhr"
    } else {
        prayerState.nextPrayer?.let { stringResource(it.nameRes) } ?: "Dhuhr"
    }

    val startTimeStr = if (isNow) {
        "Started at " + DateUtil.dateLongToString(prayerState.currentPrayerTime, "hh:mm a")
    } else {
        "Starts at " + DateUtil.dateLongToString(prayerState.nextPrayerTimeMillis, "hh:mm a")
    }

    val nextPrayerName = prayerState.nextPrayer?.let { stringResource(it.nameRes) } ?: ""
    val remainingLabel = if (isNow && nextPrayerName.isNotBlank()) {
        "TIME REMAINING UNTIL ${nextPrayerName.uppercase()}"
    } else {
        "TIME REMAINING"
    }

    val remainingMillis = prayerState.nextPrayerTimeMillis - System.currentTimeMillis()
    val totalSeconds = (remainingMillis.coerceAtLeast(0) / 1000)
    val hours = (totalSeconds / 3600)
    val minutes = ((totalSeconds % 3600) / 60)
    val seconds = (totalSeconds % 60)

    val hoursStr = String.format("%02d", hours)
    val minutesStr = String.format("%02d", minutes)
    val secondsStr = String.format("%02d", seconds)

    val heroGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0E7A6E),
            Color(0xFF0C6B60),
            Color(0xFF09524A),
            Color(0xFF053833),
            Color(0xFF032623)
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(dimens.elevation2)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimens.cornerXl))
                .background(heroGradient)
                .padding(dimens.space24)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(dimens.space4))
                        if (isNow) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                PulsingLiveDot()
                                Spacer(modifier = Modifier.width(dimens.space8))
                                Text(
                                    text = "CURRENT PRAYER",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = customColors.accentColor
                                )
                            }
                        } else {
                            Text(
                                text = "NEXT PRAYER",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Concentric Orbital Rings around Sun Icon
                    Box(
                        modifier = Modifier
                            .size(dimens.space64)
                            .clip(CircleShape)
                            .border(
                                width = dimens.divider,
                                color = Color(0xFFF59E0B).copy(alpha = 0.30f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(dimens.space56)
                                .clip(CircleShape)
                                .border(
                                    width = dimens.divider,
                                    color = Color(0xFFF59E0B).copy(alpha = 0.50f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(dimens.space40)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF59E0B)),
                                contentAlignment = Alignment.Center
                            ) {
                                val icon = if (isNow) prayerState.prayerIcon else prayerState.nextPrayerIcon
                                Icon(
                                    painter = painterResource(id = icon ?: R.drawable.sun),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(dimens.iconMd)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimens.space8))

                Text(
                    text = prayerName,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(dimens.space4))

                Text(
                    text = startTimeStr,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(dimens.space20))

                Text(
                    text = remainingLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(dimens.space8))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    TimerPill(value = hoursStr)
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    TimerPill(value = minutesStr)
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    TimerPill(value = secondsStr)
                }

                Spacer(modifier = Modifier.height(dimens.space24))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.compButton)
                        .clip(RoundedCornerShape(dimens.cornerLg))
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(
                            width = dimens.divider,
                            color = Color.White.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(dimens.cornerLg)
                        )
                        .clickable { onViewScheduleClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "View Full Schedule",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(dimens.space8))
                        Text(
                            text = "→",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerPill(value: String) {
    Box(
        modifier = Modifier
            .size(width = dimens.space56, height = dimens.space48)
            .clip(RoundedCornerShape(dimens.cornerLg))
            .background(Color.White.copy(alpha = 0.12f))
            .border(
                width = dimens.divider,
                color = Color.White.copy(alpha = 0.25f),
                shape = RoundedCornerShape(dimens.cornerLg)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.White
        )
    }
}

/**
 * 5-Prayer Timeline Progress Card matching Screenshot 1 with Smooth Pulse Animation on Active/Upcoming Prayer Circle.
 */
@Composable
fun PrayerTimelineCard(
    prayerData: MinimalPrayerData,
    onFullViewClick: () -> Unit
) {
    val hijriDate = IslamicCalendarUtils.getCurrentHijriDateInfo()
    val hijriDateStr = "${hijriDate.day} ${hijriDate.monthName}"

    // Smooth Infinite Pulse Animation for Active/Upcoming Prayer Node
    val infiniteTransition = rememberInfiniteTransition(label = "PrayerPulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAlpha"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space20)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(dimens.space40)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.prayers),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.space12))

                    Column {
                        Text(
                            text = "Prayer Times",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = hijriDateStr,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimens.cornerFull))
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { onFullViewClick() }
                        .padding(horizontal = dimens.space12, vertical = dimens.space4)
                ) {
                    Text(
                        text = "Full View",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space24))

            val prayerState = rememberPrayerState(prayerTimes = prayerData)
            val now = System.currentTimeMillis()

            val fajrTime = prayerData.fajrTime
            val dhuhrTime = prayerData.dhuhrTime
            val asrTime = prayerData.asrTime
            val maghribTime = prayerData.maghribTime
            val ishaTime = prayerData.ishaTime

            val fajrStr = if (fajrTime > 0) DateUtil.dateLongToString(fajrTime, "hh:mm a") else "04:42 AM"
            val dhuhrStr = if (dhuhrTime > 0) DateUtil.dateLongToString(dhuhrTime, "hh:mm a") else "12:34 PM"
            val asrStr = if (asrTime > 0) DateUtil.dateLongToString(asrTime, "hh:mm a") else "04:15 PM"
            val maghribStr = if (maghribTime > 0) DateUtil.dateLongToString(maghribTime, "hh:mm a") else "07:00 PM"
            val ishaStr = if (ishaTime > 0) DateUtil.dateLongToString(ishaTime, "hh:mm a") else "08:30 PM"

            val sunriseTime = prayerData.sunriseTime
            val activeOrNextPrayer = prayerState.currentPrayer ?: prayerState.nextPrayer

            val prayers = listOf(
                Triple("Fajr", fajrStr, (sunriseTime > 0 && now >= sunriseTime) to (activeOrNextPrayer == com.hazrat.ui.common.PrayerType.FAJR)),
                Triple("Dhu", dhuhrStr, (asrTime > 0 && now >= asrTime) to (activeOrNextPrayer == com.hazrat.ui.common.PrayerType.DHUHR || (activeOrNextPrayer == com.hazrat.ui.common.PrayerType.SUNRISE && now < dhuhrTime))),
                Triple("Asr", asrStr, (maghribTime > 0 && now >= maghribTime) to (activeOrNextPrayer == com.hazrat.ui.common.PrayerType.ASR)),
                Triple("Mag", maghribStr, (ishaTime > 0 && now >= ishaTime) to (activeOrNextPrayer == com.hazrat.ui.common.PrayerType.MAGHRIB)),
                Triple("Ish", ishaStr, (ishaTime > 0 && now >= ishaTime + 7200000L) to (activeOrNextPrayer == com.hazrat.ui.common.PrayerType.ISHA))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                prayers.forEach { (label, time, statePair) ->
                    val (isDone, isActive) = statePair

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Timeline Dot Node with Smooth Pulsing Outer Glow Ring
                        if (isDone) {
                            Box(
                                modifier = Modifier
                                    .size(dimens.space32)
                                    .clip(CircleShape)
                                    .background(customColors.accentColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✓",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        } else if (isActive) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                // Outer Expanding Pulse Ring Animation
                                Box(
                                    modifier = Modifier
                                        .size(dimens.space32)
                                        .graphicsLayer {
                                            scaleX = pulseScale
                                            scaleY = pulseScale
                                            alpha = pulseAlpha
                                        }
                                        .clip(CircleShape)
                                        .background(customColors.accentColor)
                                )

                                // Active Core Circle
                                Box(
                                    modifier = Modifier
                                        .size(dimens.space32)
                                        .clip(CircleShape)
                                        .background(customColors.accentColor.copy(alpha = 0.25f))
                                        .border(
                                            width = dimens.divider,
                                            color = customColors.accentColor,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(dimens.space12)
                                            .clip(CircleShape)
                                            .background(customColors.accentColor)
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(dimens.space32)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(dimens.space8)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(dimens.space8))

                        Text(
                            text = time,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isActive) customColors.accentColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(dimens.space4))

                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isActive) customColors.accentColor else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Side-by-side Streak & Ramadan/Event Tiles with tint = Color.Unspecified.
 */
@Composable
fun StreakAndRamadanRow(
    dailyPrayerStatus: DailyPrayerStatus?,
    upcomingEvent: UpcomingIslamicEvent?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(dimens.space12)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(dimens.cornerXl)),
            shape = RoundedCornerShape(dimens.cornerXl),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.space16),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
                    val fireColor = if (isDark) Color(0xFFFF7043) else Color(0xFFEA580C)
                    val fireBg = fireColor.copy(alpha = if (isDark) 0.20f else 0.12f)

                    Box(
                        modifier = Modifier
                            .size(dimens.space32)
                            .clip(CircleShape)
                            .background(fireBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fire),
                            contentDescription = "Streak",
                            tint = fireColor,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }
                    Spacer(modifier = Modifier.width(dimens.space8))
                    Text(
                        text = "STREAK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(dimens.space12))

                val pct = dailyPrayerStatus?.completionPercentage ?: 0
                val count = dailyPrayerStatus?.loggedPrayers?.size ?: 0

                Column {
                    Text(
                        text = "$pct%",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(dimens.space4))

                    Text(
                        text = "$count/5 today",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(dimens.cornerXl)),
            shape = RoundedCornerShape(dimens.cornerXl),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            val isDark = androidx.compose.foundation.isSystemInDarkTheme()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.space16),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val moonColor = if (isDark) Color(0xFFFFC107) else Color(0xFFD97706)
                    val moonBg = moonColor.copy(alpha = if (isDark) 0.20f else 0.12f)

                    Box(
                        modifier = Modifier
                            .size(dimens.space32)
                            .clip(CircleShape)
                            .background(moonBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.night_prayer),
                            contentDescription = "Ramadan",
                            tint = moonColor,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }
                    Spacer(modifier = Modifier.width(dimens.space8))
                    Text(
                        text = (upcomingEvent?.eventType?.name ?: "RAMADAN").uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(dimens.space12))

                val daysRemaining = upcomingEvent?.daysRemaining ?: 287
                val subtitleStr = if (upcomingEvent != null) {
                    "days until ${upcomingEvent.hijriMonth}"
                } else {
                    "days until Ramadan 1448 AH"
                }

                Column {
                    Text(
                        text = "$daysRemaining",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(dimens.space4))

                    Text(
                        text = subtitleStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

/**
 * Daily Verse Card with exact ScheherazadeFontFamily and tint = Color.Unspecified.
 */
@Composable
fun DailyVerseCard(
    dailyVerse: DailyVerseData,
    onVerseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onVerseClick() },
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(dimens.space40)
                            .clip(RoundedCornerShape(dimens.cornerMd))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.book),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.space12))

                    Column {
                        Text(
                            text = "Daily Verse",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${dailyVerse.surahName} · Verse ${dailyVerse.verseNumber}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                IconButton(
                    onClick = onVerseClick,
                    modifier = Modifier
                        .size(dimens.space32)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = "→",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = dimens.divider
                )
                Box(
                    modifier = Modifier
                        .size(dimens.space8)
                        .clip(CircleShape)
                        .background(customColors.accentColor)
                )
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            Text(
                text = dailyVerse.arabicText,
                style = IslamicTypography.ScheherazadeCardArabic,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimens.space16))

            Text(
                text = "\"${dailyVerse.englishTranslation}\"",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = FontStyle.Italic
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Clean 2x3 Quick Access Grid with tint = Color.Unspecified on icons.
 */
@Composable
fun CleanQuickAccessGrid(
    onClick: (HomePageNavIcons) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.space12)
    ) {
        Text(
            text = "Quick Access",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        val itemsRow1 = listOf<Triple<String, Int, HomePageNavIcons>>(
            Triple("99 Names", R.drawable.allah, HomePageNavIcons.AsmaUlHusna),
            Triple("Calendar", R.drawable.calendar, HomePageNavIcons.Calendar),
            Triple("Dhikr", R.drawable.zikir, HomePageNavIcons.Athkar)
        )

        val itemsRow2 = listOf<Triple<String, Int, HomePageNavIcons>>(
            Triple("Zakat", R.drawable.zakat, HomePageNavIcons.Zakat),
            Triple("Tasbih", R.drawable.tasbih, HomePageNavIcons.Tasbih),
            Triple("Duas", R.drawable.dua, HomePageNavIcons.Dua)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            itemsRow1.forEach { (label, iconRes, navIcon) ->
                QuickAccessTile(
                    modifier = Modifier.weight(1f),
                    label = label,
                    iconRes = iconRes,
                    onClick = { onClick(navIcon) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            itemsRow2.forEach { (label, iconRes, navIcon) ->
                QuickAccessTile(
                    modifier = Modifier.weight(1f),
                    label = label,
                    iconRes = iconRes,
                    onClick = { onClick(navIcon) }
                )
            }
        }
    }
}

@Composable
private fun QuickAccessTile(
    modifier: Modifier = Modifier,
    label: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.cornerXl))
            .clickable { onClick() },
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.space20, horizontal = dimens.space8),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.space48)
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(dimens.iconLg)
                )
            }

            Spacer(modifier = Modifier.height(dimens.space12))

            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Weekly Prayer Consistency Card with tint = Color.Unspecified on calendar icon.
 */
@Composable
fun WeeklyPrayerConsistencyCard(
    weeklyStats: WeeklyPrayerStats,
    onDetailsClick: () -> Unit
) {
    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(dimens.space40)
                            .clip(RoundedCornerShape(dimens.cornerMd))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.space12))

                    Column {
                        Text(
                            text = "This Week",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Prayer consistency",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimens.cornerFull))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = dimens.space12, vertical = dimens.space4)
                ) {
                    Text(
                        text = "${weeklyStats.totalCompleted}/${weeklyStats.totalTarget}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = customColors.accentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space20))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.space4),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                dayNames.forEachIndexed { index, day ->
                    val count = weeklyStats.dailyCounts.getOrElse(index) { 5 }
                    val isToday = (index == weeklyStats.currentDayIndex)

                    val pillBg = if (count == 5) customColors.accentColor
                    else if (count > 0) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.surface

                    val pillBorder = if (isToday) Modifier.border(
                        width = dimens.divider,
                        color = customColors.accentColor,
                        shape = RoundedCornerShape(dimens.cornerFull)
                    ) else Modifier

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = dimens.space32, height = dimens.space40)
                                .clip(RoundedCornerShape(dimens.cornerFull))
                                .then(pillBorder)
                                .background(pillBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (count == 5) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(dimens.space8))

                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isToday) customColors.accentColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.space20))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(dimens.space12)
                                .clip(RoundedCornerShape(dimens.cornerXs))
                                .background(customColors.accentColor)
                        )
                        Spacer(modifier = Modifier.width(dimens.space4))
                        Text(
                            text = "All 5",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(dimens.space12)
                                .clip(RoundedCornerShape(dimens.cornerXs))
                                .background(MaterialTheme.colorScheme.surface)
                        )
                        Spacer(modifier = Modifier.width(dimens.space4))
                        Text(
                            text = "Partial",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                TextButton(onClick = onDetailsClick) {
                    Text(
                        text = "Details ›",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Daily Dua Card with tint = Color.Unspecified on dua icon.
 */
@Composable
fun DailyDuaCard(
    dailyDua: DailyDuaData,
    onAllDuasClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(dimens.space40)
                            .clip(RoundedCornerShape(dimens.cornerMd))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.dua),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.space12))

                    Column {
                        Text(
                            text = "Daily Dua",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = dailyDua.categoryTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                TextButton(onClick = onAllDuasClick) {
                    Text(
                        text = "All Duas",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            Text(
                text = dailyDua.arabicText,
                style = IslamicTypography.ScheherazadeCardArabic,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimens.space12))

            Text(
                text = "\"${dailyDua.transliteration}\"",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = FontStyle.Italic
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimens.space12))

            Text(
                text = dailyDua.translation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimens.space12))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "— ${dailyDua.reference}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun HomeScreenEventCard(
    eventName: String,
    eventDate: String,
    eventType: com.hazrat.model.EventType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(dimens.cornerLg)
    ) {
        Row(
            modifier = Modifier
                .padding(dimens.space16)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.space56)
                    .background(
                        color = eventType.containerColor().copy(alpha = 0.2f),
                        shape = RoundedCornerShape(dimens.cornerLg)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(eventType.icon()),
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconLg),
                    tint = Color.Unspecified
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(dimens.space8),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = eventName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2
                )
                Text(
                    text = eventDate,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 2
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        color = eventType.color().copy(alpha = 0.1f),
                        shape = RoundedCornerShape(dimens.cornerLg)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = eventType.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = eventType.color()
                    ),
                    modifier = Modifier.padding(
                        horizontal = dimens.space12,
                        vertical = dimens.space8
                    )
                )
            }
        }
    }
}

@Composable
fun HomeRecentsSection(
    recentReads: List<com.hazrat.model.quran.RecentReadSurah>,
    onRecentReadClick: (com.hazrat.model.quran.RecentReadSurah) -> Unit,
    modifier: Modifier = Modifier
) {
    if (recentReads.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.space12)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Recents",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(dimens.space4))
            Text(
                text = "ⓘ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            items(recentReads) { recent ->
                HomeRecentSurahCard(
                    recent = recent,
                    onClick = { onRecentReadClick(recent) }
                )
            }
        }
    }
}

@Composable
private fun HomeRecentSurahCard(
    recent: com.hazrat.model.quran.RecentReadSurah,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(dimens.layoutXs)
            .clickable { onClick() },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(dimens.space2)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.space64)
                .clip(RoundedCornerShape(dimens.cornerXl))
                .background(Color.White.copy(alpha = 0.08f))
                .border(
                    width = dimens.divider,
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(dimens.cornerXl)
                ),
            contentAlignment = Alignment.Center
        ) {
            SurahSvgImage(
                surahNumber = recent.surahNumber,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .height(dimens.space40)
                    .fillMaxWidth()
                    .padding(horizontal = dimens.space8)
            )
        }

        Spacer(modifier = Modifier.height(dimens.space4))

        Text(
            text = recent.surahName,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )

        Text(
            text = "Aya ${recent.ayahNumber}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        val displayDate = remember(recent.formattedDate) {
            DateFormatter.formatHumanReadableDate(recent.formattedDate)
        }
        Text(
            text = displayDate,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}
