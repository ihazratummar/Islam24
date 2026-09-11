package com.hazrat.alQuran.ui.surah

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.hazrat.alQuran.ui.component.JuzSegmentCard
import com.hazrat.alQuran.ui.component.SurahCard
import com.hazrat.model.quran.RecentReadSurah
import com.hazrat.model.quran.SurahModel
import com.hazrat.ui.R
import com.hazrat.ui.common.DateFormatter
import com.hazrat.ui.common.IslamicGridBackground
import com.hazrat.ui.common.SurahNameProvider
import com.hazrat.ui.common.SurahSvgImage
import com.hazrat.ui.theme.ScheherazadeFontFamily
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.toLocalizedDigits
import com.hazrat.alQuran.ui.ayah.cleanUthmanic

data class SurahScreenData(
    val name: String,
    val totalAyah: Int,
    val meaning: String,
    val number: Int,
    val targetAyahNumber: Int = 1,
    val isFromBookmark: Boolean = false,
    val isFromKhatam: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(
    modifier: Modifier = Modifier,
    surahState: SurahState,
    onSurahClick: (SurahScreenData) -> Unit,
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchActiveChanged: (Boolean) -> Unit = {},
    onTabSelected: (QuranTab) -> Unit = {},
    onViewModeChanged: (QuranViewMode) -> Unit = {},
    onStartNewKhatamClick: () -> Unit = {},
    onTargetDateSelected: (Long) -> Unit = {},
    onOpenEditPlanSheet: () -> Unit = {},
    onResetPlanClicked: () -> Unit = {},
    onEndPlanClicked: () -> Unit = {},
    onDismissSheets: () -> Unit = {}
) {
    var isViewModeDropdownExpanded by remember { mutableStateOf(false) }
    val isBengali = LocalLocale.current.platformLocale.language.equals("bn", ignoreCase = true)

    IslamicGridBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
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
                                text = stringResource(R.string.common_al_quran),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onSearchActiveChanged(!surahState.isSearchActive)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = stringResource(R.string.common_search),
                                tint = if (surahState.isSearchActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(dimens.iconMd)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    windowInsets = WindowInsets(top = dimens.space20)
                )
            },
            contentWindowInsets = WindowInsets()
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // Sleek Pill Search Bar
                AnimatedVisibility(
                    visible = surahState.isSearchActive,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.space20, vertical = dimens.space8)
                            .clip(RoundedCornerShape(dimens.cornerFull))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = dimens.space16, vertical = dimens.space12)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(dimens.iconSm)
                            )

                            Spacer(modifier = Modifier.width(dimens.space12))

                            Box(modifier = Modifier.weight(1f)) {
                                if (surahState.searchQuery.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.quran_search_hint),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                }
                                BasicTextField(
                                    value = surahState.searchQuery,
                                    onValueChange = onSearchQueryChanged,
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            if (surahState.searchQuery.isNotEmpty()) {
                                Text(
                                    text = "✕",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable { onSearchQueryChanged("") }
                                        .padding(dimens.space4)
                                )
                            }
                        }
                    }
                }

                // 1. Navigation Tabs Row (Read | Khatam | Bookmark)
                val tabs = listOf(
                    QuranTab.READ to stringResource(R.string.quran_tab_read),
                    QuranTab.KHATAM to stringResource(R.string.quran_tab_khatam),
                    QuranTab.BOOKMARK to stringResource(R.string.quran_tab_bookmark)
                )
                val selectedTabIndex =
                    tabs.indexOfFirst { it.first == surahState.selectedTab }.coerceAtLeast(0)

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = MaterialTheme.colorScheme.primary,
                                height = dimens.space2
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, (tab, label) ->
                        Tab(
                            selected = (selectedTabIndex == index),
                            onClick = { onTabSelected(tab) },
                            text = {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }

                if (surahState.selectedTab == QuranTab.READ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dimens.space20),
                        verticalArrangement = Arrangement.spacedBy(dimens.space20)
                    ) {
                        // 2. Resume Reading Hero Card (when recent reads exist)
                        val latestRecent = surahState.recentReads.firstOrNull()
                        if (latestRecent != null) {
                            val matchedSurah =
                                surahState.quranData.find { it.surahNumber == latestRecent.surahNumber }
                            item {
                                Spacer(modifier = Modifier.height(dimens.space12))
                                ResumeReadingHeroCard(
                                    recent = latestRecent,
                                    matchedSurah = matchedSurah,
                                    onClick = {
                                        onSurahClick(
                                            SurahScreenData(
                                                name = matchedSurah?.nameTransliterated
                                                    ?: recentTitle(latestRecent, matchedSurah, isBengali),
                                                totalAyah = matchedSurah?.totalAyahs ?: 0,
                                                meaning = matchedSurah?.nameEnglish ?: "",
                                                number = latestRecent.surahNumber,
                                                targetAyahNumber = latestRecent.ayahNumber
                                            )
                                        )
                                    }
                                )
                            }
                        }

                        // 3. Recents Horizontal Scroll Row
                        if (surahState.recentReads.isNotEmpty()) {
                            item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(dimens.space12)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = stringResource(R.string.quran_recents),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Spacer(modifier = Modifier.width(dimens.space4))
                                        Text(
                                            text = "ⓘ",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                alpha = 0.6f
                                            )
                                        )
                                    }

                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                                    ) {
                                        items(surahState.recentReads) { recent ->
                                            val matchedSurah =
                                                surahState.quranData.find { it.surahNumber == recent.surahNumber }
                                            RecentSurahCard(
                                                recent = recent,
                                                matchedSurah = matchedSurah,
                                                onClick = {
                                                    onSurahClick(
                                                        SurahScreenData(
                                                            name = matchedSurah?.nameTransliterated
                                                                ?: recentTitle(
                                                                    recent,
                                                                    matchedSurah,
                                                                    isBengali
                                                                ),
                                                            totalAyah = matchedSurah?.totalAyahs
                                                                ?: 0,
                                                            meaning = matchedSurah?.nameEnglish
                                                                ?: "",
                                                            number = recent.surahNumber,
                                                            targetAyahNumber = recent.ayahNumber
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 4. View Switcher Header (Sura list | View: Sura ∨) with Icon
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(top = dimens.space12)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (surahState.selectedViewMode == QuranViewMode.SURA) stringResource(R.string.quran_surah_list) else stringResource(R.string.quran_juz_list),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Box {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(dimens.cornerFull))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .border(
                                                width = dimens.divider,
                                                color = MaterialTheme.colorScheme.outlineVariant,
                                                shape = RoundedCornerShape(dimens.cornerFull)
                                            )
                                            .clickable { isViewModeDropdownExpanded = true }
                                            .padding(
                                                horizontal = dimens.space16,
                                                vertical = dimens.space8
                                            )
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(
                                                    id = if (surahState.selectedViewMode == QuranViewMode.SURA) R.drawable.book else R.drawable.calendar
                                                ),
                                                contentDescription = null,
                                                tint = Color.Unspecified,
                                                modifier = Modifier.size(dimens.iconSm)
                                            )
                                            Spacer(modifier = Modifier.width(dimens.space8))
                                            Text(
                                                text = stringResource(
                                                    R.string.quran_view_mode,
                                                    if (surahState.selectedViewMode == QuranViewMode.SURA) stringResource(R.string.quran_surah) else stringResource(R.string.quran_juz)
                                                ),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.width(dimens.space4))
                                            Icon(
                                                painter = painterResource(R.drawable.down_arrow),
                                                contentDescription = null,
                                                modifier = Modifier.size(dimens.iconSm)
                                            )
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = isViewModeDropdownExpanded,
                                        onDismissRequest = { isViewModeDropdownExpanded = false },
                                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        DropdownMenuItem(
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.book),
                                                    contentDescription = null,
                                                    tint = Color.Unspecified,
                                                    modifier = Modifier.size(dimens.iconSm)
                                                )
                                            },
                                            text = {
                                                Text(
                                                    text = stringResource(R.string.quran_surah),
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = if (surahState.selectedViewMode == QuranViewMode.SURA) FontWeight.Bold else FontWeight.Normal
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            },
                                            onClick = {
                                                onViewModeChanged(QuranViewMode.SURA)
                                                isViewModeDropdownExpanded = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            leadingIcon = {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.calendar),
                                                    contentDescription = null,
                                                    tint = Color.Unspecified,
                                                    modifier = Modifier.size(dimens.iconSm)
                                                )
                                            },
                                            text = {
                                                Text(
                                                    text = stringResource(R.string.quran_juz),
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = if (surahState.selectedViewMode == QuranViewMode.JUZ) FontWeight.Bold else FontWeight.Normal
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            },
                                            onClick = {
                                                onViewModeChanged(QuranViewMode.JUZ)
                                                isViewModeDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // 5. Content View: Sura List vs Juz List
                        if (surahState.selectedViewMode == QuranViewMode.SURA) {
                            items(surahState.filteredQuranData, key = { it.surahNumber }) { surah ->
                                SurahCard(
                                    surah = surah,
                                    onClick = {
                                        onSurahClick(
                                            SurahScreenData(
                                                name = surah.nameTransliterated,
                                                totalAyah = surah.totalAyahs,
                                                meaning = surah.nameEnglish,
                                                number = surah.surahNumber
                                            )
                                        )
                                    }
                                )
                            }
                        } else {
                            // Juz List View Mode
                            items(surahState.juzList) { juz ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = dimens.space4),
                                    verticalArrangement = Arrangement.spacedBy(dimens.space8)
                                ) {
                                    Text(
                                        text = stringResource(R.string.quran_juz_number, juz.juzNumber.toLocalizedDigits()),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(
                                            horizontal = dimens.space4,
                                            vertical = dimens.space4
                                        )
                                    )

                                    juz.segments.forEach { seg ->
                                        JuzSegmentCard(
                                            segment = seg,
                                            onClick = {
                                                val matchedSurah =
                                                    surahState.quranData.find { it.surahNumber == seg.surahNumber }
                                                onSurahClick(
                                                    SurahScreenData(
                                                        name = matchedSurah?.nameTransliterated
                                                            ?: seg.surahNameEnglish,
                                                        totalAyah = matchedSurah?.totalAyahs ?: 0,
                                                        meaning = matchedSurah?.nameEnglish
                                                            ?: seg.surahNameEnglish,
                                                        number = seg.surahNumber,
                                                        targetAyahNumber = seg.startAyah
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(dimens.space32))
                        }
                    }
                } else if (surahState.selectedTab == QuranTab.KHATAM) {
                    com.hazrat.alQuran.ui.khatam.KhatamScreenContent(
                        activePlan = surahState.activeKhatamPlan,
                        khatamHistory = surahState.khatamHistory,
                        onStartNewPlanClick = onStartNewKhatamClick,
                        onContinueReadingClick = { surahNum, ayahNum ->
                            val surahModel =
                                surahState.quranData.find { it.surahNumber == surahNum }
                            onSurahClick(
                                SurahScreenData(
                                    name = surahModel?.nameEnglish ?: "Al-Fatihah",
                                    totalAyah = surahModel?.totalAyahs ?: 7,
                                    meaning = surahModel?.nameTransliterated ?: "The Opening",
                                    number = surahNum,
                                    targetAyahNumber = ayahNum,
                                    isFromKhatam = true
                                )
                            )
                        },
                        onEditPlanClick = onOpenEditPlanSheet
                    )
                } else if (surahState.selectedTab == QuranTab.BOOKMARK) {
                    // Bookmark Tab View
                    if (surahState.bookmarkedAyahs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimens.space32),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(dimens.space16)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(dimens.space64)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.star),
                                        contentDescription = null,
                                        tint = Color(0xFFFFB800),
                                        modifier = Modifier.size(dimens.iconLg)
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.quran_no_bookmarks_title),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = stringResource(R.string.quran_no_bookmarks_desc),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = dimens.space20),
                            verticalArrangement = Arrangement.spacedBy(dimens.space16)
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(dimens.space8))
                                Text(
                                    text = stringResource(R.string.quran_bookmarked_ayahs, surahState.bookmarkedAyahs.size),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            surahState.bookmarkedAyahsGrouped.forEach { (surahNum, ayahs) ->
                                val matchedSurah =
                                    surahState.quranData.find { it.surahNumber == surahNum }
                                val surahName =
                                    matchedSurah?.nameTransliterated ?: "Surah $surahNum"
                                val surahMeaning = matchedSurah?.nameEnglish ?: ""
                                val totalAyahs = matchedSurah?.totalAyahs ?: 0

                                item(key = "surah_header_$surahNum") {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(dimens.cornerXl),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                                alpha = 0.35f
                                            )
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal = dimens.space16,
                                                    vertical = dimens.space12
                                                ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(dimens.space16)
                                            ) {
                                                Text(
                                                    text = surahNum.toString(),
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    modifier = Modifier.width(dimens.space24)
                                                )

                                                SurahSvgImage(
                                                    surahNumber = surahNum,
                                                    tint = MaterialTheme.colorScheme.onBackground,
                                                    modifier = Modifier
                                                        .height(dimens.space32)
                                                        .width(dimens.avatarXl)
                                                )
                                            }

                                            Column(
                                                horizontalAlignment = Alignment.End,
                                                verticalArrangement = Arrangement.spacedBy(dimens.space2)
                                            ) {
                                                Text(
                                                    text = surahName,
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                                Text(
                                                    text = stringResource(R.string.quran_saved_count, surahMeaning, ayahs.size),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }

                                items(ayahs, key = { "bkm_${it.id}" }) { ayah ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(dimens.cornerLg))
                                            .clickable {
                                                onSurahClick(
                                                    SurahScreenData(
                                                        name = surahName,
                                                        totalAyah = totalAyahs,
                                                        meaning = surahMeaning,
                                                        number = surahNum,
                                                        targetAyahNumber = ayah.ayahNumber,
                                                        isFromBookmark = true
                                                    )
                                                )
                                            },
                                        shape = RoundedCornerShape(dimens.cornerLg),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                                alpha = 0.5f
                                            )
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(dimens.space16),
                                            verticalArrangement = Arrangement.spacedBy(dimens.space12)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(50))
                                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                                        .padding(
                                                            horizontal = dimens.space12,
                                                            vertical = dimens.space4
                                                        )
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            dimens.space4
                                                        )
                                                    ) {
                                                        Text(
                                                            text = "${stringResource(R.string.quran_aya_short)} ${ayah.surahNumber}:${ayah.ayahNumber}",
                                                            style = MaterialTheme.typography.labelMedium,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.star),
                                                            contentDescription = null,
                                                            tint = Color(0xFFFFB800),
                                                            modifier = Modifier.size(dimens.iconXs)
                                                        )
                                                    }
                                                }
                                            }

                                            Text(
                                                text = ayah.arabicText.cleanUthmanic(),
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontFamily = ScheherazadeFontFamily,
                                                    fontSize = 24.sp,
                                                    lineHeight = 44.sp,
                                                    textDirection = TextDirection.Rtl
                                                ),
                                                modifier = Modifier.fillMaxWidth(),
                                                color = MaterialTheme.colorScheme.onBackground
                                            )

                                            if (ayah.englishTranslation.isNotBlank()) {
                                                Text(
                                                    text = ayah.englishTranslation,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(dimens.space32))
                            }
                        }
                    }
                }
            }
        }

        if (surahState.showTargetDatePickerSheet) {
            com.hazrat.alQuran.ui.khatam.TargetDatePickerSheet(
                initialTargetTimestamp = surahState.activeKhatamPlan?.targetEndDateTimestamp,
                onDismissRequest = onDismissSheets,
                onDateSelected = onTargetDateSelected
            )
        }

        if (surahState.showSettingCompletedSheet) {
            com.hazrat.alQuran.ui.khatam.SettingCompletedSheet(
                onDismissRequest = onDismissSheets,
                onStartNow = {
                    onDismissSheets()
                    val firstSurah = surahState.quranData.firstOrNull()
                    onSurahClick(
                        SurahScreenData(
                            name = firstSurah?.nameEnglish ?: "Al-Fatihah",
                            totalAyah = firstSurah?.totalAyahs ?: 7,
                            meaning = firstSurah?.nameTransliterated ?: "The Opening",
                            number = 1,
                            targetAyahNumber = 1,
                            isFromKhatam = true
                        )
                    )
                }
            )
        }

        if (surahState.showEditPlanSheet) {
            com.hazrat.alQuran.ui.khatam.EditReadingPlanSheet(
                onDismissRequest = onDismissSheets,
                onEditTargetDateClick = onStartNewKhatamClick,
                onResetPlanClick = onResetPlanClicked,
                onEndPlanClick = onEndPlanClicked
            )
        }
    }
}

