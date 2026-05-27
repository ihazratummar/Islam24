package com.hazrat.alQuran.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.hazrat.model.al_quran_model.SurahModel
import com.hazrat.ui.theme.NotoNaskhFontFamily
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import kotlin.math.cos
import kotlin.math.sin


/**
 * @author hazratummar
 * Created on 27/05/26
 */


// Hexagon badge (approximated with a rotated box)
@Composable
fun SurahNumberBadge(number: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(dimens.space40)
            .drawBehind {
                // Draw hexagon outline
                val path = hexagonPath(size)
                drawPath(
                    path = path,
                    color = Color(0xFF4DB6AC),
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
    ) {
        Text(
            text = number.toString(),
            color = Color(0xFF4DB6AC),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun hexagonPath(size: Size): Path {
    val path = Path()
    val cx = size.width / 2f
    val cy = size.height / 2f
    val r = size.minDimension / 2f * 0.9f
    for (i in 0..5) {
        val angle = Math.toRadians((60.0 * i) - 30.0)
        val x = cx + r * cos(angle).toFloat()
        val y = cy + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}


@Composable
fun SurahCard(
    modifier: Modifier = Modifier,
    surah: SurahModel,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(dimens.space12)
            .clickable(
                onClick = onClick
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SurahNumberBadge(
            surah.surahNumber
        )
        Spacer(Modifier.width(dimens.space8))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimens.space4)
        ) {
            Text(
                text = surah.nameEnglish,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = "${surah.nameTransliterated} • ${surah.totalAyahs} verses • ${surah.type}",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = customColors.secondaryText
                )
            )
        }

        Text(
            text = surah.nameArabic,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = ScheherazadeFontFamily,
                color = MaterialTheme.colorScheme.onBackground,
                textDirection = TextDirection.Rtl,
                fontFeatureSettings = "calt, kern, liga, clig, ss01, ss03"
            )
        )
    }
}