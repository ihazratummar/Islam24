package com.hazrat.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.ui.theme.customColors


/**
 * @author Hazrat Ummar Shaikh
 */


import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import com.hazrat.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBar(
    topBarTitle: String = "",
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {

    TopAppBar(
        title = {
            TopAppBarTitle(title = topBarTitle)
        },
        navigationIcon = {
            BackIcon(onBackClick = onBackClick)
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        windowInsets = WindowInsets(top = dimens.space20)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsmaulHusnaTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {

    TopAppBar(
        modifier = modifier,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBarTitle(title = stringResource(com.hazrat.ui.R.string.home_asmaul_husna))
                Text(
                    text = stringResource(com.hazrat.ui.R.string.allah_names_subtitle),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = customColors.secondaryText
                    )
                )
            }
        },
        navigationIcon = {
            BackIcon(onBackClick = onBackClick)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun TopAppBarTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
    )
}