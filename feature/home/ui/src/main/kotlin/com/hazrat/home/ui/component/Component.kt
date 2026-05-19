package com.hazrat.home.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.EventType
import com.hazrat.model.MinimalPrayerData
import com.hazrat.ui.R
import com.hazrat.ui.common.LongText
import com.hazrat.ui.common.PulsingLiveDot
import com.hazrat.ui.common.rememberPrayerState
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil
import kotlin.random.Random

/**
 * @author Hazrat Ummar Shaikh
 */


@Composable
fun HomeTopCard(
    prayerData: MinimalPrayerData,
    onLogPrayerClick: () -> Unit
) {
    val prayerState = rememberPrayerState(prayerTimes = prayerData)
    val isNow = prayerState.isNow

    val prayerName = if (isNow) {
        prayerState.currentPrayer?.let { stringResource(it.nameRes) }
    } else {
        prayerState.nextPrayer?.let { stringResource(it.nameRes) }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        CardGradient {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.space12),
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimens.space8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimens.space4),
                    ) {
                        if (isNow) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(dimens.space8),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PulsingLiveDot()
                                Text(
                                    text = "TIME FOR",
                                    color = customColors.emerald,
                                    fontWeight = FontWeight.W600,
                                    style = MaterialTheme.typography.bodySmall,

                                    )
                            }
                        } else {
                            Text(
                                text = "Next Prayer",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }

                        Text(
                            text = prayerName ?: "",
                            style = MaterialTheme.typography.displayMedium.copy(
                                MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.W700
                            )
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceTint.copy(0.50f),
                                shape = CircleShape
                            )
                            .border(
                                width = dimens.elevation2,
                                color = MaterialTheme.colorScheme.primary.copy(0.4f),
                                shape = CircleShape
                            )
                    ) {
                        val icon = if (isNow) prayerState.prayerIcon else prayerState.nextPrayerIcon
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(dimens.space12)
                                .size(dimens.iconLg),
                            tint = customColors.iconColor
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimens.space8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {

                        val time = if (isNow) {
                            DateUtil.dateLongToString(
                                prayerState.currentPrayerTime,
                                "hh:mm a"
                            )
                        } else {
                            DateUtil.dateLongToString(
                                prayerState.nextPrayerTimeMillis,
                                "hh:mm a"
                            )
                        }

                        Text(
                            text = if (isNow) "Started at $time" else "Starts At",
                            style = MaterialTheme.typography.bodySmall.copy(
                                MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        if (isNow) {

                            Card(
                                onClick = onLogPrayerClick,
                                colors = CardDefaults.cardColors(
                                    containerColor = customColors.accentColor,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                shape = RoundedCornerShape(dimens.cornerMd),
                                modifier = Modifier.height(dimens.space40)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(dimens.space8),
                                    modifier = Modifier
                                        .padding(horizontal = dimens.space12)
                                        .fillMaxHeight()
                                        .align(Alignment.CenterHorizontally)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.double_check),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(dimens.iconSx)
                                    )
                                    Text(
                                        text = "Log Prayer",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = time,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.W600
                                )
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Text(
                            text = if (isNow) "Next Prayer ${stringResource(prayerState.nextPrayer!!.nameRes)}" else "TIME REMAINING",
                            style = MaterialTheme.typography.bodySmall.copy(
                                MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )

                        prayerState.countdownText.split(":").let {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                it.forEachIndexed { index, string ->
                                    Box(
                                        modifier = Modifier
                                            .size(dimens.space48)
                                            .background(
                                                color = MaterialTheme.colorScheme.surfaceTint.copy(
                                                    0.15f
                                                ),
                                                shape = RoundedCornerShape(dimens.cornerMd)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = string,
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.W700
                                            ),
                                            modifier = Modifier.padding(dimens.space8)
                                        )
                                    }

                                    if (index != it.size - 1) {
                                        Text(
                                            text = ":",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurface
                                            ),
                                            modifier = Modifier.padding(start = dimens.space4)
                                        )
                                        Spacer(Modifier.width(dimens.space4))
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

    }
}



@Composable
fun DashboardTile(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = R.drawable.calendar1,
    label: String = "HIJRI DATE",
    mainText: String = "15 Rabi' al-Awwal",
    bottomLabel: String = "1447 AH"
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier.padding(dimens.space16),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimens.space4)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space8)
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .size(dimens.iconSx)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(Modifier.height(dimens.space4))

            Text(
                text = mainText,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.W700
                )
            )
            LongText(
                text = bottomLabel,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium
                ),

            )
        }
    }
}




@Composable
fun CardGradient(
    content: @Composable () -> Unit
) {
    val gradientColors = customColors.homeCardGradient
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithCache {
                val linear = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to gradientColors[0],
                        0.45f to gradientColors[1],
                        1.0f to gradientColors[2],
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )

                val radial = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF7A8F63).copy(alpha = 0.30f), // yellowish teal glow
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.88f, size.height * 0.20f),
                    radius = size.width * 0.45f
                )

                onDrawBehind {
                    drawRect(linear)
                    drawRect(radial)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}


@Composable
fun HomeScreenEventCard(
    eventName: String,
    eventDate: String,
    eventType: EventType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
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
                        color = eventType.containerColor().copy(0.2f),
                        shape = RoundedCornerShape(dimens.cornerLg)
                    ),
                contentAlignment = Alignment.Center,

                ) {
                Icon(
                    painter = painterResource(eventType.icon()),
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconLg),
                    tint = Color.Unspecified
                )
            }

            Column (
                verticalArrangement = Arrangement.spacedBy(dimens.space8),
                modifier = Modifier.weight(1f)
            ){
                val randomInt = Random.nextInt(5000, 10000)
                LongText(
                    text = eventName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    ),
                    repeatDelay = randomInt,
                    delay = randomInt
                )

                Text(
                    text = eventDate,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 2,
                )
            }

            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .background(
                    color = eventType.color().copy(0.1f),
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
