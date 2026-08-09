package com.hazrat.auth.ui.support.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.profile.SupporterTickerModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.toCurrencySymbol

/**
 * Clean floating overlay popup for real-time WebSocket supporter events at bottom of screen.
 * @author Hazrat Ummar Shaikh
 */
@Composable
fun SupportLiveTickerOverlay(
    visibleTicker: SupporterTickerModel?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visibleTicker != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.space16, vertical = dimens.space16)
    ) {
        visibleTicker?.let { ticker ->
            Card(
                shape = RoundedCornerShape(dimens.cornerFull),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = dimens.space8),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = dimens.divider,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(dimens.cornerFull)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(dimens.space12),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimens.iconXl)
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
                            modifier = Modifier.size(dimens.iconMd)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimens.space2)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                        ) {
                            Text(
                                text = ticker.donorName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Box(
                                modifier = Modifier
                                    .size(dimens.space8)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50))
                            )
                        }

                        val actionText = when (ticker.type.uppercase()) {
                            "TIP" -> {
                                val symbol = ticker.currency?.toCurrencySymbol() ?: "$"
                                val amt = ticker.amount?.let { if (it % 1.0 == 0.0) "${it.toInt()}" else "$it" } ?: "0"
                                "just tipped $symbol$amt"
                            }
                            else -> "just subscribed as Monthly Supporter"
                        }

                        Text(
                            text = actionText,
                            style = MaterialTheme.typography.bodySmall,
                            color = customColors.secondaryText
                        )
                    }
                }
            }
        }
    }
}
