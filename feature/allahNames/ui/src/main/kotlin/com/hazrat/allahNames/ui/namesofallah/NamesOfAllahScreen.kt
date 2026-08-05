package com.hazrat.allahNames.ui.namesofallah

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hazrat.allahNames.model.namesofallah.NameOfAllahData
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.IslamicLoadingScreen
import com.hazrat.ui.common.OfflineCard
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

/**
 * Premium Asmaul Husna (99 Names of Allah) Screen.
 * Rebuilt to match exact user mockup with hero banner, search, All/Favorites tabs, and 2-column grid.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamesOfAllahScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    nameEntity: List<NameOfAllahData> = emptyList(),
    onSupportClick: (() -> Unit)? = null,
    viewModel: NamesViewmodel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val favoriteNames by viewModel.favoriteNames.collectAsStateWithLifecycle()
    val filteredNamesFlow by viewModel.filteredNames.collectAsStateWithLifecycle()

    val displayedNames = if (filteredNamesFlow.isNotEmpty()) {
        filteredNamesFlow
    } else if (searchQuery.isEmpty() && selectedTab == NameTab.ALL) {
        nameEntity
    } else {
        emptyList()
    }

    // Daily Deterministic Random Featured Name (changes once every day)
    val featuredName = remember(displayedNames, nameEntity) {
        val sourceList = if (nameEntity.isNotEmpty()) nameEntity else displayedNames
        if (sourceList.isNotEmpty()) {
            val dayOfYear = LocalDate.now().dayOfYear
            val index = (dayOfYear - 1) % sourceList.size
            sourceList[index]
        } else {
            NameOfAllahData(
                number = 1,
                enDesc = "The Most Compassionate",
                enMeaning = "The Most Compassionate",
                found = "",
                name = "الرَّحْمَنُ",
                transliteration = "Ar-Rahman",
                bnTransliteration = "",
                bnMeaning = "",
                bnDec = ""
            )
        }
    }

    Scaffold(
        topBar = {
            AsmaulHusnaCustomTopBar(
                onBackClick = onBackClick
            )
        },
        contentWindowInsets = WindowInsets(),
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        if (nameEntity.isEmpty()){
            IslamicLoadingScreen(subtitle = "Loading names.....")
        }else{
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = dimens.space16)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimens.space16)
            ) {

                // Search Bar Input
                item {
                    SearchBarInput(
                        query = searchQuery,
                        onQueryChange = viewModel::onSearchQueryChange
                    )
                }

                // Featured Hero Name Banner (Daily Random Name)
                item {
                    HeroNameBannerCard(
                        featuredName = featuredName,
                        isFavorite = favoriteNames.contains(featuredName.number),
                        onFavoriteToggle = { viewModel.toggleFavorite(featuredName.number) }
                    )
                }

                // Tabs Row: All & Favorites (Learned tab completely removed)
                item {
                    TabFilterRow(
                        selectedTab = selectedTab,
                        onTabSelected = viewModel::onTabSelected
                    )
                }

                // 2-Column Name Cards Grid
                if (displayedNames.isNotEmpty()) {
                    val chunkedNames = displayedNames.chunked(2)
                    items(chunkedNames) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            rowItems.forEach { data ->
                                NameCardItem(
                                    name = data,
                                    isFavorite = favoriteNames.contains(data.number),
                                    onFavoriteToggle = { viewModel.toggleFavorite(data.number) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    item {
                        if (nameEntity.isEmpty()) {
                            OfflineCard()
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimens.space32),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No names found",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = customColors.secondaryText
                                )
                            }
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimens.space16)
                            .clickable { onSupportClick?.invoke() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space4)
                        ) {
                            Text(
                                text = "Free forever — support the mission",
                                style = MaterialTheme.typography.bodySmall,
                                color = customColors.secondaryText.copy(alpha = 0.8f)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.chevron_right),
                                contentDescription = null,
                                tint = customColors.secondaryText.copy(alpha = 0.8f),
                                modifier = Modifier.size(dimens.iconXs)
                            )
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

/**
 * Top app bar with back button, star emblem, Asmaul Husna title, and subtitle (no trailing actions).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AsmaulHusnaCustomTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            BackIcon(onBackClick = onBackClick)
        },
        title = {
            Column {
                Text(
                    text = "Asmaul Husna",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Reflect on the 99 Beautiful Names",
                    style = MaterialTheme.typography.labelMedium,
                    color = customColors.secondaryText
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        windowInsets = WindowInsets(top = dimens.space20),
        modifier = modifier
    )
}

/**
 * Soft rounded Search Input Bar.
 */
