package com.hazrat.islam24.core.presentation.al_quran

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazrat.islam24.R
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
                windowInsets = WindowInsets(top = dimens.size20),
                title = {
                    Row(
                        modifier = Modifier.padding(vertical = dimens.size10),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Al-Quran",
                            style = MaterialTheme.typography.headlineSmall.copy(
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
        },
        contentWindowInsets = WindowInsets(bottom = 0.dp),
    ) { paddingValues ->
        if (quranState.arQuranData != null && quranState.quranEnData != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = dimens.size20)
            ) {

                LastReadCard(
                    onLastReadClick = {
                        if (quranState.lastReadSurah != null){
                            onSurahClick(
                                quranState.lastReadSurah,
                                quranState.lastReadAyah,
                                true
                            )
                        }
                    },
                    lastReadSurahName = quranState.lastReadSurahName ,
                    lastReadAyah = quranState.lastReadAyah?: 0
                )
                // Static card content
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
                                        surahName = when (systemLanguage) {
                                            "bn" -> quranBn?.get(surah.number - 1)?.transliteration
                                                ?: ""

                                            else -> surah.englishName
                                        },
                                        surahVersesCount = surah.ayahs.size,
                                        type = when (systemLanguage) {
                                            "bn" -> {
                                                when (surah.revelationType) {
                                                    "Meccan" -> "মাক্কি"
                                                    "Medinan" -> "মদীনা"
                                                    else -> ""
                                                }
                                            }

                                            else -> surah.revelationType
                                        },
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
                                        index = index,
                                        ayat = "${juzDetails.start} - ${juzDetails.end}"
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

                                            else -> quranEn[it.surahNumber - 1].verses[it.ayahNumber - 1].translation
                                        }
                                        val surahAyahText = when (systemLanguage) {
                                            "bn" -> "${quranBn?.get(it.surahNumber - 1)?.transliteration}"
                                            else -> "${quranState.arQuranData[it.surahNumber - 1].englishName} "
                                        } + " - ${stringResource(R.string.ayah)}: ${it.ayahNumber}"

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