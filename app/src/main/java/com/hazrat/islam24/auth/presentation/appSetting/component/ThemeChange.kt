package com.hazrat.islam24.auth.presentation.appSetting.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingEvent
import com.hazrat.islam24.auth.presentation.appSetting.Themes
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectThemeDialog(
    appSettingEvent: (AppSettingEvent) -> Unit,
) {
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(
        shape = BottomSheetDefaults.ExpandedShape,
        tonalElevation = BottomSheetDefaults.Elevation,
        sheetState = state,
        onDismissRequest = {
            appSettingEvent(AppSettingEvent.ClickThemeDialog)
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Card(
            modifier = Modifier.padding(dimens.size10),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.change_theme),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(
                        horizontal = dimens.size30,
                        vertical = dimens.size10
                    )
                )
                ThemeItemCard(appSettingEvent = appSettingEvent, themes = Themes.DARK)
                ThemeItemCard(appSettingEvent = appSettingEvent, themes = Themes.LIGHT)
            }
        }
    }
}

@Composable
private fun ThemeItemCard(
    appSettingEvent: (AppSettingEvent) -> Unit,
    themes: Themes
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size15)
            .clickable {
                appSettingEvent(AppSettingEvent.ChangeTheme(themes))
                appSettingEvent(AppSettingEvent.ClickThemeDialog)
            }
            .padding(dimens.size10),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimens.size15,
                    vertical = dimens.size15
                ),
            text = themes.name
        )
    }
}
