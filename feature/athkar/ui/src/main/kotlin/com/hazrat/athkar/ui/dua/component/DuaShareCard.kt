package com.hazrat.athkar.ui.dua.component

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
import com.hazrat.model.DuaItemModel
import com.hazrat.ui.R
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.dimens

private fun String.cleanUthmanic(): String {
    return this
        .replace("\u06DF", "")
        .replace('\u0652', '\u06e1')
        .replace("\uFEFF", "")
        .trim()
}

/**
 * Branded Visual Card for Dua sharing with official app logo and non-overlapping footer.
 * @author hazratummar
 */
@Composable
fun DuaShareCard(
    dua: DuaItemModel,
    chapterTitle: String,
    theme: DuaShareTheme,
    modifier: Modifier = Modifier
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
            // Chapter / Topic Title
            if (chapterTitle.isNotBlank()) {
                Text(
                    text = chapterTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = theme.accentColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Inner Card for Dua Content
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
                    // Arabic Text
                    val plainText = remember(dua.arabicText) { dua.arabicText.cleanUthmanic() }
                    Text(
                        text = plainText,
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

                    // English Translation
                    Text(
                        text = dua.translation,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }
            }

            // Footer row with Islam 24 app logo watermark and source reference
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Official Islam 24 App Logo & Branding
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

                // Reference text properly constrained so it never collides
                if (dua.reference.isNotBlank()) {
                    Text(
                        text = dua.reference,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .padding(start = dimens.space12)
                            .weight(1f, fill = false)
                    )
                }
            }
        }
    }
}
