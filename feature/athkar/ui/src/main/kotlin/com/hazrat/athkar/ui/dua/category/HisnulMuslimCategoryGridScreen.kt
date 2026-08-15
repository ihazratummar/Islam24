package com.hazrat.athkar.ui.dua.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.hazrat.athkar.ui.dua.component.DuaCategoryCard
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.theme.dimens

/**
 * 2-Column Category Grid Screen displaying all 11 Hisnul Muslim Categories.
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HisnulMuslimCategoryGridScreen(
    onCategoryClick: (HisnulMuslimCategory) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.dua_hisnul_muslim),
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = dimens.space16, vertical = dimens.space12),
            horizontalArrangement = Arrangement.spacedBy(dimens.space12),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            items(HisnulMuslimCategory.entries) { category ->
                DuaCategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category) }
                )
            }
        }
    }
}
