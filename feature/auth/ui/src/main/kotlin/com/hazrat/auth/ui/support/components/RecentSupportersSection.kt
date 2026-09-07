package com.hazrat.auth.ui.support.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.profile.SupporterTickerModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.toCurrencySymbol

/**
 * Clean animated horizontal carousel for recent community supporters (top 10 cached tickers).
 * @author Hazrat Ummar Shaikh
 */
@Composable
fun RecentSupportersSection(
    recentTickers: List<SupporterTickerModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.space12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.support_recent_supporters_caps),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = customColors.secondaryText.copy(alpha = 0.7f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space8)
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.space8)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )
                Text(
                    text = stringResource(R.string.support_live_activity),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimens.space12),
            contentPadding = PaddingValues(horizontal = dimens.space4)
        ) {
            itemsIndexed(
                items = recentTickers,
                key = { index, ticker -> ticker.eventId.ifEmpty { "$index-${ticker.donorName}" } }
            ) { index, ticker ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInHorizontally(),
                    exit = fadeOut() + slideOutHorizontally()
                ) {
                    Card(
                        shape = RoundedCornerShape(dimens.cornerLg),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier
                            .width(dimens.compButton * 4.5f)
                            .border(
                                width = dimens.divider,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(dimens.cornerLg)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(dimens.space12),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(dimens.iconLg)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (ticker.type == "TIP") R.drawable.ic_sadaqah else R.drawable.heart
                                    ),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(dimens.iconSm)
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(dimens.space2)
                            ) {
                                Text(
                                    text = ticker.donorName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )

                                val actionText = when (ticker.type.uppercase()) {
                                    "TIP" -> {
                                        val symbol = ticker.currency?.toCurrencySymbol() ?: "$"
                                        val amt = ticker.amount?.let { if (it % 1.0 == 0.0) "${it.toInt()}" else "$it" } ?: "0"
                                        "Tipped $symbol$amt"
                                    }
                                    else -> "Monthly Supporter"
                                }

                                Text(
                                    text = actionText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = customColors.secondaryText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
