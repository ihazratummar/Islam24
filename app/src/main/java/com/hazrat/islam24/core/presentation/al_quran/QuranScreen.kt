package com.hazrat.islam24.core.presentation.al_quran

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.findLocalQuranSurahNumberByAyahNumber
import com.hazrat.islam24.ui.theme.Lafadz
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.getSystemLanguage

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(
    modifier: Modifier = Modifier,
    onSurahClick: (Int, Int?, Boolean) -> Unit,
    quranState: QuranState,
    refresh: () -> Unit
) {
    val quranBn = quranState.quranBnData
    val systemLanguage = getSystemLanguage()
    val quranEn = quranState.quranEnData
    // Tab state
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(quranState) {
        refresh()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(top = dimens.size20, bottom = dimens.size5),
                title = {
                    Row(
                        modifier = Modifier.padding(vertical = dimens.size10),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Al-Quran",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Spacer(Modifier.width(dimens.size2))
                        Text(
                            text = "L",
                            fontFamily = Lafadz,
                            fontSize = 50.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = dimens.size20)
        ) {
            // Static card content
            LastReadCard(
                onLastReadClick = {
                    onSurahClick(
                        quranState.lastReadSurah,
                        quranState.lastReadAyah,
                        true
                    )
                },
                lastReadSurahName = quranState.lastReadSurahName ?: "Surah Name",
                lastReadAyah = quranState.lastReadAyah
            )

            // Tabs and content
            if (quranState.arQuranData != null) {
                val quranMetaDataJuz = quranState.juzData ?: return@Scaffold
                TabRowComponent(
                    modifier = Modifier.weight(1f),
                    contentScreens = listOf(
                        {
                            LazyColumn {
                                items(quranState.arQuranData) { surah ->
                                    SurahCard(
                                        isLastReadSurah = quranState.lastReadSurah == surah.number,
                                        surahNumber = surah.number,
                                        surahName = surah.englishName,
                                        surahVersesCount = surah.ayahs.size,
                                        type = surah.revelationType,
                                        onSurahClick = { onSurahClick(surah.number, null, true) },
                                        surahNameTranslation = when (systemLanguage) {
                                            "bn" -> quranBn?.get(surah.number - 1)?.translation
                                                ?: ""

                                            else -> surah.englishNameTranslation
                                        }
                                    )
                                }
                            }
                        },
                        {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                itemsIndexed(quranMetaDataJuz.juzData) { index, juzDetails ->
                                    val surahNumber = findLocalQuranSurahNumberByAyahNumber(
                                        quranItems = quranState.arQuranData,
                                        ayahNumber = juzDetails.start
                                    )
                                    JuzCard(
                                        onClick = {
                                            onSurahClick(
                                                surahNumber?.first ?: 1,
                                                surahNumber?.second ?: 1,
                                                false
                                            )
                                        },
                                        index = index
                                    )
                                }
                            }
                        },
                        {
                            LazyColumn {
                                if (quranState.favoritesList != null) {
                                    items(quranState.favoritesList) {
                                        val translationText = when (systemLanguage) {
                                            "bn" -> quranBn?.get(it.surahNumber - 1)?.verses?.get(
                                                it.ayahNumber - 1
                                            )?.translation ?: ""

                                            else -> quranEn?.get(it.surahNumber - 1)?.verses?.get(
                                                it.ayahNumber - 1
                                            )?.translation ?: ""
                                        }
                                        val surahAyahText =
                                            "${quranState.arQuranData[it.surahNumber - 1].englishName} - Ayat: ${it.ayahNumber}"
                                        FavoriteAyahCard(
                                            onClick = {
                                                onSurahClick(
                                                    it.surahNumber,
                                                    it.ayahNumber,
                                                    false
                                                )
                                            },
                                            translationText = translationText,
                                            surahAyahText = surahAyahText
                                        )
                                    }
                                }
                            }
                        }
                    ),
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )
            }
        }
    }
}