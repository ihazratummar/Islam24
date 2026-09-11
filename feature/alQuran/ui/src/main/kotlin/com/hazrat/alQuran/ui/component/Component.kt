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
import com.hazrat.alQuran.ui.surah.JuzSurahSegment
import com.hazrat.model.quran.SurahModel
import com.hazrat.ui.common.SurahSvgImage
import com.hazrat.ui.theme.dimens

import androidx.compose.ui.res.stringResource
import com.hazrat.ui.R
import com.hazrat.ui.common.SurahNameProvider
import com.hazrat.utils.toLocalizedDigits
import androidx.compose.ui.platform.LocalLocale

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
                    text = surah.surahNumber.toLocalizedDigits(),
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

            // Right: Surah Name & Meaning (Right aligned)
            val isBengali = LocalLocale.current.platformLocale.language.equals("bn", ignoreCase = true)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = if (isBengali) {
                        SurahNameProvider.getSurahNameBengali(surah.surahNumber)
                    } else {
                        surah.nameTransliterated
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isBengali) {
                        SurahNameProvider.getSurahMeaningBengali(surah.surahNumber)
                    } else {
                        surah.nameEnglish
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Juz Segment Card matching SurahCard aesthetics with SVG Calligraphy and Ayah range.
 *
 * @author hazratummar
 */
@Composable
fun JuzSegmentCard(
    modifier: Modifier = Modifier,
    segment: JuzSurahSegment,
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
            // Left: Surah Number & Calligraphy Image together
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.space16)
            ) {
                Text(
                    text = segment.surahNumber.toLocalizedDigits(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.width(dimens.space24)
                )

                // Calligraphy SVG - Prominent & Large matching SurahCard
                SurahSvgImage(
                    surahNumber = segment.surahNumber,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .height(dimens.space40)
                        .width(dimens.avatarXl)
                )
            }

            // Right: Bengali Name & Ayah Range (Right aligned)
            val isBengali = LocalLocale.current.platformLocale.language.equals("bn", ignoreCase = true)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = if (isBengali) {
                        SurahNameProvider.getSurahNameBengali(segment.surahNumber)
                    } else {
                        SurahNameProvider.getSurahNameEnglish(segment.surahNumber)
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${stringResource(R.string.quran_aya_short)} ${segment.startAyah.toLocalizedDigits()} - ${segment.endAyah.toLocalizedDigits()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}