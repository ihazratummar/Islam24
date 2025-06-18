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
import com.hazrat.ui.R
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingEvent
import com.hazrat.ui.theme.dimens
import com.hazrat.islam24.util.Languages

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SelectLanguageDialog(
    appSettingEvent: (AppSettingEvent) -> Unit
) {
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(
        shape = BottomSheetDefaults.ExpandedShape,
        tonalElevation = BottomSheetDefaults.Elevation,
        sheetState = state,
        onDismissRequest = {
            appSettingEvent(AppSettingEvent.ClickLanguageDialog)
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
                    text = stringResource(R.string.select_language),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = dimens.size30, vertical = dimens.size10)
                )
                LanguageCard(
                    appSettingEvent = appSettingEvent,
                    languageCode = Languages.ENGLISH,
                    languageName = "English"
                )
                LanguageCard(
                    appSettingEvent = appSettingEvent,
                    languageCode = Languages.BENGALI,
                    languageName = "বাংলা"
                )
            }
        }
    }
}

@Composable
private fun LanguageCard(
    appSettingEvent: (AppSettingEvent) -> Unit,
    languageCode: Languages,
    languageName: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size15)
            .clickable {
                appSettingEvent(AppSettingEvent.SelectLanguage(languageCode))
                appSettingEvent(AppSettingEvent.ClickLanguageDialog)
            }
            .padding(dimens.size10),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.size15, vertical = dimens.size15),
            text = languageName
        )
    }
}