package com.hazrat.athkar.ui.dua.dua_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.hazrat.athkar.ui.dua.component.DuaItemCard
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Dua Details Screen displaying all Duas for a specific chapter with source references and sharing capabilities.
 * @author hazratummar
 * Created on 30/05/26
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaItemScreen(
    state: DuaItemState,
    onBackClick: () -> Unit,
    event: (DuaItemEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimens.space2)
                    ) {
                        Text(
                            text = state.chapterTitle.ifBlank { stringResource(R.string.dua_essential_duas) },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (state.totalDuas > 0) {
                            Text(
                                text = stringResource(R.string.dua_items_count, state.totalDuas),
                                style = MaterialTheme.typography.bodySmall,
                                color = customColors.secondaryText
                            )
                        }
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
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimens.space16),
            verticalArrangement = Arrangement.spacedBy(dimens.space8)
        ) {
            items(
                items = state.duaItemList,
                key = { it.id }
            ) { dua ->
                DuaItemCard(
                    dua = dua,
                    onToggleBookmark = { isBookmarked ->
                        event(DuaItemEvent.ToggleBookmark(dua.id, isBookmarked))
                    },
                    onShareClick = {
                        event(DuaItemEvent.SelectDuaForShare(dua))
                    }
                )
            }
        }

        // Dua Share Dialog
        state.selectedDuaForShare?.let { duaToShare ->
            DuaShareDialog(
                dua = duaToShare,
                chapterTitle = state.chapterTitle,
                onDismissRequest = {
                    event(DuaItemEvent.SelectDuaForShare(null))
                }
            )
        }
    }
}