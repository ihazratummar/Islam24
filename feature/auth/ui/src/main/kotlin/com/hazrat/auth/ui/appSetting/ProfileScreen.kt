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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                label = "Dark Mode",
                statusText = if (state.toggleTheme) "On" else "Off",
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
                label = "Notifications",
                statusText = if (state.isMasterNotificationEnabled) "Enabled" else "Disabled",
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
                label = "Haptic Feedback",
                statusText = if (state.isHapticFeedbackEnabled) "Enabled" else "Disabled",
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

        val appMetaSettings = listOf(
            AppMetaDataSettings(
                icon = R.drawable.outlinstar,
                settingName = "About Islam 24",
                trailingIcon = R.drawable.arrowright,
                onClick = {
                    hapticFeedbacks(
                        isEnable = isHapticFeedback,
                        hapticFeedback = hapticFeedback
                    )
                    onAboutUsClick("https://islam24.app/about-us", "About Islam 24")
                }
            ),
            AppMetaDataSettings(
                icon = R.drawable.share,
                settingName = "Share App",
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
                settingName = "Rate Us",
                trailingIcon = R.drawable.arrowright,
                label = "Share Your Valuable Feedback",
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
                settingName = "Support Islam 24",
                label = "Keep us free",
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
                                count = state.totalPrayersLogged.toString(),
                                label = "Prayers"
                            )
                            MetricItem(
                                count = state.prayerStreak.toString(),
                                label = "• Streak",
                                labelColor = Color(0xFFFF8E00)
                            )
                            MetricItem(
                                count = state.totalBookmarkedAyahs.toString(),
                                label = "Bookmarks"
                            )
                        }
                    }
                }
            }

            // PREFERENCES Section
            item {
                AppSection(
                    sectionTitle = "PREFERENCES"
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
                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            }
                        }
                    }
                }
            }

            // APP Section
            item {
                AppSection(
                    sectionTitle = "APP"
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
                    sectionTitle = "LEGAL"
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
                            settingText = "Legal Docs",
                            trailingIcon = R.drawable.arrowright,
                            onClick = onPolicyClick,
                        )
                    }
                }
            }

            if (state.isLoggedIn){
                item {
                    AppSection(
                        sectionTitle = "ACCOUNT"
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
                                settingText = "Sign Out",
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
                        text = "Made with love ❤️ for the Ummah",
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
            IslamicLoadingScreen(subtitle = "Signing out...")
        }

        if (state.isRatingDialogOpen) {
            RatingBottomSheet(
                appSettingEvent,
                hapticFeedback = {
                    hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                }
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