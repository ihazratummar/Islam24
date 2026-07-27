package com.hazrat.alQuran.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.al_quran_model.SurahModel
import com.hazrat.ui.common.SurahSvgImage
import com.hazrat.ui.theme.dimens

/**
 * Surah Card matching user's reference design with direct Coil SVG Calligraphy rendering.
 *
 * @author hazratummar
 */
@Composable
fun SurahCard(
    modifier: Modifier = Modifier,
    surah: SurahModel,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.space16, vertical = dimens.space12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Number & Calligraphy Image together
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space16)
            ) {
                Text(
                    text = surah.surahNumber.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.width(dimens.space24)
                )

                // Calligraphy SVG - Prominent & Large using design system dimens!
                SurahSvgImage(
                    surahNumber = surah.surahNumber,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .height(dimens.space40)
                        .width(dimens.avatarXl)
                )
            }

            // Right: Transliterated Name & English Meaning (Right aligned)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = surah.nameTransliterated,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = surah.nameEnglish,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}