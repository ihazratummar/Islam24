package com.hazrat.islam24.auth.presentation.appSetting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.presentation.appSetting.component.SelectLanguageDialog
import com.hazrat.islam24.auth.presentation.appSetting.component.SelectThemeDialog
import com.hazrat.islam24.auth.presentation.appSetting.component.logOutCardShimmerEffect
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.Languages
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingScreen(
    navController: NavController,
    authState: AuthState,
    appSettingState: AppSettingState,
    appSettingEvent: (AppSettingEvent) -> Unit,
    appSettingStateTheme: AppSettingState,
    appSettingEventTheme: (AppSettingEvent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
        containerColor = MaterialTheme.colorScheme.surfaceDim,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceDim
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.setting),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.backicon),
                            contentDescription = null
                        )
                    }
                })
        }
    ) { paddingValues ->
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
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.app_setting),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(
                                horizontal = dimens.size15,
                                vertical = dimens.size10
                            )
                        )
                        SettingItemCard(
                            painter = painterResource(id = R.drawable.language),
                            text = stringResource(id = R.string.language),
                            onClick = {
                                appSettingEvent(AppSettingEvent.ClickLanguageDialog)
                            },
                            selectedText = when (appSettingState.currentLanguage) {
                                Languages.ENGLISH -> stringResource(R.string.english)
                                Languages.BENGALI -> stringResource(R.string.bengali)
                            }
                        )
                        SettingItemCard(
                            painter = painterResource(id = R.drawable.theme_light_dark),
                            text = stringResource(R.string.theme),
                            onClick = {
                                appSettingEventTheme(AppSettingEvent.ClickThemeDialog)
                            },
                            selectedText = when (appSettingStateTheme.currentTheme) {
                                Themes.DARK -> stringResource(R.string.dark)
                                Themes.LIGHT -> stringResource(R.string.light)
                            }
                        )
                        SettingItemCard(
                            painter = painterResource(id = R.drawable.sysemsetting),
                            text = stringResource(R.string.system_seeting),
                            onClick = {
                                appSettingEvent(AppSettingEvent.OpenAppSetting)
                            }
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(dimens.size10))
                when (authState) {
                    is AuthState.Authenticated , is AuthState.Error -> {
                        SettingItemCard(
                            painter = painterResource(id = R.drawable.logout),
                            text = stringResource(R.string.logout),
                            onClick = {
                                appSettingEvent(AppSettingEvent.SignOut)
                            },
                            iconColor = MaterialTheme.colorScheme.error,
                            cardContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    }

                    AuthState.Loading -> {
                        SettingItemCard(
                            modifier = Modifier.logOutCardShimmerEffect(),
                            painter = painterResource(id = R.drawable.logout),
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
        }
        if (appSettingState.isLanguageDialogOpen) {
            SelectLanguageDialog(appSettingEvent)
        }

        if (appSettingStateTheme.isThemeDialogOpen) {
            SelectThemeDialog(appSettingEventTheme)
        }
    }
}


@Composable
private fun SettingItemCard(
    modifier: Modifier = Modifier,
    painter: Painter,
    text: String,
    onClick: () -> Unit = {},
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
    cardContainerColor: Color = Color.Transparent,
    selectedText: String = ""
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
                    modifier = Modifier.padding(horizontal = dimens.size10),
                    painter = painter,
                    contentDescription = text,
                    tint = iconColor
                )
                Spacer(modifier = Modifier.width(dimens.size30))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier,
                    text = selectedText,
                    style = MaterialTheme.typography.titleMedium,
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