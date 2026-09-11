package com.hazrat.alQuran.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.hazrat.model.quran.AyahModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.dimens

/**
 * Branded Visual Card for Quran Ayah sharing with official app logo and theme styling.
 * @author hazratummar
 */
@Composable
fun AyahShareCard(
    ayah: AyahModel,
    surahName: String,
    theme: QuranShareTheme,
    modifier: Modifier = Modifier,
    translationSource: String = "MUHIUDDIN"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerXl))
            .background(theme.outerGradient)
            .border(
                width = dimens.space2,
                color = theme.accentColor.copy(alpha = 0.35f),
                shape = RoundedCornerShape(dimens.cornerXl)
            )
            .padding(dimens.space16)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // Surah & Ayah Title Header
            Text(
                text = "$surahName • Ayah ${ayah.surahNumber}:${ayah.ayahNumber}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = theme.accentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Inner Card for Ayah Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimens.cornerLg))
                    .background(theme.cardBackground.copy(alpha = 0.85f))
                    .border(
                        width = dimens.divider,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(dimens.cornerLg)
                    )
                    .padding(dimens.space16)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    // Clean Arabic Text
                    val plainArabic = remember(ayah.arabicText) {
                        ayah.arabicText
                            .replace(Regex("\\[.*?\\]"), "")
                            .replace("\u06DF", "")
                            .replace('\u0652', '\u06e1')
                            .replace("\uFEFF", "")
                            .trim()
                    }

                    Text(
                        text = plainArabic,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = ScheherazadeFontFamily,
                            color = Color.White,
                            textDirection = TextDirection.Rtl,
                            fontFeatureSettings = "cv62",
                            fontSize = 26.sp,
                            lineHeight = 52.sp
                        )
                    )

                    // Transliteration
                    val transliterationText = remember(ayah) { ayah.getActiveTransliteration() }
                    if (transliterationText.isNotBlank()) {
                        Text(
                            text = transliterationText,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        )
                    }

                    // Translation
                    val activeTranslation = remember(ayah, translationSource) {
                        ayah.getTranslation(translationSource)
                    }
                    if (activeTranslation.isNotBlank()) {
                        Text(
                            text = activeTranslation,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.95f)
                            )
                        )
                    }
                }
            }

            // Footer row with Islam 24 app logo watermark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash_logo),
                        contentDescription = "Islam 24",
                        modifier = Modifier
                            .size(dimens.iconMd)
                            .clip(CircleShape)
                    )
                    Text(
                        text = "Islam 24",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor
                    )
                }

                Text(
                    text = "Al-Quran • ${ayah.surahNumber}:${ayah.ayahNumber}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
