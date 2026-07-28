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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.hazrat.alQuran.ui.component.AyahContextMenu
import com.hazrat.alQuran.ui.component.FloatingAudioController
import com.hazrat.alQuran.ui.surah.SurahScreenData
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.SurahSvgImage
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

    val pagerState = rememberPagerState(
        initialPage = (surahScreenData.number - 1).coerceIn(0, 113),
        pageCount = { 114 }
    )

    // rememberUpdatedState so long-running coroutines always see the latest values
    val currentAyahState by rememberUpdatedState(ayahState)
    val currentOnAyahScrolled by rememberUpdatedState(onAyahScrolled)
    val currentOnSurahCompleted by rememberUpdatedState(onSurahCompleted)

    // Smooth auto-page scroll when playing Surah changes (Juz auto-advance across Surahs)
    LaunchedEffect(ayahState.playingSurahNumber) {
        val playingSurah = ayahState.playingSurahNumber
        if (playingSurah != null && playingSurah in 1..114) {
            val targetPage = playingSurah - 1
            if (pagerState.currentPage != targetPage) {
                pagerState.animateScrollToPage(targetPage)
            }
        }
    }

    // Always reset to visible when a new audio recitation starts
    LaunchedEffect(ayahState.playingAyahNumber) {
        if (ayahState.playingAyahNumber != null) {
            isControllerVisible = true
        }
    }

    // Scroll to targetAyahNumber ONCE on initial launch when ayahs are first loaded
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

    // Detect user scrolling to hide controller
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

    // Auto-reappear controller when user stops scrolling
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

    // Track active viewed Ayah on scroll — updates UI instantly
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
                    val currentAyahNum = ayahs[ayahIdx].ayahNumber
                    currentViewedAyah = currentAyahNum
                }
            }
    }

    // Dwell-Time Debounce (600ms): Only save position when user actually pauses/dwells on an Ayah.
    // Prevents accidental fast-scrolls or peeking ahead from overwriting last read position.
    LaunchedEffect(currentViewedAyah) {
        delay(600.milliseconds)
        currentOnAyahScrolled(currentViewedAyah)
    }

    // Detect surah completion: when user stays on the last ayah for 2 seconds, remove from recent
    LaunchedEffect(currentViewedAyah, ayahState.ayahs) {
        if (ayahState.ayahs.isNotEmpty() && currentViewedAyah == ayahState.ayahs.last().ayahNumber) {
            delay(2000.milliseconds)
            currentOnSurahCompleted()
        }
    }

    // Initial page index
    val initialPageIndex = remember { (surahScreenData.number - 1).coerceIn(0, 113) }

    // Notify ViewModel when user swipes to a new Surah page and reset scroll position to 0
    LaunchedEffect(pagerState.currentPage) {
        val targetSurahNum = pagerState.currentPage + 1
        onEvent(AyahUiEvent.OnSurahPageChanged(targetSurahNum))
        
        if (pagerState.currentPage != initialPageIndex || hasInitialScrolled) {
            listState.scrollToItem(0)
        }
    }

    // Nested scroll connection to block forward swipe in Khatam mode unless user reaches the last Ayah
    val khatamNestedScrollConnection = remember(surahScreenData.isFromKhatam, currentViewedAyah, ayahState.ayahs) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (surahScreenData.isFromKhatam && source == NestedScrollSource.UserInput) {
                    val totalAyahs = ayahState.ayahs.size
                    val isAtLastAyah = totalAyahs > 0 && currentViewedAyah >= totalAyahs
                    // Block RIGHT swipe (swiping right towards next Surah) until user reaches last Ayah
                    if (!isAtLastAyah && available.x > 0f) {
                        return available // Consume scroll delta to block right swipe to next Surah
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
                            text = currentSurahName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Surah $currentSurahNum • ${if (ayahState.ayahs.isNotEmpty()) ayahState.ayahs.size else surahScreenData.totalAyah} verses",
                            style = MaterialTheme.typography.bodySmall,
                            color = customColors.secondaryText
                        )
                    }
                },
                navigationIcon = {
                    BackIcon(onBackClick = onBackClick)
                },
                actions = {
                    SurahSvgImage(
                        surahNumber = currentSurahNum,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .height(dimens.space32)
                            .padding(end = dimens.space16)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(top = dimens.space20)
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

                    val cardBg = if (isCurrentPlaying) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    } else {
                        Color.Transparent
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
                        if (ayahState.selectedAyahForMenu?.id == ayah.id) {
                            AyahContextMenu(
                                expanded = true,
                                ayah = ayah,
                                onDismissRequest = { onEvent(AyahUiEvent.OnDismissMenu) },
                                onPlaySurahClick = { onEvent(AyahUiEvent.OnPlaySurahFrom(ayah.ayahNumber)) },
                                onPlayJuzClick = { onEvent(AyahUiEvent.OnPlayJuzFrom(ayah.ayahNumber)) },
                                onRepeatAyahClick = { onEvent(AyahUiEvent.OnRepeatAyah(ayah.ayahNumber)) },
                                onBookmarkClick = { onEvent(AyahUiEvent.OnToggleBookmark(ayah)) },
                                onCopyClick = {
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Ayah Text", "${ayah.arabicText}\n\n${ayah.transliteration}\n\n${ayah.englishTranslation}")
                                    clipboard.setPrimaryClip(clip)
                                    android.widget.Toast.makeText(context, "Copied Ayah", android.widget.Toast.LENGTH_SHORT).show()
                                },
                                onShareClick = {
                                    val shareText = "${ayah.arabicText}\n\n${ayah.transliteration}\n\n${ayah.englishTranslation}\n\n- [${surahScreenData.name}, Aya ${ayah.surahNumber}:${ayah.ayahNumber}]"
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Ayah"))
                                }
                            )
                        }

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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(dimens.space4)
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
                                    if (ayah.isBookmarked) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.star),
                                            contentDescription = "Bookmarked",
                                            tint = Color(0xFFFFB800),
                                            modifier = Modifier.size(dimens.iconXs)
                                        )
                                    }
                                }
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

                            // Translation (Bengali / English) - Crisp typography
                            Text(
                                text = ayah.englishTranslation,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = playingTextColor,
                                    textAlign = TextAlign.End,
                                    fontSize = 16.sp,
                                    lineHeight = 26.sp
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
                    val isServiceActive = ayahState.playingAyahNumber != null

                    val activeSurahName = if (ayahState.playingSurahNumber != null) {
                        com.hazrat.ui.common.SurahNameProvider.getSurahName(ayahState.playingSurahNumber)
                    } else {
                        currentSurahName
                    }

                    val displayAyahNumber = if (ayahState.playingAyahNumber != null) {
                        ayahState.playingAyahNumber
                    } else {
                        currentViewedAyah
                    }

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
                        onCloseClick = {
                            onEvent(AyahUiEvent.OnStopAudio)
                        }
                    )
                }
            }
        }
    }
}
}