package com.hazrat.prayer.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.common.PrayerType
import com.hazrat.ui.common.PulsingLiveDot
import com.hazrat.ui.theme.FajrGradient
import com.hazrat.ui.theme.Success
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil
import com.hazrat.utils.formatLocalizedDigits
import com.hazrat.utils.toLocalizedDigits

val GoldAccent = Color(0xFFE5A93C)

/**
 * Top Next Prayer Countdown Hero Card styled with app emerald gradient and pattern.
 */
@Composable
fun NextPrayerHeroCard(
    modifier: Modifier = Modifier,
    prayerName: String,
    scheduledTimeStr: String,
    countdownText: String,
    isNow: Boolean,
    nextPrayerName: String? = null
) {
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
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = if (isNow) BorderStroke(dimens.divider * 2, GoldAccent) else null,
        elevation = CardDefaults.cardElevation(dimens.elevation2)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimens.cornerXl))
                .background(heroGradient)
        ) {
            // Pure Compose Radial Glow Backdrop (No image vector pattern!)
            androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GoldAccent.copy(alpha = 0.15f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.15f),
                        radius = size.width * 0.65f
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.space24)
            ) {
                // Header Label Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isNow) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PulsingLiveDot()
                            Spacer(modifier = Modifier.width(dimens.space8))
                            Text(
                                text = stringResource(R.string.prayer_current_prayer),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = GoldAccent
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.prayer_next_prayer_uppercase),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimens.space8))

                // Prayer Name
                Text(
                    text = prayerName,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(dimens.space4))

                // Scheduled Time
                Text(
                    text = scheduledTimeStr,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(dimens.space20))

                val timerLabel = if (isNow && !nextPrayerName.isNullOrEmpty()) {
                    stringResource(R.string.home_time_remaining_until, nextPrayerName)
                } else if (!nextPrayerName.isNullOrEmpty()) {
                    stringResource(R.string.home_time_remaining_until, nextPrayerName)
                } else {
                    stringResource(R.string.home_time_remaining)
                }

                // Countdown Timer Section
                Text(
                    text = timerLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(dimens.space8))

                val parts = countdownText.split(":")
                val hoursStr = parts.getOrNull(0) ?: "00"
                val minutesStr = parts.getOrNull(1) ?: "00"
                val secondsStr = parts.getOrNull(2) ?: "00"

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    TimerPill(value = hoursStr.formatLocalizedDigits())
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    TimerPill(value = minutesStr.formatLocalizedDigits())
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    TimerPill(value = secondsStr.formatLocalizedDigits())
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
            .clip(RoundedCornerShape(dimens.cornerMd))
            .background(Color.White.copy(alpha = 0.15f))
            .border(
                width = dimens.divider,
                color = Color.White.copy(alpha = 0.25f),
                shape = RoundedCornerShape(dimens.cornerMd)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = Color.White
        )
    }
}

/**
 * Date Pagination Header & Quick Filter Chips.
 */
@Composable
fun PrayerDatePaginationHeader(
    modifier: Modifier = Modifier,
    dateTitleStr: String,
    hijriSubtitleStr: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    selectedChipIndex: Int, // 0 = Yesterday, 1 = Today, 2 = Tomorrow, -1 = Other date
    onChipSelect: (Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.space12)
    ) {
        // Date Prev/Next Navigation Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.compChip)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .clickable { onPrevClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = stringResource(R.string.prayer_previous_day),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(dimens.iconSm)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = dateTitleStr,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = hijriSubtitleStr,
                    style = MaterialTheme.typography.labelMedium,
                    color = GoldAccent
                )
            }

            Box(
                modifier = Modifier
                    .size(dimens.compChip)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .clickable { onNextClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrowright),
                    contentDescription = stringResource(R.string.prayer_next_day),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(dimens.iconSm)
                )
            }
        }

        // Quick Filter Chips Row (Yesterday, Today, Tomorrow)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(dimens.cornerFull),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                border = BorderStroke(dimens.divider, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(dimens.space4),
                    horizontalArrangement = Arrangement.spacedBy(dimens.space4)
                ) {
                    val chips = listOf(
                        stringResource(R.string.date_yesterday),
                        stringResource(R.string.calendar_today),
                        stringResource(R.string.date_tomorrow)
                    )
                    chips.forEachIndexed { idx, label ->
                        val isSelected = (selectedChipIndex == idx)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimens.cornerFull))
                                .background(if (isSelected) GoldAccent else Color.Transparent)
                                .clickable { onChipSelect(idx) }
                                .padding(horizontal = dimens.space16, vertical = dimens.space8)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Progress Card matching reference layout with gold percentage & smooth progress bar.
 */
@Composable
fun PrayerProgressCard(
    modifier: Modifier = Modifier,
    todayTimeStamp: Long,
    prayerCompletePercent: Int = 0,
    completionRatio: Float = 0f,
    completePrayerCount: Int = 0
) {
    val animatedProgress by animateFloatAsState(
        targetValue = completionRatio,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "progressAnimation"
    )
    val animatedPercentage by animateIntAsState(
        targetValue = prayerCompletePercent,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "completePercentage"
    )

    Card(
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.avatarMd)
                        .clip(RoundedCornerShape(dimens.cornerMd))
                        .background(GoldAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.circle_check),
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }

                Text(
                    text = stringResource(R.string.prayer_completed_count, completePrayerCount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "$animatedPercentage%".formatLocalizedDigits(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = GoldAccent,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.space12)
                    .clip(shape = RoundedCornerShape(100))
                    .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(GoldAccent)
                )
            }
        }
    }
}

