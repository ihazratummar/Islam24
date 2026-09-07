package com.hazrat.athkar.ui.dua.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.hazrat.model.RecentReadDua
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Recent Duas history screen.
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaRecentsScreen(
    duaCategoryState: DuaCategoryState,
    onBackClick: () -> Unit,
    onDuaClick: (Int) -> Unit,
    onDeleteRecent: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.dua_recents),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
        if (duaCategoryState.recentDuas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.dua_no_recents),
                    style = MaterialTheme.typography.bodyLarge,
                    color = customColors.secondaryText
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(dimens.space8)
            ) {
                items(duaCategoryState.recentDuas) { recent ->
                    RecentDuaItem(
                        recent = recent,
                        onClick = { onDuaClick(recent.chapterId) },
                        onDeleteClick = { onDeleteRecent(recent.chapterId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentDuaItem(
    recent: RecentReadDua,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.space12, horizontal = dimens.space16),
            horizontalArrangement = Arrangement.spacedBy(dimens.space12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.book),
                contentDescription = null,
                modifier = Modifier.size(dimens.iconMd),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.space2)
            ) {
                Text(
                    text = recent.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${recent.duaCount} Duas • ${recent.formattedDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = customColors.secondaryText
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter = painterResource(id = R.drawable.cross),
                    contentDescription = "Delete",
                    modifier = Modifier.size(dimens.iconSm),
                    tint = customColors.secondaryText
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = dimens.space16),
            thickness = dimens.divider,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    }
}
