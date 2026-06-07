package com.hazrat.auth.ui.appSetting

import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.annotation.ExperimentalCoilApi
import com.hazrat.auth.ui.appSetting.component.SelectLanguageDialog
import com.hazrat.auth.ui.component.AppMetaDataSettings
import com.hazrat.auth.ui.component.SettingItemCard
import com.hazrat.auth.ui.component.ToggleSettingData
import com.hazrat.auth.ui.component.ToggleSettings
import com.hazrat.auth.ui.profileScreen.component.RatingBottomSheet
import com.hazrat.ui.R
import com.hazrat.ui.common.AppSection
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.common.toDisplayName
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.hapticFeedbacks

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun AppSettingScreen(
    appSettingEvent: (AppSettingEvent) -> Unit,
    appSettingState: AppSettingState,
    isHapticFeedback: Boolean = false,
    onPolicyClick: () -> Unit = {},
    onAboutUsClick: (String, String) -> Unit,
) {

    val context = LocalContext.current
    val activity = LocalActivity.current

    val snackBarHostState = remember { SnackbarHostState() }
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
                    modifier = Modifier,
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium,
                    actionOnNewLine = false,
                    dismissActionContentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                },
                windowInsets = WindowInsets(top = dimens.space20)
            )
        }
    ) { paddingValues ->

        val toggleSettingsTab = listOf(
            ToggleSettingData(
                label = "Dark Mode",
                statusText = if (appSettingState.toggleTheme) "Currently On" else "Currently Off",
                icon = R.drawable.isha,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    appSettingEvent(AppSettingEvent.ToggleTheme)
                },
                isEnable = appSettingState.toggleTheme
            ),
            ToggleSettingData(
                label = "Haptic",
                statusText = if (appSettingState.isHapticFeedbackEnabled) "Currently On" else "Currently Off",
                icon = R.drawable.vibrate,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    appSettingEvent(AppSettingEvent.HapticFeedbackClick)
                },
                isEnable = appSettingState.isHapticFeedbackEnabled
            )
        )


        val appMetaSettings = listOf(
            AppMetaDataSettings(
                icon = R.drawable.language,
                settingName = "Language",
                label = appSettingState.currentLanguage?.toDisplayName() ,
                trailingIcon = R.drawable.arrowright,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    appSettingEvent(AppSettingEvent.ClickLanguageDialog)
                }
            ),
            AppMetaDataSettings(
                icon = R.drawable.outlinstar,
                settingName = "About Islam 24",
                trailingIcon = R.drawable.arrowright,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    onAboutUsClick("https://islam24.hazratdev.top/about-us", "About Islam 24")
                }
            ),
            AppMetaDataSettings(
                icon = R.drawable.share,
                settingName = "Share App",
                trailingIcon = R.drawable.share2,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    appSettingEvent(AppSettingEvent.ShareApp)
                }
            ),
            AppMetaDataSettings(
                icon = R.drawable.star,
                settingName = "Rate Us",
                trailingIcon = R.drawable.arrowright,
                label = "Share You Valuable Feedback",
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    activity?.let {
                        appSettingEvent(AppSettingEvent.RateUs(activity))
                    }
                }
            ),
        )


        LazyColumn(
            modifier = Modifier
                .padding(horizontal = dimens.space12)
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimens.space20)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color((0xFF0b5a5d)),
                                    Color((0xFF0b5a5d)),
                                    Color((0xFF23655b)),
                                )
                            ),
                            shape = RoundedCornerShape(dimens.space12)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .padding(dimens.space16)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF387578),
                                    shape = RoundedCornerShape(dimens.space12)
                                )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.splash_logo),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(dimens.space4)
                                    .size(dimens.iconXl)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Guest User",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                            Text(
                                text = "Tap to sign in and sync your data",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xff79c4bb)
                                )
                            )
                        }

                        IconWithBackground(
                            icon = R.drawable.arrow_right2,
                            iconColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = Color(0xFF4b7e76),
                            onClick = {}
                        )
                    }
                }
            }


            item {
                AppSection(
                    sectionTitle = "PREFERENCES"
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    ) {
                        toggleSettingsTab.forEachIndexed { index, toggles ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(dimens.space8)
                            ) {
                                ToggleSettings(
                                    icon = toggles.icon,
                                    label = toggles.label,
                                    statusText = toggles.statusText,
                                    isEnabled = toggles.isEnable,
                                    onClick = toggles.onClick
                                )
                                if (index != toggleSettingsTab.size - 1)
                                    HorizontalDivider()
                            }
                        }
                    }
                }
            }

            item {
                AppSection(
                    sectionTitle = "APP"
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    ) {
                        appMetaSettings.forEachIndexed { index, appSettings ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(dimens.space8)
                            ) {
                                SettingItemCard(
                                    leadingIcon = appSettings.icon,
                                    label = appSettings.label,
                                    settingText = appSettings.settingName,
                                    trailingIcon = appSettings.trailingIcon,
                                    onClick = appSettings.onClick,
                                )
                                if (index != appMetaSettings.size - 1)
                                    HorizontalDivider()
                            }
                        }
                    }
                }
            }

            item {
                AppSection(
                    sectionTitle = "POLICY"
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    ) {
                        SettingItemCard(
                            leadingIcon = R.drawable.privacy_policy,
                            settingText = "Legal Docs",
                            trailingIcon = R.drawable.arrowright,
                            onClick = onPolicyClick,
                        )
                    }
                }
            }

            item {
                val versionName = context.packageName?.let {
                    context.packageManager.getPackageInfo(
                        it,
                        PackageManager.GET_META_DATA
                    ).versionName
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimens.space4)
                ) {
                    Text(
                        text = "Islam 24 $versionName",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = "Made with love for the Ummah",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
            item {
                Spacer(Modifier.height(dimens.space64))
            }


        }
        if (appSettingState.isLanguageDialogOpen) {
            SelectLanguageDialog(appSettingEvent)
        }
        if (appSettingState.isRatingDialogOpen) {
            RatingBottomSheet(
                appSettingEvent,
                hapticFeedback = {
                    hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                }
            )
        }
    }
}