package com.hazrat.tasbih.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import com.hazrat.tasbih.domain.model.Tasbih
import com.hazrat.tasbih.domain.util.getDisplayMeaning
import com.hazrat.tasbih.domain.util.getDisplayTransliteration
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

private fun String.cleanUthmanic(): String {
    return this
        .replace("\u06DF", "")
        .replace('\u0652', '\u06e1')
        .replace("\uFEFF", "")
        .trim()
}

/**
 * Royal Islamic Tasbih Card displaying official Scheherazade Arabic calligraphy, dial counter, targets, and action buttons.
 * @author hazratummar
 */
@Composable
fun RoyalTasbihCard(
    tasbih: Tasbih,
    selectedTarget: Int,
    onCount: () -> Unit,
    onReset: () -> Unit,
    onUndo: () -> Unit,
    onTargetSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            width = dimens.divider,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
        ),
        elevation = CardDefaults.cardElevation(dimens.elevation2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Official Arabic Calligraphy in Scheherazade Font
            val cleanArabic = remember(tasbih.arabicText) { tasbih.arabicText.cleanUthmanic() }
            Text(
                text = cleanArabic,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = ScheherazadeFontFamily,
                    fontFeatureSettings = "cv62",
                    textDirection = TextDirection.Rtl
                ),
                color = customColors.accentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimens.space4))

            // Transliteration Subtitle
            val displayTransliteration = tasbih.getDisplayTransliteration()
            val displayMeaning = tasbih.getDisplayMeaning()

            Text(
                text = displayTransliteration,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            // Meaning Subtitle
            if (displayMeaning.isNotBlank() && displayMeaning != displayTransliteration) {
                Spacer(modifier = Modifier.height(dimens.space2))
                Text(
                    text = displayMeaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = customColors.secondaryText,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(dimens.space24))

            // Royal Radial Dial Counter
            TasbihDialCounter(
                currentCount = tasbih.currentCount,
                targetLimit = selectedTarget,
                onCount = onCount
            )

            Spacer(modifier = Modifier.height(dimens.space24))

            // Target Limit Chips Row
            TasbihTargetSelector(
                selectedTarget = selectedTarget,
                onTargetSelect = onTargetSelect
            )

            Spacer(modifier = Modifier.height(dimens.space20))

            // Action Buttons (Count, Reset, Undo)
            TasbihActionButtons(
                onCount = onCount,
                onReset = onReset,
                onUndo = onUndo
            )
        }
    }
}
