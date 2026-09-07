package com.hazrat.athkar.ui.dua.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

/**
 * Saved / Bookmarked Duas screen.
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaBookmarksScreen(
    duaCategoryState: DuaCategoryState,
    onBackClick: () -> Unit,
    onDuaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.dua_bookmarks),
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
        if (duaCategoryState.bookmarkedDuas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.dua_no_bookmarks),
                    style = MaterialTheme.typography.bodyLarge,
                    color = customColors.secondaryText
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(dimens.space12)
            ) {
                items(duaCategoryState.bookmarkedDuas) { dua ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimens.space16, vertical = dimens.space8)
                    ) {
                        Text(
                            text = dua.arabicText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = dua.translation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = customColors.secondaryText,
                            modifier = Modifier.padding(top = dimens.space4)
                        )
                    }
                }
            }
        }
    }
}
