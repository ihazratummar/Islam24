package com.hazrat.tasbih.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.toLocalizedDigits

/**
 * Target count selector chips (33, 99, 100, 500, 1000).
 * @author hazratummar
 */
@Composable
fun TasbihTargetSelector(
    selectedTarget: Int,
    onTargetSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val limits = listOf(33, 99, 100, 500, 1000)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        limits.forEach { limit ->
            val isSelected = limit == selectedTarget
            val chipBg = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            val chipTextCol = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimens.cornerFull))
                    .background(chipBg)
                    .clickable { onTargetSelect(limit) }
                    .padding(horizontal = dimens.space16, vertical = dimens.space12),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = limit.toLocalizedDigits(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = chipTextCol,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}
