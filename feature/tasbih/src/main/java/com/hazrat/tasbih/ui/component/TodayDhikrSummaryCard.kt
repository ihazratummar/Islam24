package com.hazrat.tasbih.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.toLocalizedDigits

/**
 * Summary card showing total Dhikrs recited today.
 * @author hazratummar
 */
@Composable
fun TodayDhikrSummaryCard(
    todayTotal: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(dimens.elevation1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimens.space12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.avatarLg)
                        .clip(RoundedCornerShape(dimens.cornerLg))
                        .background(customColors.accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.zikir),
                        contentDescription = stringResource(R.string.tasbih_total_today),
                        tint = customColors.accentColor,
                        modifier = Modifier.size(dimens.iconMd)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(dimens.space2)
                ) {
                    Text(
                        text = stringResource(R.string.tasbih_total_today),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.tasbih_may_allah_accept),
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText
                    )
                }
            }

            Text(
                text = todayTotal.toLocalizedDigits(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = customColors.accentColor
            )
        }
    }
}
