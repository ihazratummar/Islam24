package com.hazrat.alQuran.ui.ayah

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.hazrat.alQuran.ui.component.AyahActionBottomSheet
import com.hazrat.alQuran.ui.component.FloatingAudioController
import com.hazrat.alQuran.ui.surah.SurahScreenData
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration.Companion.milliseconds

/**
 * Pure Presentational Ayah Reading Screen.
 * Displays Ayah text, Tajweed color coding, and synchronized media controls.
 * Navigating away via Back button or minimizing app preserves background recitation in QuranAudioService.
 *
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    val context = LocalContext.current
    val defaultTextColor = MaterialTheme.colorScheme.onBackground
    val isDarkMode = isSystemInDarkTheme()
    val scaffoldBg = customColors.ayahScreenBackground

    val headerCount = 1
    val listState = rememberLazyListState()
    var hasScrolled by rememberSaveable { mutableStateOf(false) }

    var isControllerVisible by remember { mutableStateOf(true) }

    var currentViewedAyah by remember(surahScreenData.targetAyahNumber) {
        mutableStateOf(surahScreenData.targetAyahNumber)
    }

    // Always reset to visible when a new audio recitation starts
    LaunchedEffect(ayahState.playingAyahNumber) {
        if (ayahState.playingAyahNumber != null) {
            isControllerVisible = true
        }
    }

    // Scroll to targetAyahNumber on launch
    LaunchedEffect(ayahState.ayahs) {
        if (ayahState.ayahs.isNotEmpty()) {
            val targetIndex = surahScreenData.targetAyahNumber - 1
            val scrollToIndex = (targetIndex + headerCount).coerceIn(0, ayahState.ayahs.size - 1 + headerCount)
            if (scrollToIndex > 0) {
                listState.animateScrollToItem(scrollToIndex)
            }
        }
    }

    // Smooth Auto-Scroll when current playing Ayah changes (background service auto-advance)
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

    // Scroll direction detection: Hide when scrolling down list (reading downwards), Show when scrolling back up
    LaunchedEffect(listState) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousOffset = listState.firstVisibleItemScrollOffset

        snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collect { (currentIndex, currentOffset) ->
            if (currentIndex > previousIndex) {
                isControllerVisible = false
            } else if (currentIndex < previousIndex) {
                isControllerVisible = true
            } else {
                val delta = currentOffset - previousOffset
                if (delta > 20) {
                    isControllerVisible = false
                } else if (delta < -20) {
                    isControllerVisible = true
                }
            }
            previousIndex = currentIndex
            previousOffset = currentOffset
        }
    }

    // Smart scroll tracking
    LaunchedEffect(ayahState.ayahs, listState) {
        if (ayahState.ayahs.isNotEmpty()) {
            snapshotFlow { listState.layoutInfo }
                .distinctUntilChanged()
                .collect { layoutInfo ->
                    val visibleItems = layoutInfo.visibleItemsInfo
                    if (visibleItems.isEmpty()) return@collect
                    val viewportEnd = layoutInfo.viewportEndOffset

                    if (listState.firstVisibleItemScrollOffset > 0 || listState.firstVisibleItemIndex > 0) {
                        hasScrolled = true
                    }

                    val mostVisible = visibleItems.maxByOrNull { item ->
                        val itemTop = item.offset.coerceAtLeast(0)
                        val itemBottom = (item.offset + item.size).coerceAtMost(viewportEnd)
                        (itemBottom - itemTop).coerceAtLeast(0)
                    }

                    val rawIndex = mostVisible?.index ?: return@collect
                    val ayahIdx = (rawIndex - headerCount).coerceIn(0, ayahState.ayahs.lastIndex)
                    val currentAyahNum = ayahState.ayahs[ayahIdx].ayahNumber
                    currentViewedAyah = currentAyahNum
                    onAyahScrolled(currentAyahNum)
                }
        }
    }



    val sheetState = rememberModalBottomSheetState()

    // Modal Bottom Sheet (Play & Share ONLY)
    if (ayahState.selectedAyahForMenu != null) {
        AyahActionBottomSheet(
            sheetState = sheetState,
            surahName = surahScreenData.name,
            ayah = ayahState.selectedAyahForMenu,
            onDismissRequest = { onEvent(AyahUiEvent.OnDismissMenu) },
            onPlayClick = {
                onEvent(AyahUiEvent.OnPlayAyah(ayahState.selectedAyahForMenu.ayahNumber))
            },
            onShareClick = {
                val ayah = ayahState.selectedAyahForMenu
                val shareText = """
                    ${ayah.arabicText}

                    ${ayah.transliteration}

                    ${ayah.englishTranslation}

                    - [${surahScreenData.name}, Aya ${ayah.surahNumber}:${ayah.ayahNumber}]
                """.trimIndent()

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Ayah"))
            }
        )
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

        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimens.space20),
                verticalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                // Bismillah header
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
                    val isCurrentPlaying = (ayah.surahNumber == ayahState.playingSurahNumber) && (ayah.ayahNumber == ayahState.playingAyahNumber)

                    val cardBg = when {
                        isCurrentPlaying && isDarkMode -> Color(0xFF143330)
                        isCurrentPlaying && !isDarkMode -> Color(0xFFE5EDE6)
                        else -> Color.Transparent
                    }

                    val playingTextColor = defaultTextColor
                    val playingSubTextColor = customColors.secondaryText

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(dimens.cornerXl))
                            .background(cardBg)
                            .clickable { onEvent(AyahUiEvent.OnAyahClick(ayah)) }
                            .padding(dimens.space12)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            // Aya Pill Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        when {
                                            isCurrentPlaying && isDarkMode -> Color(0xFF1B5E57)
                                            isCurrentPlaying && !isDarkMode -> Color(0xFFCDE2D5)
                                            isDarkMode -> MaterialTheme.colorScheme.outlineVariant
                                            else -> MaterialTheme.colorScheme.primaryContainer
                                        }
                                    )
                                    .padding(horizontal = dimens.space12, vertical = dimens.space4)
                            ) {
                                Text(
                                    text = "Aya ${ayah.surahNumber}:${ayah.ayahNumber}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = when {
                                            isCurrentPlaying && isDarkMode -> Color.White
                                            isCurrentPlaying && !isDarkMode -> Color(0xFF00332C)
                                            isDarkMode -> MaterialTheme.colorScheme.onBackground
                                            else -> MaterialTheme.colorScheme.onPrimaryContainer
                                        }
                                    )
                                )
                            }

                            // Arabic Text — Tajweed colored
                            if (ayah.tajweedText.isNotBlank()) {
                                val annotatedAyah = remember(ayah.tajweedText, isCurrentPlaying, isDarkMode) {
                                    parseTajweedHtml(ayah.tajweedText, playingTextColor)
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
                                        color = playingTextColor,
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
                                    color = playingSubTextColor,
                                    textAlign = TextAlign.End
                                )
                            )

                            // Translation
                            Text(
                                text = ayah.englishTranslation,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = playingTextColor,
                                    textAlign = TextAlign.End
                                )
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(top = dimens.space12),
                                color = playingTextColor.copy(alpha = 0.12f)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(dimens.space64))
                }
            }

            // Floating Media Controller with Scroll Hide/Show animation
            val showController = ayahState.ayahs.isNotEmpty()
            if (showController) {
                AnimatedVisibility(
                    visible = isControllerVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    val isServiceActiveForThisSurah = ayahState.playingSurahNumber == surahScreenData.number && ayahState.playingAyahNumber != null

                    val activeSurahName = if (isServiceActiveForThisSurah) {
                        com.hazrat.ui.common.SurahNameProvider.getSurahName(ayahState.playingSurahNumber)
                    } else {
                        surahScreenData.name
                    }

                    val displayAyahNumber = if (isServiceActiveForThisSurah) {
                        ayahState.playingAyahNumber
                    } else {
                        currentViewedAyah
                    }

                    val isPlaying = if (isServiceActiveForThisSurah) ayahState.isPlaying else false
                    val isDownloading = if (isServiceActiveForThisSurah) ayahState.isDownloading else false
                    val downloadProgress = if (isServiceActiveForThisSurah) ayahState.downloadProgress else 0f

                    FloatingAudioController(
                        surahName = activeSurahName,
                        ayahNumber = displayAyahNumber,
                        isPlaying = isPlaying,
                        isDownloading = isDownloading,
                        downloadProgress = downloadProgress,
                        playbackSpeed = ayahState.playbackSpeed,
                        onPlayPauseClick = {
                            if (isServiceActiveForThisSurah) {
                                if (ayahState.isPlaying) onEvent(AyahUiEvent.OnPauseAudio)
                                else onEvent(AyahUiEvent.OnResumeAudio)
                            } else {
                                onEvent(AyahUiEvent.OnPlayAyah(displayAyahNumber))
                            }
                        },
                        onPreviousClick = {
                            if (isServiceActiveForThisSurah) {
                                onEvent(AyahUiEvent.OnPlayPreviousAyah)
                            } else {
                                if (displayAyahNumber > 1) {
                                    onEvent(AyahUiEvent.OnPlayAyah(displayAyahNumber - 1))
                                }
                            }
                        },
                        onNextClick = {
                            if (isServiceActiveForThisSurah) {
                                onEvent(AyahUiEvent.OnPlayNextAyah)
                            } else {
                                if (displayAyahNumber < ayahState.ayahs.size) {
                                    onEvent(AyahUiEvent.OnPlayAyah(displayAyahNumber + 1))
                                }
                            }
                        },
                        onSpeedChange = { speed -> onEvent(AyahUiEvent.OnSpeedChange(speed)) },
                        onCloseClick = {
                            onEvent(AyahUiEvent.OnStopAudio)
                        }
                    )
                }
            }
        }
    }
}