@Composable
private fun SearchBarInput(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search a name",
                style = MaterialTheme.typography.bodyMedium,
                color = customColors.secondaryText
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Search",
                tint = customColors.secondaryText,
                modifier = Modifier.size(dimens.iconSm)
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.cross),
                    contentDescription = "Clear",
                    tint = customColors.secondaryText,
                    modifier = Modifier
                        .size(dimens.iconSm)
                        .clickable { onQueryChange("") }
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        ),
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Featured Hero Banner Card displaying daily random name with Reflect button.
 */
@Composable
private fun HeroNameBannerCard(
    featuredName: NameOfAllahData,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .border(
                width = dimens.divider,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(dimens.cornerLg)
            ),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
            // Premium Calligraphy Emblem Container (Large)
            CalligraphyEmblemRing(
                nameNumber = featuredName.number,
                isLarge = true
            )

            // Right Info & Reflect Button Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space4)
            ) {
                Text(
                    text = featuredName.transliteration,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space4)
                ) {
                    Text(
                        text = "◆",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = featuredName.enMeaning,
                        style = MaterialTheme.typography.bodyMedium,
                        color = customColors.secondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(dimens.space4))

                // Sage Green Reflect Button
                Button(
                    onClick = onFavoriteToggle,
                    shape = RoundedCornerShape(dimens.cornerFull),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.height(dimens.space32)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space4)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.heart),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(dimens.iconXs)
                        )
                        Text(
                            text = if (isFavorite) "Reflected" else "Reflect",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Filter Tab Row for All and Favorites (Learned tab removed).
 */
@Composable
private fun TabFilterRow(
    selectedTab: NameTab,
    onTabSelected: (NameTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.space12)
    ) {
        TabPillButton(
            text = "All",
            isSelected = selectedTab == NameTab.ALL,
            onClick = { onTabSelected(NameTab.ALL) }
        )
        TabPillButton(
            text = "Favorites",
            isSelected = selectedTab == NameTab.FAVORITES,
            onClick = { onTabSelected(NameTab.FAVORITES) }
        )
    }
}

@Composable
private fun TabPillButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.cornerFull))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .border(
                width = dimens.divider,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                shape = RoundedCornerShape(dimens.cornerFull)
            )
            .clickable { onClick() }
            .padding(horizontal = dimens.space20, vertical = dimens.space8),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
        )
    }
}

/**
 * 2-Column Name Card Item.
 */
@Composable
fun NameCardItem(
    name: NameOfAllahData,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerLg))
            .border(
                width = dimens.divider,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(dimens.cornerLg)
            ),
        shape = RoundedCornerShape(dimens.cornerLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.space12),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimens.space8)
        ) {
            // Header Row: Number on left, Heart Favorite toggle on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name.number.toString().padStart(2, '0'),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = customColors.secondaryText
                )

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(dimens.space24)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.heart),
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFE53935) else customColors.secondaryText.copy(alpha = 0.5f),
                        modifier = Modifier.size(dimens.iconSm)
                    )
                }
            }

            // Center Calligraphy Emblem Inside Premium Ring Container
            CalligraphyEmblemRing(
                nameNumber = name.number,
                isLarge = false
            )

            // Transliteration & Meaning
            Text(
                text = name.transliteration,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = name.enMeaning,
                style = MaterialTheme.typography.bodySmall,
                color = customColors.secondaryText,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Premium Calligraphy Emblem Ring — uses the ornate premium_circle vector as the frame.
 * No custom border or background overlay — lets the detailed golden vector artwork shine.
 */
@Composable
private fun CalligraphyEmblemRing(
    nameNumber: Int,
    isLarge: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Large = hero banner ring, small = grid card ring
    val ringSize = if (isLarge) dimens.layoutSm else dimens.layoutXs
    val calligraphySize = if (isLarge) dimens.space56 else dimens.avatarMd

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(ringSize)
    ) {
        // Ornate golden ring vector — this IS the premium frame
        Icon(
            painter = painterResource(R.drawable.premium_circle),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.fillMaxSize()
        )
        // Calligraphy vector centered inside with breathing room
        Icon(
            painter = painterResource(getNameOfAllahIcon(nameNumber)),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(calligraphySize)
        )
    }
}