package com.hazrat.islam24.core.presentation.al_quran

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.hazrat.ui.R
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_ar.ArAyah
import com.hazrat.islam24.core.domain.model.al_quran_model.local_quran_transliteration.TransliterationVerse
import com.hazrat.ui.theme.IndoPak
import com.hazrat.ui.theme.Kitab
import com.hazrat.ui.theme.Uthmani
import com.hazrat.ui.theme.dimens
import com.hazrat.islam24.util.getSystemLanguage
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
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
    ayatNumber: Int?,
    isTracking: Boolean = true,
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

    val scope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .debounce(600)
            .collect { index ->
                if (isTracking) {
                    event(QuranEvent.SaveLastRead(surahNumber, index))
                }
            }
    }

    LaunchedEffect(Unit) {
        if (quranState.lastReadSurah == surahNumber && quranState.lastReadAyah != null) {
            listState.scrollToItem(quranState.lastReadAyah)
        } else {
            listState.scrollToItem(0)
        }
        if (ayatNumber != null) {
            listState.scrollToItem(ayatNumber + 1)
        }
    }
    val totalAyahs = quranAr.ayahs.size
    val ayahNumber =
        remember { derivedStateOf { (listState.firstVisibleItemIndex + 1).coerceAtMost(totalAyahs) } }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AyaTopBar(
                scrollBehavior = scrollBehavior,
                surahName = when (systemLanguage) {
                    "bn" -> quranBn.transliteration
                    else -> quranAr.englishName
                },
                onBackClick = { onBackClick() },
                ayahNumber = ayahNumber.value,
                onAyahClick = {
                    event(QuranEvent.AyahDropDownClick)
                }
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
            itemsIndexed(quranAr.ayahs) { index, ayah ->
                val quran = quranAr.ayahs[index]
                val isFavorite =
                    quranState.ayahFavoriteStatus[Pair(quranAr.number, ayah.numberInSurah)] == true
                AyahRow(
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
                    transliterationVerse = quranTransliteration.verses[quran.numberInSurah - 1],
                    onFavoriteClick = {
                        event(QuranEvent.SaveFavorite(quranAr, ayah))
                    },
                    isFavorite = isFavorite,
                    bismillahText = if (quranAr.number == 1 || quranAr.number == 9) {
                        null
                    } else if (index == 0) {
                        "﷽"
                    } else {
                        null
                    }
                )
            }
        }

        if (quranState.isAyahDropDownOpen) {
            ModalBottomSheet(
                onDismissRequest = { event(QuranEvent.AyahDropDownClick) }
            ) {
                LazyColumn {
                    itemsIndexed(quranEn.verses) { index, ayah ->
                        Text(
                            text = "${stringResource(R.string.ayah)} ${index + 1} - ${ayah.translation}",
                            maxLines = 1,
                            modifier = Modifier
                                .padding(horizontal = dimens.size10, vertical = dimens.size5)
                                .clickable(
                                    onClick = {
                                        scope.launch {
                                            val lastIndex = listState.layoutInfo.totalItemsCount
                                            listState.animateScrollToItem(
                                                index,
                                                if (index == lastIndex) Int.MAX_VALUE else 0
                                            )
                                        }

                                        event(QuranEvent.AyahDropDownClick)
                                    }
                                ),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
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
    onFavoriteClick: () -> Unit,
    arabicText: String,
    bismillahText: String?,
    transliterationVerse: TransliterationVerse,
    isFavorite: Boolean = false
) {

    val number = NumberFormat.getInstance(Locale.forLanguageTag("ar")).format(verse.numberInSurah)
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            bismillahText?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = bismillahText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = Kitab,
                        textAlign = TextAlign.Center
                    )
                )

            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.size20),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = IndoPak,
                            fontWeight = FontWeight.Thin
                        )
                    ) {
                        append(arabicText)
                    }

                    withStyle(
                        style = SpanStyle(
                            fontFamily = IndoPak
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
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                )
            )
            Spacer(Modifier.height(dimens.size5))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.size10),
                text = translationText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                )
            )
            Spacer(Modifier.height(dimens.size5))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier.width(dimens.size60),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(
                        width = dimens.size1,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(dimens.size5)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimens.size5),
                        text = "${verse.numberInSurah}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                IconButton(
                    modifier = Modifier.padding(dimens.size10),
                    onClick = { onFavoriteClick() },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.favourite),
                        contentDescription = null
                    )
                }


            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.4f)
            )
            Spacer(Modifier.height(dimens.size5))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AyaTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    surahName: String,
    scrollBehavior: TopAppBarScrollBehavior,
    ayahNumber: Int? = null,
    onAyahClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier.padding(top = dimens.size30),
        title = {
            Column(
                modifier = Modifier.combinedClickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onAyahClick() }
                )
            ) {
                Text(
                    surahName,
                    modifier = Modifier
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Ayat :${ayahNumber}",
                        modifier = Modifier,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Icon(
                        painter = painterResource(R.drawable.down_arrow),
                        contentDescription = null
                    )
                }
            }


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
//        actions = {
//            Row (
//                modifier = Modifier,
//                verticalAlignment = Alignment.CenterVertically
//            ){
//                Icon(
//                    painter = painterResource(R.drawable.settings),
//                    contentDescription = null
//                )
//            }
//        },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets(top = 20.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,

            )
    )
}