/**
 * 5 Daily Prayer Card matching reference layout with custom icons, period labels, badges, and conditional Log/Done buttons.
 */
@Composable
fun PrayerTimeCard(
    prayerType: PrayerType = PrayerType.ISHA,
    prayerTime: Long = 0L,
    isLogged: Boolean = false,
    isNextPrayer: Boolean = false,
    isNotificationEnabled: Boolean = false,
    onNotificationClick: (PrayerType) -> Unit = {},
    onLogPrayerClick: (PrayerType) -> Unit = {}
) {
    val isFuturePrayer = prayerTime > System.currentTimeMillis()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isNextPrayer) BorderStroke(dimens.space2, GoldAccent) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            // Left Custom Prayer Icon Container
            PrayerIconWithBackground(
                icon = prayerType.icon,
                containerColor = prayerType.gradient
            )

            // Center Info Column (Name, Badges, Time)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space4),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    Text(
                        text = stringResource(prayerType.nameRes),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Badges
                    if (isLogged && prayerType != PrayerType.SUNRISE) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimens.cornerSm))
                                .background(Success.copy(alpha = 0.2f))
                                .padding(horizontal = dimens.space8, vertical = dimens.space2)
                        ) {
                            Text(
                                text = stringResource(R.string.prayer_done_caps),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Success
                            )
                        }
                    } else if (isNextPrayer) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimens.cornerSm))
                                .background(GoldAccent.copy(alpha = 0.25f))
                                .padding(horizontal = dimens.space8, vertical = dimens.space2)
                        ) {
                            Text(
                                text = stringResource(R.string.prayer_next_caps),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = GoldAccent
                            )
                        }
                    }
                }

                Text(
                    text = DateUtil.dateLongToString(prayerTime, "hh:mm a").formatLocalizedDigits(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Right Action Section: Notification Bell & Circular Radio / Log Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space8)
            ) {
                if (prayerType != PrayerType.SUNRISE) {
                    // Notification Bell Icon
                    val notificationColor = if (isNotificationEnabled) GoldAccent else customColors.secondaryText
                    Box(
                        modifier = Modifier
                            .size(dimens.avatarMd)
                            .clip(CircleShape)
                            .background(notificationColor.copy(alpha = 0.15f))
                            .clickable { onNotificationClick(prayerType) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(if (isNotificationEnabled) R.drawable.notificationonn else R.drawable.notificationoff),
                            contentDescription = stringResource(R.string.prayer_notification_toggle),
                            tint = notificationColor,
                            modifier = Modifier.size(dimens.iconSm)
                        )
                    }

                    // Radio Button / Circular Log Toggle
                    if (isLogged) {
                        Box(
                            modifier = Modifier
                                .size(dimens.avatarMd)
                                .clip(CircleShape)
                                .background(Success)
                                .clickable { onLogPrayerClick(prayerType) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.check),
                                contentDescription = stringResource(R.string.prayer_logged),
                                tint = Color.White,
                                modifier = Modifier.size(dimens.iconSx)
                            )
                        }
                    } else if (!isFuturePrayer) {
                        // Past or Current Active Prayer: Clickable Radio Button
                        Box(
                            modifier = Modifier
                                .size(dimens.avatarMd)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                                .border(
                                    BorderStroke(dimens.space2, GoldAccent),
                                    shape = CircleShape
                                )
                                .clickable { onLogPrayerClick(prayerType) },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(dimens.space8)
                                    .clip(CircleShape)
                                    .background(GoldAccent.copy(alpha = 0.2f))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerIconWithBackground(
    icon: Int,
    containerColor: List<Color> = FajrGradient,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(dimens.avatarLg)
            .clip(RoundedCornerShape(dimens.cornerMd))
            .background(
                brush = Brush.linearGradient(
                    colors = containerColor
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(dimens.iconSm),
            tint = Color.White
        )
    }
}

/**
 * Notifications Setting Action Card.
 */
@Composable
fun NotificationSettingCard(
    modifier: Modifier = Modifier,
    totalNotificationOn: Int = 0,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            IconWithBackground(
                icon = R.drawable.notificationonn,
                containerColor = GoldAccent.copy(alpha = 0.15f),
                iconColor = GoldAccent
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space4),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.prayer_notifications),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = stringResource(R.string.prayer_manage_alerts, totalNotificationOn),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = customColors.secondaryText
                    )
                )
            }

            Icon(
                painter = painterResource(R.drawable.arrowright),
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSm),
                tint = customColors.secondaryText
            )
        }
    }
}

/**
 * Automatic Location Display Card.
 */
@Composable
fun LocationDisplayCard(
    modifier: Modifier = Modifier,
    locationName: String
) {
    Card(
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.avatarMd)
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📍",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space4),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = if (locationName.isNotBlank()) locationName else "Location",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = stringResource(R.string.prayer_auto_location),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = customColors.secondaryText
                    )
                )
            }
        }
    }
}

data class AzanData(
    val id: Int,
    val azanName: String,
    val azanPath: String
)

val listOfAzan = listOf(
    AzanData(1, "Makkah", "makkah.mp3"),
    AzanData(2, "Madinah", "madinah.mp3"),
    AzanData(3, "Al-Aqsa", "alaqsa.mp3")
)

