package com.hazrat.athkar.ui.dua.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.DuaCategoryModel
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.SearchField
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens


/**
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
    event: (DuaCategoryEvent) -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(dimens.space4)
                    ) {
                        Text(
                            text = "Duas",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Daily Supplications",
                            style = MaterialTheme.typography.bodySmall,
                            color = customColors.secondaryText
                        )
                    }
                },
                navigationIcon = {
                    BackIcon(
                        onBackClick = onBackClick
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(top = dimens.space20)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.padding(dimens.space12).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                SearchField(
                    query = duaCategoryState.searchText,
                    onQueryChange = {event(DuaCategoryEvent.SearchDuaCategory(it))}
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.space20),
            verticalArrangement = Arrangement.spacedBy(dimens.space12)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimens.cornerMd),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    duaCategoryState.duaCategory.forEachIndexed { index,  category ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = { onDuaClick(category.id) }
                                ),
                            verticalArrangement = Arrangement.spacedBy(dimens.space8)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(dimens.space8)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(dimens.space12),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(dimens.iconLg)
                                        .background(
                                            color = MaterialTheme.colorScheme.primaryContainer.copy(
                                                0.4f
                                            ),
                                            shape = RoundedCornerShape(dimens.cornerMd)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${category.id}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    )
                                }
                                Text(
                                    text = category.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                            }
                            if (index != duaCategoryState.duaCategory.size - 1){
                                HorizontalDivider()
                            }
                        }
                    }
                }

            }
            items(5){
                Spacer(Modifier.height(dimens.space12))
            }
        }
    }
}