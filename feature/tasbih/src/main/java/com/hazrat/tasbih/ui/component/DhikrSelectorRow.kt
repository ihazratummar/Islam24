package com.hazrat.tasbih.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.tasbih.domain.model.Tasbih
import com.hazrat.tasbih.domain.util.getDisplayTransliteration
import com.hazrat.ui.theme.dimens

/**
 * Horizontal scrolling Dhikr selector bar.
 * @author hazratummar
 */
@Composable
fun DhikrSelectorRow(
    tasbihList: List<Tasbih>,
    activeTasbih: Tasbih?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
    ) {
        items(
            items = tasbihList,
            key = { it.id }
        ) { tasbih ->
            val isSelected = tasbih.id == activeTasbih?.id
            val bg = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            val textCol = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimens.cornerFull))
                    .background(bg)
                    .clickable { onSelect(tasbih.id) }
                    .padding(horizontal = dimens.space16, vertical = dimens.space8),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tasbih.getDisplayTransliteration(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = textCol,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}
