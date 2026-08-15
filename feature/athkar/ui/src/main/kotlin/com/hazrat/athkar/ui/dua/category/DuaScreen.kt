package com.hazrat.athkar.ui.dua.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.athkar.ui.dua.component.DuaChapterListItem
import com.hazrat.athkar.ui.dua.component.DuaHeroBannerCard
import com.hazrat.athkar.ui.dua.component.DuaQuickActionBar
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.SearchField
import com.hazrat.ui.theme.dimens

/**
 * Main Dua & Azkar Discovery Hub.
 * @author hazratummar
 * Created on 30/05/26
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaScreen(
    modifier: Modifier = Modifier,
    duaCategoryState: DuaCategoryState,
    onBackClick: () -> Unit,
    onDuaClick: (Int) -> Unit,
    onCategoryClick: (HisnulMuslimCategory) -> Unit,
    onHisnulMuslimClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onRecentsClick: () -> Unit,
    onTasbihClick: () -> Unit,
    event: (DuaCategoryEvent) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.home_duas),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    BackIcon(onBackClick = onBackClick)
                },
                actions = {
                    IconButton(onClick = onBookmarksClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.favourite),
                            contentDescription = stringResource(R.string.dua_bookmarks),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(top = dimens.space20)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(dimens.space20)
        ) {
            // 1. Search Bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.space16)
                ) {
                    SearchField(
                        query = duaCategoryState.searchText,
                        onQueryChange = { event(DuaCategoryEvent.SearchDua(it)) }
                    )
                }
            }

            if (duaCategoryState.searchText.isNotBlank()) {
                // Search Results
                item {
                    Text(
                        text = stringResource(R.string.home_quick_access_label),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = dimens.space16)
                    )
                }

                items(duaCategoryState.searchResults) { chapter ->
                    DuaChapterListItem(
                        chapter = chapter,
                        onClick = { onDuaClick(chapter.id) }
                    )
                }
            } else {
                // 2. Quick Action Bar (Bookmarks, Recents, Hisnul Muslim, Tasbih)
                item {
                    Box(modifier = Modifier.padding(horizontal = dimens.space16)) {
                        DuaQuickActionBar(
                            onBookmarksClick = onBookmarksClick,
                            onRecentsClick = onRecentsClick,
                            onHisnulMuslimClick = onHisnulMuslimClick,
                            onTasbihClick = onTasbihClick
                        )
                    }
                }

                // 3. Daily Remembrances Section
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(dimens.space12)
                    ) {
                        Text(
                            text = stringResource(R.string.dua_daily_remembrances),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = dimens.space16)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = dimens.space16),
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            items(
                                listOf(
                                    HisnulMuslimCategory.MORNING_EVENING,
                                    HisnulMuslimCategory.PRAISING_ALLAH,
                                    HisnulMuslimCategory.PRAYER
                                )
                            ) { category ->
                                DuaHeroBannerCard(
                                    category = category,
                                    onClick = { onCategoryClick(category) }
                                )
                            }
                        }
                    }
                }

                // 4. Hisnul Muslim Section Carousel
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(dimens.space12)
                    ) {
                        Text(
                            text = stringResource(R.string.dua_hisnul_muslim),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = dimens.space16)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = dimens.space16),
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            items(
                                listOf(
                                    HisnulMuslimCategory.FOOD_DRINK,
                                    HisnulMuslimCategory.TRAVEL,
                                    HisnulMuslimCategory.NATURE,
                                    HisnulMuslimCategory.HOME_FAMILY,
                                    HisnulMuslimCategory.HAJJ_UMRAH,
                                    HisnulMuslimCategory.JOY_DISTRESS,
                                    HisnulMuslimCategory.SICKNESS_DEATH,
                                    HisnulMuslimCategory.GOOD_ETIQUETTE
                                )
                            ) { category ->
                                DuaHeroBannerCard(
                                    category = category,
                                    onClick = { onCategoryClick(category) }
                                )
                            }
                        }
                    }
                }

                // 5. Essential Duas / Popular Chapters Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.space16),
                        verticalArrangement = Arrangement.spacedBy(dimens.space12)
                    ) {
                        Text(
                            text = stringResource(R.string.dua_essential_duas),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(dimens.cornerLg),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            val popularChapters = duaCategoryState.allChapters.take(10)
                            popularChapters.forEachIndexed { index, chapter ->
                                DuaChapterListItem(
                                    chapter = chapter,
                                    onClick = { onDuaClick(chapter.id) },
                                    showDivider = index != popularChapters.lastIndex
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(dimens.space24))
                }
            }
        }
    }
}