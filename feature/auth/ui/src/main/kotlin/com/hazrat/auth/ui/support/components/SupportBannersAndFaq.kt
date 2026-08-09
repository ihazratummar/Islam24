package com.hazrat.auth.ui.support.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.auth.ui.support.FaqItem
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Clean sub-component housing Offline Warning Banner, Metrics Summary Card, and FAQ Accordion Row.
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun OfflineWarningBanner(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
        )
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
                    .size(dimens.iconLg)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.alert),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(dimens.iconSm)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = stringResource(R.string.support_offline_title),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = stringResource(R.string.support_offline_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Suppress("NonObservableLocale")
@Composable
fun SupportMetricsCard(
    supportersCount: Int,
    formattedSupportedTotal: String,
    modifier: Modifier = Modifier
) {
    val locale = androidx.compose.ui.platform.LocalConfiguration.current.locales[0]
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.space16, horizontal = dimens.space8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricColumn(
                value = String.format(locale, "%,d", supportersCount),
                label = stringResource(R.string.support_supporters_count),
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .width(dimens.divider)
                    .height(dimens.space32)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )

            MetricColumn(
                value = formattedSupportedTotal,
                label = stringResource(R.string.support_you_supported),
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .width(dimens.divider)
                    .height(dimens.space32)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )

            MetricColumn(
                value = "100%",
                label = stringResource(R.string.support_ad_free),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricColumn(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.space2)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = customColors.secondaryText
        )
    }
}

@Composable
fun FaqAccordionRow(
    faq: FaqItem,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    showDivider: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(faq.questionRes),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(
                    id = if (isExpanded) R.drawable.arrowup else R.drawable.down_arrow
                ),
                contentDescription = null,
                tint = customColors.secondaryText,
                modifier = Modifier.size(dimens.iconSm)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = stringResource(faq.answerRes),
                style = MaterialTheme.typography.bodySmall,
                color = customColors.secondaryText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimens.space16, end = dimens.space16, bottom = dimens.space16)
            )
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.divider)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            )
        }
    }
}
