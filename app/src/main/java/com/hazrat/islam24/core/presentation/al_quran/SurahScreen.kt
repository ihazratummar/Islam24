package com.hazrat.islam24.core.presentation.al_quran

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.R
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.ArAyah
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_transliteration.TransliterationVerse
import com.hazrat.islam24.ui.theme.Kitab
import com.hazrat.islam24.ui.theme.Uthmani
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.getSystemLanguage
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import java.text.NumberFormat
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SurahScreen(
    modifier: Modifier = Modifier,
    surahNumber: Int,
    onBackClick: () -> Unit,
    event: (QuranEvent) -> Unit,
    quranState: QuranState,

    ) {


    val quranTransliteration = quranState.quranTransliterationData?.get(surahNumber - 1) ?: return
    val quranEn = quranState.quranEnData?.get(surahNumber - 1) ?: return
    val quranAr = quranState.arQuranData?.get(surahNumber - 1) ?: return
    val quranBn = quranState.quranBnData?.get(surahNumber - 1) ?: return


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val systemLanguage = getSystemLanguage()
    val listState = rememberLazyListState()




    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .debounce(600)
            .collect { index ->
                event(QuranEvent.SaveLastRead(surahNumber, index))
            }
    }

    LaunchedEffect(Unit) {
        if (quranState.lastReadSurah == surahNumber) {
            listState.scrollToItem(quranState.lastReadAyah)
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AyaTopBar(
                scrollBehavior = scrollBehavior,
                surahName = quranAr.englishName,
                onBackClick = { onBackClick() }
            )
        }
    ) { innerpadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(top = innerpadding.calculateTopPadding())
                .fillMaxSize()
                .padding(horizontal = dimens.size20)
        ) {
            item {
                if (quranTransliteration.id != 1 && quranTransliteration.id != 9) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(width = dimens.size200, height = dimens.size50),
                            painter = painterResource(R.drawable.bismillah),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }
            }
            itemsIndexed(quranAr.ayahs) { index, ayah ->
                val quran = quranAr.ayahs[index]
                AyahRow(
                    onCliCK = {
                        Log.d(
                            "AyahClick",
                            "SurahScreen: SurahId: ${quranTransliteration.id}  AyahId : ${quran.numberInSurah} "
                        )
                    },
                    verse = quran,
                    translationText = when (systemLanguage) {
                        "bn" -> quranBn.verses[quran.numberInSurah - 1].translation
                        else -> quranEn.verses[quran.numberInSurah - 1].translation
                    },
                    arabicText = if (quranAr.number == 0 || quranAr.number == 9) {
                        ayah.text
                    } else if (index == 0) {
                        val processText = ayah.text.drop(39)
                        if (processText.isBlank()) ayah.text else processText
                    } else {
                        ayah.text
                    },
                    transliterationVerse = quranTransliteration.verses[quran.numberInSurah - 1]
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AyahRow(
    modifier: Modifier = Modifier,
    verse: ArAyah,
    translationText: String,
    onCliCK: () -> Unit,
    arabicText: String,
    transliterationVerse: TransliterationVerse
) {

    val number = NumberFormat.getInstance(Locale.forLanguageTag("ar")).format(verse.numberInSurah)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onCliCK.invoke()
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.size20),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = Kitab
                        )
                    ) {
                        append(arabicText)
                    }

                    withStyle(
                        style = SpanStyle(
                            fontFamily = Uthmani
                        )
                    ) {
                        append(" $number")
                    }
                },
                style = MaterialTheme.typography.displayLarge.copy(
                    textDirection = TextDirection.Rtl
                )
            )

            Spacer(Modifier.height(dimens.size5))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.size10),
                text = transliterationVerse.transliteration,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal,
                )
            )
            Spacer(Modifier.height(dimens.size5))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.size10),
                text = translationText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal,
                )
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.4f)
            )
            Spacer(Modifier.height(dimens.size5))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyaTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    surahName: String,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        modifier = modifier.padding(top = dimens.size30),
        title = {
            Text(
                surahName,
                modifier = Modifier
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(dimens.size10),
                onClick = { onBackClick() },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backicon),
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets(top = 0.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,

            )
    )
}
