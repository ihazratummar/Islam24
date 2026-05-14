package com.hazrat.home.ui.component

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.MinimalPrayerData
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil

/**
 * @author Hazrat Ummar Shaikh
 */


@Composable
fun HomeTopCard(
    prayerData: MinimalPrayerData
) {
    val prayerState = rememberPrayerState(prayerTimes = prayerData)
    val isNow = prayerState.isNow

    val prayerName = if (isNow){
        prayerState.currentPrayer?.let { stringResource(it.nameRes) }
    }else{
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
                    .padding(dimens.space16),
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimens.space8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(dimens.space4)
                        ) {
                            if (isNow) {
                                PulsingLiveDot()
                            }
                            Text(
                                text = if (isNow) "Praying Now" else "Next Prayer",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }

                        Text(
                            text = prayerName?:"",
                            style = MaterialTheme.typography.displaySmall.copy(
                                MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.surfaceTint.copy(0.15f),
                            shape = CircleShape
                        )
                    ) {

                        val icon = if (isNow) prayerState.prayerIcon else prayerState.nextPrayerIcon

                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(dimens.space12)
                                .size(dimens.iconMd),
                            tint = prayerState.nextPrayerColor
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
                    Column {
                        Text(
                            text = "Starts At",
                            style = MaterialTheme.typography.labelSmall.copy(
                                MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        Text(
                            text = DateUtil.dateLongToString(
                                prayerState.nextPrayerTimeMillis,
                                "hh:mm a"
                            ),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.W600
                            )
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = if (isNow) "Next Prayer ${stringResource(prayerState.nextPrayer!!.nameRes)}" else "TIME REMAINING",
                            style = MaterialTheme.typography.labelSmall.copy(
                                MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )

                        prayerState.countdownText.split(":").let {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                it.forEachIndexed { index, string ->
                                    Box(
                                        modifier = Modifier
                                            .size(dimens.space48)
                                            .padding(start = dimens.space4, top = dimens.space4)
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
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.W800
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
fun PulsingLiveDot(
    color: Color = Color(0xFF34D399), // emerald-400
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(dimens.space16)
    ) {
        // outer ping ring
        val infiniteTransition = rememberInfiniteTransition(label = "ping")
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = EaseOut),
                repeatMode = RepeatMode.Restart
            ),
            label = "scale"
        )
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = EaseOut),
                repeatMode = RepeatMode.Restart
            ),
            label = "alpha"
        )

        Box(
            modifier = Modifier
                .size(dimens.space8)
                .scale(scale)
                .background(
                    color = color.copy(alpha = alpha),
                    shape = CircleShape
                )
        )

        // inner solid dot
        Box(
            modifier = Modifier
                .size(dimens.space4)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )
    }
}


@Composable
fun CardGradient(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithCache {
                val linear = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFF0C4F52),
                        0.45f to Color(0xFF0F5B5B),
                        1.0f to Color(0xFF1F6359),
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
