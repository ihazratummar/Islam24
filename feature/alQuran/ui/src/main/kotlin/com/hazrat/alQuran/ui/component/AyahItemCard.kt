package com.hazrat.alQuran.ui.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.hazrat.alQuran.ui.ayah.AyahState
import com.hazrat.alQuran.ui.ayah.AyahUiEvent
import com.hazrat.alQuran.ui.ayah.formatForFont
import com.hazrat.alQuran.ui.ayah.parseTajweedHtml
import com.hazrat.model.al_quran_model.AyahModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Individual Ayah Card composable.
 * Renders Aya number pill badge, Tajweed color coding / plain Arabic text,
 * transliteration, translation, and context menu actions.
 *
 * @author hazratummar
 */
@Composable
fun AyahItemCard(
    ayah: AyahModel,
    surahName: String,
    ayahState: AyahState,
    fontConfig: FontRenderConfig,
    currentFontSize: TextUnit,
    currentLineHeight: TextUnit,
    onEvent: (AyahUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val defaultTextColor = MaterialTheme.colorScheme.onBackground
    val isDarkMode = isSystemInDarkTheme()

    val isCurrentPlaying = (ayah.surahNumber == ayahState.playingSurahNumber) &&
            (ayah.ayahNumber == ayahState.playingAyahNumber)

    val cardBg = if (isCurrentPlaying) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
    } else {
        Color.Transparent
    }

    val playingTextColor = defaultTextColor
    val playingSubTextColor = customColors.secondaryText

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerXl))
            .background(cardBg)
            .clickable { onEvent(AyahUiEvent.OnAyahClick(ayah)) }
            .padding(dimens.space12)
    ) {
        // Context Menu Overlay
        if (ayahState.selectedAyahForMenu?.id == ayah.id) {
            AyahContextMenu(
                expanded = true,
                ayah = ayah,
                onDismissRequest = { onEvent(AyahUiEvent.OnDismissMenu) },
                onPlaySurahClick = { onEvent(AyahUiEvent.OnPlaySurahFrom(ayah.ayahNumber)) },
                onPlayJuzClick = { onEvent(AyahUiEvent.OnPlayJuzFrom(ayah.ayahNumber)) },
                onRepeatAyahClick = { onEvent(AyahUiEvent.OnRepeatAyah(ayah.ayahNumber)) },
                onBookmarkClick = { onEvent(AyahUiEvent.OnToggleBookmark(ayah)) },
                onCopyClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(
                        "Ayah Text",
                        "${ayah.arabicText}\n\n${ayah.transliteration}\n\n${ayah.englishTranslation}"
                    )
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Copied Ayah", Toast.LENGTH_SHORT).show()
                },
                onShareClick = {
                    val shareText = "${ayah.arabicText}\n\n${ayah.transliteration}\n\n${ayah.englishTranslation}\n\n- [$surahName, Aya ${ayah.surahNumber}:${ayah.ayahNumber}]"
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Ayah"))
                }
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            // Aya Pill Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        when {
                            isCurrentPlaying && isDarkMode -> Color(0xFF1B5E57)
                            isCurrentPlaying && !isDarkMode -> Color(0xFFCDE2D5)
                            isDarkMode -> MaterialTheme.colorScheme.outlineVariant
                            else -> MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                    .padding(horizontal = dimens.space12, vertical = dimens.space4)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space4)
                ) {
                    Text(
                        text = "Aya ${ayah.surahNumber}:${ayah.ayahNumber}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = when {
                                isCurrentPlaying && isDarkMode -> Color.White
                                isCurrentPlaying && !isDarkMode -> Color(0xFF00332C)
                                isDarkMode -> MaterialTheme.colorScheme.onBackground
                                else -> MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        )
                    )
                    if (ayah.isBookmarked) {
                        Icon(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Bookmarked",
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(dimens.iconXs)
                        )
                    }
                }
            }

            // Arabic Text — Tajweed colored or plain text based on per-font config
            if (ayah.tajweedText.isNotBlank()) {
                val annotatedAyah = remember(ayah.tajweedText, isCurrentPlaying, isDarkMode, ayahState.selectedFont) {
                    parseTajweedHtml(
                        input = ayah.tajweedText,
                        defaultColor = playingTextColor,
                        fontType = ayahState.selectedFont,
                        enableTajweedColor = fontConfig.enableTajweedColors
                    )
                }
                Text(
                    text = annotatedAyah,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = fontConfig.fontFamily,
                        textDirection = TextDirection.Rtl,
                        fontFeatureSettings = fontConfig.fontFeatureSettings,
                        fontSize = currentFontSize,
                        lineHeight = currentLineHeight,
                        letterSpacing = fontConfig.letterSpacing
                    )
                )
            } else {
                val plainText = ayah.arabicText.formatForFont(ayahState.selectedFont)
                Text(
                    text = plainText,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = fontConfig.fontFamily,
                        color = playingTextColor,
                        textDirection = TextDirection.Rtl,
                        fontFeatureSettings = fontConfig.fontFeatureSettings,
                        fontSize = currentFontSize,
                        lineHeight = currentLineHeight,
                        letterSpacing = fontConfig.letterSpacing
                    )
                )
            }

            // Conditional Transliteration & Translation Visibility
            if (ayahState.showTranslation) {
                // Transliteration
                Text(
                    text = ayah.transliteration,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = playingSubTextColor,
                        textAlign = TextAlign.End
                    )
                )

                // Translation (Bengali / English)
                Text(
                    text = ayah.englishTranslation,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = playingTextColor,
                        textAlign = TextAlign.End,
                        fontSize = 16.sp,
                        lineHeight = 26.sp
                    )
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = dimens.space12),
                color = playingTextColor.copy(alpha = 0.12f)
            )
        }
    }
}
