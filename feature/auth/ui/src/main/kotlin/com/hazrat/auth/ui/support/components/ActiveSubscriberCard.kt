package com.hazrat.auth.ui.support.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Clean composable for active monthly subscriber card display.
 * @author Hazrat Ummar Shaikh
 */
@Composable
fun ActiveSubscriberCard(
    displayPrice: String,
    memberSinceDate: String,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = dimens.divider * 2,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(dimens.cornerLg)
            ),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                // Crown/Star Icon Box
                Box(
                    modifier = Modifier
                        .size(dimens.iconXl)
                        .clip(RoundedCornerShape(dimens.cornerMd))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconMd)
                    )
                }

                // Title, Badge, and Subtitle Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Text(
                            text = "Active Subscriber",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        // ACTIVE Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimens.cornerFull))
                                .background(Color(0xFF2E7D32).copy(alpha = 0.15f))
                                .padding(horizontal = dimens.space8, vertical = dimens.space2)
                        ) {
                            Text(
                                text = "ACTIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }

                    Text(
                        text = "Monthly Patron · $displayPrice",
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText
                    )

                    Text(
                        text = "Member since $memberSinceDate",
                        style = MaterialTheme.typography.labelSmall,
                        color = customColors.secondaryText.copy(alpha = 0.7f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.divider)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "💚 JazakAllah Khair for your support",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = customColors.secondaryText,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimens.cornerFull))
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
                        .clickable(onClick = onCancelClick)
                        .padding(horizontal = dimens.space12, vertical = dimens.space4)
                ) {
                    Text(
                        text = "Cancel Subscription",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
