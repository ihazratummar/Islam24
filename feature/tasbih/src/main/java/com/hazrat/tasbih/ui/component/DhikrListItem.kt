package com.hazrat.tasbih.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import com.hazrat.tasbih.domain.model.Tasbih
import com.hazrat.tasbih.domain.util.getDisplayMeaning
import com.hazrat.tasbih.domain.util.getDisplayTransliteration
import com.hazrat.ui.R
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.toLocalizedDigits

private fun String.cleanUthmanic(): String {
    return this
        .replace("\u06DF", "")
        .replace('\u0652', '\u06e1')
        .replace("\uFEFF", "")
        .trim()
}

/**
 * Polished Dhikr List Item with Scheherazade calligraphy and clean surface styling.
 * @author hazratummar
 */
@Composable
fun DhikrListItem(
    tasbih: Tasbih,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(dimens.elevation1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.space16, vertical = dimens.space12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space4)
            ) {
                // Arabic Title in Scheherazade Calligraphy
                val cleanArabic = remember(tasbih.arabicText) { tasbih.arabicText.cleanUthmanic() }
                Text(
                    text = cleanArabic,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = ScheherazadeFontFamily,
                        fontFeatureSettings = "cv62",
                        textDirection = TextDirection.Rtl
                    ),
                    color = if (isSelected) customColors.accentColor else MaterialTheme.colorScheme.onSurface
                )

                // Transliteration
                val displayTransliteration = tasbih.getDisplayTransliteration()
                val displayMeaning = tasbih.getDisplayMeaning()

                Text(
                    text = displayTransliteration,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                    ),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )

                // Translated Meaning
                if (displayMeaning.isNotBlank() && displayMeaning != displayTransliteration) {
                    Text(
                        text = displayMeaning,
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(dimens.space8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Count Pill Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimens.cornerFull))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest)
                        .padding(horizontal = dimens.space12, vertical = dimens.space4)
                ) {
                    Text(
                        text = tasbih.currentCount.toLocalizedDigits(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Favorite Star Button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(dimens.iconLg)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (tasbih.isFavorite) R.drawable.star else R.drawable.outlinstar
                        ),
                        contentDescription = "Favorite",
                        tint = if (tasbih.isFavorite) customColors.accentColor else customColors.secondaryText,
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }
            }
        }
    }
}
