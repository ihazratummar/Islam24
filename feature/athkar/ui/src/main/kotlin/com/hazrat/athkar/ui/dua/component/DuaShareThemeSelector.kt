package com.hazrat.athkar.ui.dua.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.hazrat.ui.theme.dimens

/**
 * Theme gradient selector row for Dua share card customization.
 * @author hazratummar
 */
@Composable
fun DuaShareThemeSelector(
    selectedTheme: DuaShareTheme,
    onThemeSelect: (DuaShareTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.space16, vertical = dimens.space8),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimens.space16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DuaShareTheme.entries.forEach { theme ->
                val isSelected = theme == selectedTheme
                Box(
                    modifier = Modifier
                        .size(dimens.avatarSm)
                        .clip(CircleShape)
                        .background(theme.outerGradient)
                        .border(
                            width = if (isSelected) dimens.space2 else dimens.divider,
                            color = if (isSelected) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onThemeSelect(theme) }
                )
            }
        }
    }
}
