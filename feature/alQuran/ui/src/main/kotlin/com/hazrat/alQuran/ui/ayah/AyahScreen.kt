package com.hazrat.alQuran.ui.ayah

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.hazrat.alQuran.ui.surah.SurahScreenData
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens


/**
 * @author hazratummar
 */

private fun String.cleanUthmanic(): String {
    return this
        .replace("\u06DF", "")       // Remove Small High Rounded Zero (the small circle mark)
        .replace('\u0652', '\u06e1') // Convert standard round sukun to comma sukun
        .replace("\uFEFF", "")       // Remove BOM
        .trim()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahScreen(
    modifier: Modifier = Modifier,
    ayahState: AyahState,
    onBackClick: () -> Unit,
    surahScreenData: SurahScreenData
) {

    // Exact Bismillah from quran-uthmani API
    val bismillahRaw = "\u0628\u0650\u0633\u0652\u0645\u0650 \u0671\u0644\u0644\u0651\u064e\u0647\u0650 \u0671\u0644\u0631\u0651\u064e\u062d\u0652\u0645\u064e\u0670\u0646\u0650 \u0671\u0644\u0631\u0651\u064e\u062d\u0650\u064a\u0645\u0650"
    val bismillahText = bismillahRaw.cleanUthmanic()

    Scaffold(
        containerColor = Color(0xFF272727),
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(dimens.space4)
                    ) {
                        Text(
                            text = surahScreenData.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${surahScreenData.meaning} • ${surahScreenData.totalAyah} verses",
                            style = MaterialTheme.typography.bodySmall,
                            color = customColors.secondaryText
                        )
                    }
                },
                navigationIcon = {
                    BackIcon(
                        onBackClick = onBackClick
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { paddingValues ->


        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.space20),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            item(key = "bismillah") {
                val surahNumber = surahScreenData.number
                if (surahNumber != 1 && surahNumber != 9) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = bismillahText.cleanUthmanic(),
                            modifier = Modifier.padding(vertical = dimens.space32),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = ScheherazadeFontFamily,
                                fontSize = 32.sp
                            )
                        )
                    }
                }
            }
            items(ayahState.ayahs) { ayah ->

                var ayahText = ayah.arabicText.cleanUthmanic()

                if (ayah.ayahNumber == 1 && ayah.surahNumber != 1 && ayah.surahNumber != 9) {
                    ayahText = ayahText.replace(bismillahText, "").trim()
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimens.space12),
                    verticalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    // Aya Pill Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .padding(horizontal = dimens.space12, vertical = dimens.space4)
                    ) {
                        Text(
                            text = "Aya ${ayah.surahNumber}:${ayah.ayahNumber}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    // Arabic Text
                    Text(
                        text = ayahText,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = ScheherazadeFontFamily,
                            color = MaterialTheme.colorScheme.onBackground,
                            textDirection = TextDirection.Rtl,
                            fontSize = 30.sp,
                            lineHeight = 60.sp
                        )
                    )

                    // Transliteration
                    Text(
                        text = ayah.transliteration,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = customColors.secondaryText,
                            textAlign = TextAlign.End
                        )
                    )

                    // Translation
                    Text(
                        text = ayah.englishTranslation,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.End
                        )
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(top = dimens.space12),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
                    )
                }

            }
        }
    }
}