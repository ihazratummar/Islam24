package com.hazrat.alQuran.ui.ayah

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.hazrat.alQuran.ui.component.AyahItemCard
import com.hazrat.alQuran.ui.component.AyahTopAppBar
import com.hazrat.alQuran.ui.component.FloatingAudioController
import com.hazrat.alQuran.ui.component.rememberFontRenderConfig
import com.hazrat.alQuran.ui.surah.SurahScreenData
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration.Companion.milliseconds

/**
 * Clean, Presentational Ayah Reading Screen.
 * Composes isolated sub-components (AyahTopAppBar, AyahItemCard, FloatingAudioController, rememberFontRenderConfig).
 *
 * @author hazratummar
 */
@Composable
fun AyahScreen(
    modifier: Modifier = Modifier,
    ayahState: AyahState,
    onBackClick: () -> Unit,
    surahScreenData: SurahScreenData,
    onAyahScrolled: (Int) -> Unit = {},
    onSurahCompleted: () -> Unit = {},
    onEvent: (AyahUiEvent) -> Unit = {}
) {
    val defaultTextColor = MaterialTheme.colorScheme.onBackground
    val scaffoldBg = customColors.ayahScreenBackground

    val headerCount = 1
    val listState = rememberLazyListState()
    var hasScrolled by rememberSaveable { mutableStateOf(false) }
    var isControllerVisible by remember { mutableStateOf(true) }

    var currentViewedAyah by remember(surahScreenData.targetAyahNumber) {
        mutableStateOf(surahScreenData.targetAyahNumber)
    }

    val pagerState = rememberPagerState(
        initialPage = (surahScreenData.number - 1).coerceIn(0, 113),
        pageCount = { 114 }
    )

    val currentAyahState by rememberUpdatedState(ayahState)
    val currentOnAyahScrolled by rememberUpdatedState(onAyahScrolled)
    val currentOnSurahCompleted by rememberUpdatedState(onSurahCompleted)

    // Auto-scroll pager when playing Surah changes
    LaunchedEffect(ayahState.playingSurahNumber) {
        val playingSurah = ayahState.playingSurahNumber
        if (playingSurah != null && playingSurah in 1..114) {
            val targetPage = playingSurah - 1
            if (pagerState.currentPage != targetPage) {
                pagerState.animateScrollToPage(targetPage)
            }
        }
    }

    // Reset media controller visibility when new audio starts
    LaunchedEffect(ayahState.playingAyahNumber) {
        if (ayahState.playingAyahNumber != null) {
            isControllerVisible = true
        }
    }

    // Scroll to initial targetAyahNumber ONCE
    var hasInitialScrolled by rememberSaveable(surahScreenData.targetAyahNumber) { mutableStateOf(false) }
    LaunchedEffect(ayahState.ayahs) {
        if (!hasInitialScrolled && ayahState.ayahs.isNotEmpty()) {
            hasInitialScrolled = true
            val targetIndex = surahScreenData.targetAyahNumber - 1
            val scrollToIndex = (targetIndex + headerCount).coerceIn(0, ayahState.ayahs.size - 1 + headerCount)
            if (scrollToIndex > 0) {
                listState.scrollToItem(scrollToIndex)
            }
        }
    }

    // Auto-scroll LazyColumn when current playing Ayah changes
    LaunchedEffect(ayahState.playingAyahNumber) {
        val playingAyah = ayahState.playingAyahNumber
        if (playingAyah != null && ayahState.ayahs.isNotEmpty()) {
            val targetIndex = (playingAyah - 1 + headerCount).coerceIn(0, ayahState.ayahs.size - 1 + headerCount)
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val isAlreadyVisible = visibleItems.any { it.index == targetIndex }

            if (!isAlreadyVisible) {
                listState.animateScrollToItem(index = targetIndex, scrollOffset = -120)
            }
        }
    }

    // Scroll progress detection for media controller visibility
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { isScrolling ->
                if (isScrolling) {
                    hasScrolled = true
                    isControllerVisible = false
                }
            }
    }

    // Reappear media controller on scroll pause
    LaunchedEffect(hasScrolled) {
        if (hasScrolled) {
            snapshotFlow { listState.isScrollInProgress }
                .distinctUntilChanged()
                .collect { isScrolling ->
                    if (!isScrolling) {
                        delay(600.milliseconds)
                        isControllerVisible = true
                    }
                }
        }
    }

    // Track active viewed Ayah on scroll
    LaunchedEffect(listState) {
        snapshotFlow { Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) }
            .collect {
                val ayahs = currentAyahState.ayahs
                val layoutInfo = listState.layoutInfo
                val visibleItems = layoutInfo.visibleItemsInfo
                if (visibleItems.isNotEmpty() && ayahs.isNotEmpty()) {
                    val viewportEnd = layoutInfo.viewportEndOffset
                    val mostVisible = visibleItems.maxByOrNull { item ->
                        val itemTop = item.offset.coerceAtLeast(0)
                        val itemBottom = (item.offset + item.size).coerceAtMost(viewportEnd)
                        (itemBottom - itemTop).coerceAtLeast(0)
                    }

                    val rawIndex = mostVisible?.index ?: return@collect
                    val ayahIdx = (rawIndex - headerCount).coerceIn(0, ayahs.lastIndex)
                    currentViewedAyah = ayahs[ayahIdx].ayahNumber
                }
            }
    }

    // Dwell-Time Debounce (600ms) for last read position saving
    LaunchedEffect(currentViewedAyah) {
        delay(600.milliseconds)
        currentOnAyahScrolled(currentViewedAyah)
    }

    // Surah completion detector
    LaunchedEffect(currentViewedAyah, ayahState.ayahs) {
        if (ayahState.ayahs.isNotEmpty() && currentViewedAyah == ayahState.ayahs.last().ayahNumber) {
            delay(2000.milliseconds)
            currentOnSurahCompleted()
        }
    }

    // Notify ViewModel when user swipes page
    val initialPageIndex = remember { (surahScreenData.number - 1).coerceIn(0, 113) }
    LaunchedEffect(pagerState.currentPage) {
        val targetSurahNum = pagerState.currentPage + 1
        onEvent(AyahUiEvent.OnSurahPageChanged(targetSurahNum))
        if (pagerState.currentPage != initialPageIndex || hasInitialScrolled) {
            listState.scrollToItem(0)
        }
    }

    // Khatam mode nested scroll connection
    val khatamNestedScrollConnection = remember(surahScreenData.isFromKhatam, currentViewedAyah, ayahState.ayahs) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (surahScreenData.isFromKhatam && source == NestedScrollSource.UserInput) {
                    val totalAyahs = ayahState.ayahs.size
                    val isAtLastAyah = totalAyahs > 0 && currentViewedAyah >= totalAyahs
                    if (!isAtLastAyah && available.x > 0f) {
                        return available
                    }
                }
                return Offset.Zero
            }
        }
    }

    val currentSurahNum = pagerState.currentPage + 1
    val currentSurahName = remember(currentSurahNum) {
        com.hazrat.ui.common.SurahNameProvider.getSurahName(currentSurahNum)
    }

    // Isolated per-font rendering configuration
    val fontConfig = rememberFontRenderConfig(ayahState.selectedFont)
    val currentFontSize = ayahState.fontSize.sp
    val currentLineHeight = (ayahState.fontSize * fontConfig.lineMultiplier).sp

    Scaffold(
        containerColor = scaffoldBg,
        topBar = {
            AyahTopAppBar(
                surahName = currentSurahName,
                surahNumber = currentSurahNum,
                totalAyahsCount = if (ayahState.ayahs.isNotEmpty()) ayahState.ayahs.size else surahScreenData.totalAyah,
                ayahState = ayahState,
                onBackClick = onBackClick,
                onEvent = onEvent
            )
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            reverseLayout = true,
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .nestedScroll(khatamNestedScrollConnection)
        ) { page ->
            val pageSurahNumber = page + 1
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (page == pagerState.currentPage) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dimens.space20),
                        verticalArrangement = Arrangement.spacedBy(dimens.space12)
                    ) {
                        // Bismillah header
                        item(key = "bismillah_$pageSurahNumber") {
                            if (pageSurahNumber != 1 && pageSurahNumber != 9) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val tajweedBismillah = "بِسْمِ [h:1[ٱ]للَّهِ [h:2[ٱ][l[ل]رَّحْمَ[n[ـٰ]نِ [h:3[ٱ][l[ل]رَّح[p[ِي]مِ"
                                    val bismillahAnnotated = remember(ayahState.selectedFont) {
                                        parseTajweedHtml(
                                            input = tajweedBismillah,
                                            defaultColor = defaultTextColor,
                                            fontType = ayahState.selectedFont,
                                            enableTajweedColor = fontConfig.enableTajweedColors
                                        )
                                    }
                                    Text(
                                        text = bismillahAnnotated,
                                        modifier = Modifier.padding(vertical = dimens.space32),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontFamily = fontConfig.fontFamily,
                                            textDirection = TextDirection.Rtl,
                                            fontFeatureSettings = fontConfig.fontFeatureSettings,
                                            fontSize = currentFontSize,
                                            lineHeight = currentLineHeight,
                                            letterSpacing = fontConfig.letterSpacing
                                        )
                                    )
                                }
                            }
                        }

                        // Ayah items rendered via modular AyahItemCard
                        items(ayahState.ayahs, key = { it.id }) { ayah ->
                            AyahItemCard(
                                ayah = ayah,
                                surahName = currentSurahName,
                                ayahState = ayahState,
                                fontConfig = fontConfig,
                                currentFontSize = currentFontSize,
                                currentLineHeight = currentLineHeight,
                                onEvent = onEvent
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(dimens.space64))
                        }
                    }
                }

                // Floating Media Controller
                val showController = ayahState.ayahs.isNotEmpty()
                if (showController) {
                    AnimatedVisibility(
                        visible = isControllerVisible,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it }),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        val isServiceActive = ayahState.playingAyahNumber != null
                        val activeSurahName = if (ayahState.playingSurahNumber != null) {
                            com.hazrat.ui.common.SurahNameProvider.getSurahName(ayahState.playingSurahNumber)
                        } else {
                            currentSurahName
                        }
                        val displayAyahNumber = ayahState.playingAyahNumber ?: currentViewedAyah
                        val isPlaying = if (isServiceActive) ayahState.isPlaying else false
                        val isDownloading = if (isServiceActive) ayahState.isDownloading else false
                        val downloadProgress = if (isServiceActive) ayahState.downloadProgress else 0f

                        FloatingAudioController(
                            surahName = activeSurahName,
                            ayahNumber = displayAyahNumber,
                            isPlaying = isPlaying,
                            isDownloading = isDownloading,
                            downloadProgress = downloadProgress,
                            playbackSpeed = ayahState.playbackSpeed,
                            onPlayPauseClick = {
                                if (isServiceActive) {
                                    if (ayahState.isPlaying) onEvent(AyahUiEvent.OnPauseAudio)
                                    else onEvent(AyahUiEvent.OnResumeAudio)
                                } else {
                                    onEvent(AyahUiEvent.OnPlayAyah(displayAyahNumber))
                                }
                            },
                            onPreviousClick = {
                                if (isServiceActive) {
                                    onEvent(AyahUiEvent.OnPlayPreviousAyah)
                                } else {
                                    if (displayAyahNumber > 1) {
                                        onEvent(AyahUiEvent.OnPlayAyah(displayAyahNumber - 1))
                                    }
                                }
                            },
                            onNextClick = {
                                if (isServiceActive) {
                                    onEvent(AyahUiEvent.OnPlayNextAyah)
                                } else {
                                    if (displayAyahNumber < ayahState.ayahs.size) {
                                        onEvent(AyahUiEvent.OnPlayAyah(displayAyahNumber + 1))
                                    }
                                }
                            },
                            onSpeedChange = { speed -> onEvent(AyahUiEvent.OnSpeedChange(speed)) },
                            onCloseClick = { onEvent(AyahUiEvent.OnStopAudio) }
                        )
                    }
                }
            }
        }

        // Ayah Visual Share Dialog
        ayahState.selectedAyahForShare?.let { ayahToShare ->
            com.hazrat.alQuran.ui.component.AyahShareDialog(
                ayah = ayahToShare,
                surahName = currentSurahName,
                translationSource = ayahState.selectedTranslationSource,
                onDismissRequest = {
                    onEvent(AyahUiEvent.OnDismissShareDialog)
                }
            )
        }
    }
}