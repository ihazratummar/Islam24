package com.hazrat.auth.ui.appSetting

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hazrat.auth.ui.profileScreen.component.LanguageSelectionBottomSheet
import com.hazrat.model.AppLanguage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.hazrat.auth.ui.component.AppMetaDataSettings
import com.hazrat.auth.ui.component.SettingItemCard
import com.hazrat.auth.ui.component.ToggleSettingData
import com.hazrat.auth.ui.component.ToggleSettings
import com.hazrat.auth.ui.component.ZoomedProfileImage
import com.hazrat.auth.ui.profileScreen.component.RatingBottomSheet
import com.hazrat.ui.R
import com.hazrat.ui.common.AppSection
import com.hazrat.ui.common.IslamicLoadingScreen
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.ui.theme.isUserLoggedIn
import com.hazrat.utils.DateUtil.toReadableLocale
import com.hazrat.utils.hapticFeedbacks
import com.hazrat.utils.toLocalizedDigits
import kotlinx.coroutines.flow.SharedFlow

/**
 * Premium Profile Screen matching exact user mockup & real database metrics.
 * @author Hazrat Ummar Shaikh
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun AppSettingScreen(
    appSettingEvent: (AppSettingEvent) -> Unit,
    state: ProfileState,
    isHapticFeedback: Boolean = false,
    onPolicyClick: () -> Unit = {},
    onAboutUsClick: (String, String) -> Unit = { _, _ -> },
    onAuthClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    effect: SharedFlow<ProfileEffect>?
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    val snackBarHostState = remember { SnackbarHostState() }
    val hapticFeedback = LocalHapticFeedback.current
    var showLanguageBottomSheet by remember { mutableStateOf(false) }

    val user = state.userModel


    LaunchedEffect(Unit) {
        effect?.collect { effect ->
            when (effect) {
                is ProfileEffect.Error -> {
                    snackBarHostState.showSnackbar(
                        message = effect.error
                    )
                }
            }
        }
    }

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
                        text = stringResource(R.string.common_profile),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                },
                windowInsets = WindowInsets(top = dimens.space20)
            )
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->

        val toggleSettingsTab = listOf(
            ToggleSettingData(
                label = stringResource(R.string.profile_dark_mode),
                statusText = if (state.toggleTheme) stringResource(R.string.profile_status_on) else stringResource(R.string.profile_status_off),
                icon = R.drawable.isha,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    appSettingEvent(AppSettingEvent.ToggleTheme)
                },
                isEnable = state.toggleTheme
            ),
            ToggleSettingData(
                label = stringResource(R.string.profile_notifications),
                statusText = if (state.isMasterNotificationEnabled) stringResource(R.string.profile_status_enabled) else stringResource(R.string.profile_status_disabled),
                icon = R.drawable.notification,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    appSettingEvent(AppSettingEvent.ToggleMasterNotification)
                },
                isEnable = state.isMasterNotificationEnabled
            ),
            ToggleSettingData(
                label = stringResource(R.string.profile_haptic_feedback),
                statusText = if (state.isHapticFeedbackEnabled) stringResource(R.string.profile_status_enabled) else stringResource(R.string.profile_status_disabled),
                icon = R.drawable.vibrate,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    appSettingEvent(AppSettingEvent.HapticFeedbackClick)
                },
                isEnable = state.isHapticFeedbackEnabled
            )
        )

        val aboutAppTitle = stringResource(R.string.profile_about_app)
        val appMetaSettings = listOf(
            AppMetaDataSettings(
                icon = R.drawable.outlinstar,
                settingName = aboutAppTitle,
                trailingIcon = R.drawable.arrowright,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    onAboutUsClick("https://islam24.app/about-us", aboutAppTitle)
                }
            ),
            AppMetaDataSettings(
                icon = R.drawable.share,
                settingName = stringResource(R.string.profile_share_app),
                trailingIcon = R.drawable.arrowright,
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
                settingName = stringResource(R.string.common_rate_us),
                trailingIcon = R.drawable.arrowright,
                label = stringResource(R.string.profile_rate_desc),
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
            AppMetaDataSettings(
                icon = R.drawable.heart,
                settingName = stringResource(R.string.profile_support_title),
                label = stringResource(R.string.profile_support_desc),
                trailingIcon = R.drawable.arrowright,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    onSupportClick()
                }
            )
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = dimens.space12)
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimens.space20)
        ) {
            // Top Guest Profile Hero Card & Metrics (Exact Mockup Match)
            item {
                Card(
                    onClick = {
                        if (!state.isLoggedIn) {
                            onAuthClick()
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(dimens.cornerXl),
                    colors = CardDefaults.cardColors(
                        containerColor = customColors.secondCardColor
                    ),
                    border = BorderStroke(
                        width = dimens.divider, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimens.space20)
                    ) {
                        // User Header Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space16)
                        ) {
                            // Circle Avatar with Plus Badge Overlay
                            Box(
                                modifier = Modifier.size(dimens.space48 + dimens.space16)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .border(
                                            width = dimens.space2,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isUserLoggedIn) {
                                        ZoomedProfileImage(
                                            imageUri = state.userModel?.picture,
                                            context = context,
                                            isVisible = true
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(R.drawable.splash_logo),
                                            contentDescription = null,
                                            modifier = Modifier.size(dimens.iconXl),
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(dimens.space20)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .border(
                                            width = dimens.divider,
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = CircleShape
                                        )
                                        .align(Alignment.BottomEnd),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                val text = if (isUserLoggedIn) user?.name ?: "" else stringResource(
                                    R.string.common_guest_user
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Text(
                                    text = if (isUserLoggedIn) user?.createdAt?.toReadableLocale()
                                        ?: "" else stringResource(R.string.common_tap_to_sign_in),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = customColors.secondaryText
                                )
                            }

                            Icon(
                                painter = painterResource(R.drawable.arrowright),
                                contentDescription = null,
                                modifier = Modifier.size(dimens.iconSm),
                                tint = customColors.secondaryText
                            )
                        }

                        Spacer(modifier = Modifier.height(dimens.space16))
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        Spacer(modifier = Modifier.height(dimens.space16))

                        // Real Database Metrics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MetricItem(
                                count = state.totalPrayersLogged.toLocalizedDigits(),
                                label = stringResource(R.string.profile_prayers_metric)
                            )
                            MetricItem(
                                count = state.prayerStreak.toLocalizedDigits(),
                                label = stringResource(R.string.profile_streak_metric),
                                labelColor = customColors.accentColor
                            )
                            MetricItem(
                                count = state.totalBookmarkedAyahs.toLocalizedDigits(),
                                label = stringResource(R.string.profile_bookmarks_metric)
                            )
                        }
                    }
                }
            }

            // PREFERENCES Section
            item {
                AppSection(
                    sectionTitle = stringResource(R.string.profile_section_preferences)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = customColors.secondCardColor
                        ),
                        border = BorderStroke(
                            width = dimens.divider, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimens.space8)
                        ) {
                            toggleSettingsTab.forEach { toggles ->
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
                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                                }
                            }
                            SettingItemCard(
                                leadingIcon = R.drawable.ic_language,
                                label = AppLanguage.fromCode(state.selectedLanguageCode).nativeName,
                                settingText = stringResource(R.string.common_language),
                                trailingIcon = R.drawable.arrowright,
                                onClick = {
                                    hapticFeedbacks(
                                        isEnable = isHapticFeedback,
                                        hapticFeedback = hapticFeedback
                                    )
                                    showLanguageBottomSheet = true
                                }
                            )
                        }
                    }
                }
            }

            // APP Section
            item {
                AppSection(
                    sectionTitle = stringResource(R.string.profile_section_app)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = customColors.secondCardColor
                        ),
                        border = BorderStroke(
                            width = dimens.divider, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
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
                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            }
                        }
                    }
                }
            }

            // LEGAL Section
            item {
                AppSection(
                    sectionTitle = stringResource(R.string.profile_section_legal)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = customColors.secondCardColor
                        ),
                        border = BorderStroke(
                            width = dimens.divider, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        )
                    ) {
                        SettingItemCard(
                            leadingIcon = R.drawable.privacy_policy,
                            settingText = stringResource(R.string.profile_legal_docs),
                            trailingIcon = R.drawable.arrowright,
                            onClick = onPolicyClick,
                        )
                    }
                }
            }

            if (state.isLoggedIn){
                item {
                    AppSection(
                        sectionTitle = stringResource(R.string.profile_section_account)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = customColors.secondCardColor
                            ),
                            border = BorderStroke(
                                width = dimens.divider, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )
                        ) {
                            SettingItemCard(
                                leadingIcon = R.drawable.logout,
                                settingText = stringResource(R.string.profile_sign_out),
                                onClick = { appSettingEvent(AppSettingEvent.LogOut) },
                                textColor = MaterialTheme.colorScheme.error,
                                iconColor = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            // Footer Section
            item {
                val versionName = try {
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
                } catch (_: Exception) {
                    "1.1.0"
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimens.space4)
                ) {
                    Text(
                        text = "Islam 24 v${versionName ?: "1.1.0"}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = customColors.secondaryText
                        )
                    )
                    Text(
                        text = stringResource(R.string.profile_made_with_love),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = customColors.secondaryText.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            item {
                Spacer(Modifier.height(dimens.space64))
            }
        }

        if (state.isLoading) {
            IslamicLoadingScreen(subtitle = stringResource(R.string.profile_signing_out))
        }

        if (state.isRatingDialogOpen) {
            RatingBottomSheet(
                appSettingEvent,
                hapticFeedback = {
                    hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                }
            )
        }

        if (showLanguageBottomSheet) {
            LanguageSelectionBottomSheet(
                selectedLanguageCode = state.selectedLanguageCode,
                onLanguageSelected = { code ->
                    appSettingEvent(AppSettingEvent.UpdateLanguage(code))
                },
                onDismissRequest = { showLanguageBottomSheet = false }
            )
        }
    }
}

@Composable
private fun MetricItem(
    count: String,
    label: String,
    labelColor: Color = customColors.secondaryText
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.space4)
    ) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = labelColor
        )
    }
}