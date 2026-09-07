package com.hazrat.alQuran.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.hazrat.alQuran.ui.ayah.AyahState
import com.hazrat.alQuran.ui.ayah.AyahUiEvent
import com.hazrat.ui.R
import com.hazrat.ui.common.BackIcon
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.common.SurahSvgImage
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens

import com.hazrat.utils.toLocalizedDigits

/**
 * Top App Bar with Navigation Back button, Surah Name/Verse count, SVG Emblem,
 * Settings Action button, and Animated Settings Drawer.
 *
 * @author hazratummar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahTopAppBar(
    surahName: String,
    surahNumber: Int,
    totalAyahsCount: Int,
    ayahState: AyahState,
    onBackClick: () -> Unit,
    onEvent: (AyahUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimens.space4)
                ) {
                    Text(
                        text = surahName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.quran_surah_detail, surahNumber.toLocalizedDigits(), totalAyahsCount.toLocalizedDigits()),
                        style = MaterialTheme.typography.bodySmall,
                        color = customColors.secondaryText
                    )
                }
            },
            navigationIcon = {
                BackIcon(onBackClick = onBackClick)
            },
            actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    SurahSvgImage(
                        surahNumber = surahNumber,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.height(dimens.space32)
                    )
                    IconWithBackground(
                        icon = R.drawable.settings,
                        onClick = { onEvent(AyahUiEvent.OnToggleSettingsMenu) }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            windowInsets = WindowInsets(top = dimens.space20)
        )

        // Animated Top Dropdown Settings Menu
        AnimatedVisibility(visible = ayahState.isSettingsMenuOpen) {
            AyahSettingsMenu(
                selectedFont = ayahState.selectedFont,
                fontSize = ayahState.fontSize,
                showTranslation = ayahState.showTranslation,
                selectedTranslationSource = ayahState.selectedTranslationSource,
                onFontSelect = { font -> onEvent(AyahUiEvent.OnFontSelected(font)) },
                onFontSizeChange = { size -> onEvent(AyahUiEvent.OnFontSizeChanged(size)) },
                onToggleTranslation = { show -> onEvent(AyahUiEvent.OnToggleTranslation(show)) },
                onTranslationSourceSelect = { source -> onEvent(AyahUiEvent.OnTranslationSourceSelected(source)) }
            )
        }
    }
}
