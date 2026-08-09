package com.hazrat.auth.ui.support.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.auth.ui.support.SubscriptionTier
import com.hazrat.auth.ui.support.SupportTab
import com.hazrat.auth.ui.support.TipTier
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Clean composable component housing Tab Selector, One-Time Tip Cards, and Monthly Subscription Cards.
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun SupportTabSelector(
    modifier: Modifier = Modifier,
    selectedTab: SupportTab,
    onTabSelected: (SupportTab) -> Unit,
    isSupporter: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(dimens.space4),
        horizontalArrangement = Arrangement.spacedBy(dimens.space4)
    ) {
        TabPill(
            text = stringResource(R.string.support_one_time_tip),
            isSelected = selectedTab == SupportTab.ONE_TIME,
            onClick = { onTabSelected(SupportTab.ONE_TIME) },
            modifier = Modifier.weight(1f)
        )
        if (!isSupporter){
            TabPill(
                text = stringResource(R.string.support_monthly_support),
                isSelected = selectedTab == SupportTab.MONTHLY,
                onClick = { onTabSelected(SupportTab.MONTHLY) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TabPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.cornerMd))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(vertical = dimens.space12),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TipTierCard(
    tier: TipTier,
    displayPrice: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .border(
                width = if (isSelected) dimens.divider * 2 else dimens.divider,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.1f
                ),
                shape = RoundedCornerShape(dimens.cornerLg)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            TierIconBox(iconRes = tier.iconRes)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    Text(
                        text = stringResource(tier.titleRes),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    tier.badgeRes?.let { badgeRes ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimens.cornerFull))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                                .padding(horizontal = dimens.space8, vertical = dimens.space2)
                        ) {
                            Text(
                                text = stringResource(badgeRes),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(tier.descRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = customColors.secondaryText
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = displayPrice,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "one-time",
                    style = MaterialTheme.typography.labelSmall,
                    color = customColors.secondaryText.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun SubscriptionTierCard(
    tier: SubscriptionTier,
    displayPrice: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .border(
                width = if (isSelected) dimens.divider * 2 else dimens.divider,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.1f
                ),
                shape = RoundedCornerShape(dimens.cornerLg)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
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
                TierIconBox(iconRes = tier.iconRes)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Text(
                            text = stringResource(tier.titleRes),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        tier.badgeRes?.let { badgeRes ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(dimens.cornerFull))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                                    .padding(horizontal = dimens.space8, vertical = dimens.space2)
                            ) {
                                Text(
                                    text = stringResource(badgeRes),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    Text(
                        text = stringResource(tier.descRes),
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Text(
                        text = displayPrice,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "/ month",
                        style = MaterialTheme.typography.labelSmall,
                        color = customColors.secondaryText.copy(alpha = 0.7f)
                    )
                }
            }

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimens.divider)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    )
                    Spacer(modifier = Modifier.height(dimens.space8))
                    Text(
                        text = stringResource(
                            R.string.support_sub_billing_disclaimer,
                            displayPrice
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun TierIconBox(
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(dimens.iconXl)
            .clip(RoundedCornerShape(dimens.cornerMd))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(dimens.iconMd)
        )
    }
}
