package com.hazrat.athkar.ui.dua.category

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.hazrat.athkar.ui.dua.component.DuaCategoryDetailHeader
import com.hazrat.athkar.ui.dua.component.DuaChapterListItem
import com.hazrat.model.HisnulMuslimCategory
import com.hazrat.ui.theme.dimens

/**
 * Category Detail Screen with panoramic Hero Banner and chapter items.
 * @author hazratummar
 */
@Composable
fun DuaCategoryDetailScreen(
    category: HisnulMuslimCategory,
    duaCategoryState: DuaCategoryState,
    onBackClick: () -> Unit,
    onChapterClick: (Int) -> Unit,
    onLoadCategory: (HisnulMuslimCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(category) {
        onLoadCategory(category)
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                DuaCategoryDetailHeader(
                    category = category,
                    onBackClick = onBackClick
                )
            }

            items(duaCategoryState.categoryChapters) { chapter ->
                DuaChapterListItem(
                    chapter = chapter,
                    onClick = { onChapterClick(chapter.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimens.space32))
            }
        }
    }
}