private fun recentTitle(recent: RecentReadSurah, matchedSurah: SurahModel?, isBengali: Boolean): String {
    return if (isBengali) {
        SurahNameProvider.getSurahNameBengali(recent.surahNumber)
    } else {
        matchedSurah?.nameTransliterated ?: recent.surahName
    }
}

@Composable
private fun ResumeReadingHeroCard(
    recent: RecentReadSurah,
    matchedSurah: SurahModel?,
    onClick: () -> Unit
) {
    val isBengali = LocalLocale.current.platformLocale.language.equals("bn", ignoreCase = true)
    val surahTitle = if (isBengali) {
        SurahNameProvider.getSurahNameBengali(recent.surahNumber)
    } else {
        matchedSurah?.nameTransliterated ?: recent.surahName
    }
    val heroGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0E7A6E),
            Color(0xFF0C6B60),
            Color(0xFF09524A),
            Color(0xFF053833),
            Color(0xFF032623)
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerXl))
            .clickable { onClick() },
        shape = RoundedCornerShape(dimens.cornerXl),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(heroGradient)
                .padding(dimens.space20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(dimens.space8)
                                .clip(CircleShape)
                                .background(customColors.accentColor)
                        )
                        Spacer(modifier = Modifier.width(dimens.space8))
                        Text(
                            text = stringResource(R.string.quran_last_read),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(dimens.space8))

                    Text(
                        text = surahTitle,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(dimens.space4))

                    val displayDate = remember(recent.formattedDate) {
                        DateFormatter.formatHumanReadableDate(recent.formattedDate)
                    }
                    Text(
                        text = stringResource(R.string.quran_verse_date, recent.ayahNumber.toLocalizedDigits(), displayDate),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimens.cornerLg))
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(
                            width = dimens.divider,
                            color = Color.White.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(dimens.cornerLg)
                        )
                        .padding(horizontal = dimens.space16, vertical = dimens.space12),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.quran_resume),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentSurahCard(
    recent: RecentReadSurah,
    matchedSurah: SurahModel?,
    onClick: () -> Unit
) {
    val isBengali = LocalLocale.current.platformLocale.language.equals("bn", ignoreCase = true)
    val surahTitle = if (isBengali) {
        SurahNameProvider.getSurahNameBengali(recent.surahNumber)
    } else {
        matchedSurah?.nameTransliterated ?: recent.surahName
    }

    Column(
        modifier = Modifier
            .width(dimens.layoutXs)
            .clickable { onClick() },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(dimens.space2)
    ) {
        // Top Container: Semi-transparent tile with LARGER Calligraphy SVG
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.space64)
                .clip(RoundedCornerShape(dimens.cornerXl))
                .background(Color.White.copy(alpha = 0.08f))
                .border(
                    width = dimens.divider,
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(dimens.cornerXl)
                ),
            contentAlignment = Alignment.Center
        ) {
            SurahSvgImage(
                surahNumber = recent.surahNumber,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .height(dimens.space40)
                    .fillMaxWidth()
                    .padding(horizontal = dimens.space8)
            )
        }

        Spacer(modifier = Modifier.height(dimens.space4))

        // 1. Surah Name (e.g. Maryam / মারইয়াম)
        Text(
            text = surahTitle,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )

        // 2. Aya Number (e.g. Aya 27)
        Text(
            text = "${stringResource(R.string.quran_aya_short)} ${recent.ayahNumber.toLocalizedDigits()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 3. Date (e.g. আজ, গতকাল, শুক্রবার, ১৫ জুলাই)
        val displayDate = remember(recent.formattedDate) {
            DateFormatter.formatHumanReadableDate(recent.formattedDate)
        }
        Text(
            text = displayDate,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}