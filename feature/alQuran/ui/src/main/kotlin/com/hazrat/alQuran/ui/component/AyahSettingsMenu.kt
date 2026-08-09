package com.hazrat.alQuran.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Data model for Quranic font choices in the settings menu.
 */
data class QuranFontOption(
    val id: String,
    val name: String,
    val subtitle: String
)

val AvailableQuranFonts = listOf(
    QuranFontOption("SCHEHERAZADE", "Scheherazade", "Naskh"),
    QuranFontOption("INDOPAK", "IndoPak", "Subcontinent"),
    QuranFontOption("UTHMANIC", "Uthmanic", "Mushaf")
)

/**
 * Premium Industry-Grade Top Dropdown Settings Card for Ayah Screen.
 * Provides real-time font selection, font size adjustment, and translation toggle.
 *
 * @author hazratummar
 */
@Composable
fun AyahSettingsMenu(
    selectedFont: String,
    fontSize: Int,
    showTranslation: Boolean,
    onFontSelect: (String) -> Unit,
    onFontSizeChange: (Int) -> Unit,
    onToggleTranslation: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    val cardBg = if (isDark) Color(0xFF19252B) else Color(0xFFF0F5F3)
    val cardBorder = if (isDark) Color(0xFF2B3D46) else Color(0xFFD6E4DF)
    val primaryColor = MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.space16, vertical = dimens.space8),
        shape = RoundedCornerShape(dimens.cornerXl),
        color = cardBg,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // Section 1: Font Selector Header & Segmented Chips
            Column(verticalArrangement = Arrangement.spacedBy(dimens.space12)) {
                Text(
                    text = "Arabic Font",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    AvailableQuranFonts.forEach { fontOption ->
                        val isSelected = (selectedFont == fontOption.id)

                        val animatedChipBg by animateColorAsState(
                            targetValue = if (isSelected) primaryColor else (if (isDark) Color(0xFF22323A) else Color(0xFFE2EBE8)),
                            animationSpec = tween(durationMillis = 200),
                            label = "chipBg"
                        )
                        val animatedTitleColor by animateColorAsState(
                            targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                            animationSpec = tween(durationMillis = 200),
                            label = "titleColor"
                        )
                        val animatedSubtitleColor by animateColorAsState(
                            targetValue = if (isSelected) Color.White.copy(alpha = 0.85f) else customColors.secondaryText,
                            animationSpec = tween(durationMillis = 200),
                            label = "subtitleColor"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(dimens.cornerLg))
                                .background(animatedChipBg)
                                .clickable { onFontSelect(fontOption.id) }
                                .padding(vertical = dimens.space8, horizontal = dimens.space4),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = fontOption.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = animatedTitleColor
                                    )
                                )
                                Text(
                                    text = fontOption.subtitle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = animatedSubtitleColor
                                    )
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(
                color = if (isDark) Color(0xFF26373F) else Color(0xFFE0ECE8),
                thickness = 1.dp
            )

            // Section 2: Font Size Adjuster (- [ 36 sp ] +)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Font Size",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    val btnBg = if (isDark) Color(0xFF253740) else Color(0xFFE0ECE8)

                    // Minus Decrement Button (Uses ic_minus)
                    IconButton(
                        onClick = { if (fontSize > 20) onFontSizeChange(fontSize - 2) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(btnBg)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_minus),
                            contentDescription = "Decrease Font Size",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Font Size Value Display
                    Text(
                        text = "$fontSize sp",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    // Plus Increment Button (Uses ic_add)
                    IconButton(
                        onClick = { if (fontSize < 44) onFontSizeChange(fontSize + 2) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(btnBg)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Increase Font Size",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            HorizontalDivider(
                color = if (isDark) Color(0xFF26373F) else Color(0xFFE0ECE8),
                thickness = 1.dp
            )

            // Section 3: Translation Toggle Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Translation",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Switch(
                    checked = showTranslation,
                    onCheckedChange = onToggleTranslation,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = primaryColor,
                        uncheckedThumbColor = if (isDark) Color(0xFFB0BEC5) else Color(0xFF78909C),
                        uncheckedTrackColor = if (isDark) Color(0xFF253740) else Color(0xFFE0ECE8)
                    )
                )
            }
        }
    }
}
