package com.hazrat.alQuran.ui.surah

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.alQuran.ui.component.SurahCard
import com.hazrat.ui.R
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

data class SurahScreenData(
    val name: String,
    val totalAyah: Int,
    val meaning: String,
    val number: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(
    modifier: Modifier = Modifier,
    surahState: SurahState,
    onSurahClick: (SurahScreenData) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.splash_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(dimens.iconLg)
                                .background(
                                    color = customColors.logoBackground,
                                    shape = RoundedCornerShape(dimens.cornerMd)
                                ),
                            tint = Color.Unspecified
                        )
                        Text(
                            text = "Al-Quran",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.space20)
                .fillMaxSize(),
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shape = RoundedCornerShape(dimens.space16)
                ) {
                    surahState.quranData.forEachIndexed { index, surah ->
                        SurahCard(
                            surah = surah,
                            onClick = {
                                onSurahClick(
                                    SurahScreenData(
                                        name = surah.nameEnglish,
                                        totalAyah = surah.totalAyahs,
                                        meaning = surah.nameTransliterated,
                                        number = surah.surahNumber
                                    )
                                )
                            }
                        )
                        if (index < surahState.quranData.size - 1) {
                            HorizontalDivider()
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(dimens.space32))
            }
        }
    }


}