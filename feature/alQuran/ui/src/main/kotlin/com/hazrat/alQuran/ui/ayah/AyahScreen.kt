package com.hazrat.alQuran.ui.ayah

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration.Companion.milliseconds

/**
 * @author hazratummar
 * Created on 27/05/26
 */

private fun String.cleanUthmanic(): String {
    return this
        .replace("\u06DF", "")
        .replace('\u0652', '\u06e1')
        .replace("\uFEFF", "")
        .trim()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahScreen(
    modifier: Modifier = Modifier,
    ayahState: AyahState,
    onBackClick: () -> Unit,
    surahScreenData: SurahScreenData,
    onAyahScrolled: (Int) -> Unit = {},
    onSurahCompleted: () -> Unit = {}
) {
    val defaultTextColor = MaterialTheme.colorScheme.onBackground
    val isDarkMode = isSystemInDarkTheme()

    // Background dynamically driven by customColors.ayahScreenBackground:
    // Dark mode:  Color(0xFF272727)
    // Light mode: Neutral98 (warm parchment)
    val scaffoldBg = customColors.ayahScreenBackground

    // LazyColumn layout: [0]=bismillah, [1..N]=ayahs  (headerCount = 1)
    val headerCount = 1
    val listState = rememberLazyListState()

    // Tracks whether the user has actively scrolled (avoids false completion on open)
    var hasScrolled by rememberSaveable { mutableStateOf(false) }

    // Scroll to targetAyahNumber once ayahs are loaded (async from Room)
    LaunchedEffect(ayahState.ayahs) {
        if (ayahState.ayahs.isNotEmpty()) {
            val targetIndex = surahScreenData.targetAyahNumber - 1
            val scrollToIndex = (targetIndex + headerCount).coerceIn(0, ayahState.ayahs.size - 1 + headerCount)
            if (scrollToIndex > 0) {
                listState.animateScrollToItem(scrollToIndex)
            }
        }
    }

    // Smart scroll tracking: ayah that occupies the MOST visible pixels on screen
    LaunchedEffect(ayahState.ayahs, listState) {
        if (ayahState.ayahs.isNotEmpty()) {
            snapshotFlow { listState.layoutInfo }
                .distinctUntilChanged()
                .collect { layoutInfo ->
                    val visibleItems = layoutInfo.visibleItemsInfo
                    if (visibleItems.isEmpty()) return@collect
                    val viewportEnd = layoutInfo.viewportEndOffset

                    // Track scroll activity
                    if (listState.firstVisibleItemScrollOffset > 0 || listState.firstVisibleItemIndex > 0) {
                        hasScrolled = true
                    }

                    // Find item with most visible pixels
                    val mostVisible = visibleItems.maxByOrNull { item ->
                        val itemTop = item.offset.coerceAtLeast(0)
                        val itemBottom = (item.offset + item.size).coerceAtMost(viewportEnd)
                        (itemBottom - itemTop).coerceAtLeast(0)
                    }

                    val rawIndex = mostVisible?.index ?: return@collect
                    val ayahIdx = (rawIndex - headerCount).coerceIn(0, ayahState.ayahs.lastIndex)
                    onAyahScrolled(ayahState.ayahs[ayahIdx].ayahNumber)
                }
        }
    }

    // Surah completion: last ayah item fully visible AND user has actively scrolled,
    // held at the bottom for 2 seconds → remove from recents
    LaunchedEffect(ayahState.ayahs, listState) {
        if (ayahState.ayahs.isNotEmpty()) {
            val lastAyahListIndex = ayahState.ayahs.lastIndex + headerCount
            snapshotFlow {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                val viewportEnd = listState.layoutInfo.viewportEndOffset
                val lastItem = visibleItems.find { it.index == lastAyahListIndex }
                // Last ayah is visible AND its bottom edge is within the viewport
                lastItem != null && (lastItem.offset + lastItem.size) <= viewportEnd + 2
            }
                .distinctUntilChanged()
                .collect { lastAyahFullyVisible ->
                    if (lastAyahFullyVisible && hasScrolled) {
                        // Wait 2 seconds — if user scrolls back up this coroutine restarts and cancels
                        delay(2000L.milliseconds)
                        onSurahCompleted()
                    }
                }
        }
    }

    Scaffold(
        containerColor = scaffoldBg,
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
                    BackIcon(onBackClick = onBackClick)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(top = dimens.space20)
            )
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->

        LazyColumn(
            state = listState,
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.space20),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            // Bismillah header — only header item now (headerCount = 1)
            item(key = "bismillah") {
                val surahNumber = surahScreenData.number
                if (surahNumber != 1 && surahNumber != 9) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val tajweedBismillah = "بِسْمِ [h:1[ٱ]للَّهِ [h:2[ٱ][l[ل]رَّحْمَ[n[ـٰ]نِ [h:3[ٱ][l[ل]رَّح[p[ِي]مِ"
                        val bismillahAnnotated = remember {
                            parseTajweedHtml(tajweedBismillah, defaultTextColor)
                        }
                        Text(
                            text = bismillahAnnotated,
                            modifier = Modifier.padding(vertical = dimens.space32),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = ScheherazadeFontFamily,
                                textDirection = TextDirection.Rtl,
                                fontFeatureSettings = "cv62",
                                fontSize = 32.sp
                            )
                        )
                    }
                }
            }

            // Ayah items
            items(ayahState.ayahs, key = { it.id }) { ayah ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimens.space12),
                    verticalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    // Aya Pill Badge — adapts to light/dark
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isDarkMode) MaterialTheme.colorScheme.outlineVariant
                                else MaterialTheme.colorScheme.primaryContainer
                            )
                            .padding(horizontal = dimens.space12, vertical = dimens.space4)
                    ) {
                        Text(
                            text = "Aya ${ayah.surahNumber}:${ayah.ayahNumber}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isDarkMode) MaterialTheme.colorScheme.onBackground
                                else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }

                    // Arabic Text — Tajweed colored
                    if (ayah.tajweedText.isNotBlank()) {
                        val annotatedAyah = remember(ayah.tajweedText) {
                            parseTajweedHtml(ayah.tajweedText, defaultTextColor)
                        }
                        Text(
                            text = annotatedAyah,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = ScheherazadeFontFamily,
                                textDirection = TextDirection.Rtl,
                                fontFeatureSettings = "cv62",
                                fontSize = 30.sp,
                                lineHeight = 60.sp
                            )
                        )
                    } else {
                        val plainText = ayah.arabicText.cleanUthmanic()
                        Text(
                            text = plainText,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = ScheherazadeFontFamily,
                                color = defaultTextColor,
                                textDirection = TextDirection.Rtl,
                                fontFeatureSettings = "cv62",
                                fontSize = 30.sp,
                                lineHeight = 60.sp
                            )
                        )
                    }

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