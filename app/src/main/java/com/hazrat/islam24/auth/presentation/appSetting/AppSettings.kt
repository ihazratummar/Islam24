package com.hazrat.islam24.auth.presentation.appSetting

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.hazrat.ui.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.presentation.appSetting.component.SelectLanguageDialog
import com.hazrat.islam24.auth.presentation.appSetting.component.logOutCardShimmerEffect
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileEvent
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.islam24.auth.presentation.profileScreen.component.RatingBottomSheet
import com.hazrat.islam24.core.presentation.common.BackIcon
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.ui.theme.dimens
import com.hazrat.islam24.util.Languages
import com.hazrat.islam24.util.hapticFeedbacks
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun AppSettingScreen(
    authState: AuthState,
    appSettingEvent: (AppSettingEvent) -> Unit,
    appSettingState: AppSettingState,
    isHapticFeedback: Boolean = false,
    onPolicyClick: () -> Unit = {},
    onBackClick: () -> Unit,
) {

    val context = LocalContext.current
    val activity = LocalActivity.current as? MainActivity

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Error -> {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        message = authState.message,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
            }

            else -> Unit
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
                modifier = Modifier.padding(top = dimens.size30),
                title = {
                    Text(
                        text = stringResource(id = R.string.setting),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    BackIcon(
                        onBackClick = { onBackClick() }
                    )
                },
                windowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.statusBars)
            )
        }
    ) { paddingValues ->

        val listOfSocialTabs = listOf<SettingItemData>(
            SettingItemData(
                leadingIcon = painterResource(id = R.drawable.like),
                tabName = stringResource(id = R.string.rate),
                onClick = {
                    activity?.let {
                        appSettingEvent(AppSettingEvent.RateUs(activity))
                    }
                },
                trailingIcon = painterResource(id = R.drawable.chevron_right)
            ),
            SettingItemData(
                leadingIcon = painterResource(id = R.drawable.share),
                tabName = stringResource(id = R.string.invite_a_friend),
                onClick = {
                    appSettingEvent(AppSettingEvent.InviteFriend)
                },
                trailingIcon = painterResource(id = R.drawable.chevron_right)
            )

        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = dimens.size10)
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.app_setting),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(
                                horizontal = dimens.size15,
                                vertical = dimens.size10
                            )
                        )
                        SettingItemCard(
                            leadingIcon = painterResource(id = R.drawable.language),
                            text = stringResource(id = R.string.language),
                            onClick = {
                                appSettingEvent(AppSettingEvent.ClickLanguageDialog)
                            },
                            selectedText = when (appSettingState.currentLanguage) {
                                Languages.ENGLISH -> {
                                    stringResource(Languages.ENGLISH.getString())
                                }

                                Languages.BENGALI -> stringResource(Languages.BENGALI.getString())
                                null -> ""
                            }
                        )

                        SettingItemCard(
                            leadingIcon = painterResource(id = R.drawable.theme_light_dark),
                            text = stringResource(R.string.theme),
                            onClick = {
                                hapticFeedbacks(
                                    isEnable = isHapticFeedback,
                                    hapticFeedback = hapticFeedback
                                )
                                appSettingEvent(AppSettingEvent.ToggleTheme)
                            },
                            isSwitch = true,
                            isSwitchChecked = appSettingState.toggleTheme,
                        )

                        SettingItemCard(
                            leadingIcon = painterResource(id = R.drawable.vibrate),
                            text = stringResource(R.string.enable_haptic),
                            onClick = {
                                appSettingEvent(AppSettingEvent.HapticFeedbackClick)
                                hapticFeedbacks(
                                    isEnable = isHapticFeedback,
                                    hapticFeedback = hapticFeedback
                                )
                            },
                            isSwitch = true,
                            isSwitchChecked = appSettingState.isHapticFeedbackEnabled,
                        )

                        SettingItemCard(
                            leadingIcon = painterResource(id = R.drawable.sysemsetting),
                            text = stringResource(R.string.system_seeting),
                            onClick = {
                                appSettingEvent(AppSettingEvent.OpenAppSetting)
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(dimens.size20))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.legal),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(
                                horizontal = dimens.size15,
                                vertical = dimens.size10
                            )
                        )
                        SettingItemCard(
                            leadingIcon = painterResource(id = R.drawable.policy),
                            text = stringResource(R.string.policies),
                            onClick = {
                                onPolicyClick()
                            }
                        )

                    }
                }
            }

            item{
                Spacer(modifier = Modifier.height(dimens.size20))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column {
                        listOfSocialTabs.forEach {
                            SettingItemCard(
                                leadingIcon = it.leadingIcon,
                                text = it.tabName,
                                onClick = {it.onClick()},

                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(dimens.size40))
                when (authState) {
                    is AuthState.Authenticated, is AuthState.Error -> {
                        SettingItemCard(
                            leadingIcon = painterResource(id = R.drawable.logout),
                            text = stringResource(R.string.logout),
                            onClick = {
                                if (appSettingState.isHapticFeedbackEnabled) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                                appSettingEvent(AppSettingEvent.SignOut)
                                context.imageLoader.diskCache?.clear()
                                context.imageLoader.memoryCache?.clear()
                            },
                            iconColor = MaterialTheme.colorScheme.error,
                            cardContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    }

                    AuthState.Loading -> {
                        SettingItemCard(
                            modifier = Modifier.logOutCardShimmerEffect(),
                            leadingIcon = painterResource(id = R.drawable.logout),
                            text = stringResource(R.string.logout),
                            onClick = {
                                appSettingEvent(AppSettingEvent.SignOut)
                            },
                            iconColor = MaterialTheme.colorScheme.error,
                            cardContainerColor = Color.Transparent
                        )
                    }

                    else -> Unit
                }
            }

            item {
                val versionName = context.packageName?.let {
                    context.packageManager.getPackageInfo(
                        it,
                        PackageManager.GET_META_DATA
                    ).versionName
                }
                Text(text = "Islam24 $versionName")
            }


        }
        if (appSettingState.isLanguageDialogOpen) {
            SelectLanguageDialog(appSettingEvent)
        }
        if (appSettingState.isRatingDialogOpen) {
            RatingBottomSheet(
                appSettingEvent,
                hapticFeedback ={
                    hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                }
            )
        }
    }
}


data class SettingItemData(
    val leadingIcon: Painter,
    val tabName: String,
    val onClick: () -> Unit = {},
    val trailingIcon: Painter
)

@Composable
fun SettingItemCard(
    modifier: Modifier = Modifier,
    leadingIcon: Painter,
    text: String,
    onClick: () -> Unit = {},
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
    cardContainerColor: Color = Color.Transparent,
    selectedText: String = "",
    isSwitch: Boolean = false,
    isSwitchChecked: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = dimens.size5),
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size10)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.size10),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = dimens.size10)
                        .size(dimens.size40),
                    painter = leadingIcon,
                    contentDescription = text,
                    tint = iconColor
                )
                Spacer(modifier = Modifier.width(dimens.size30))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isSwitch) {
                    Switch(
                        modifier = Modifier
                            .padding(end = dimens.size20)
                            .size(dimens.size40)
                            .scale(0.6f),
                        checked = isSwitchChecked,
                        onCheckedChange = {
                            onClick()
                        }
                    )
                } else {
                    Text(
                        modifier = Modifier,
                        text = selectedText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(
                        modifier = Modifier.padding(horizontal = dimens.size10),
                        painter = painterResource(id = R.drawable.arrowright),
                        contentDescription = text,
                        tint = iconColor
                    )
                }

            }
        }
    }
}