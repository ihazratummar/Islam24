package com.hazrat.prayer.ui.notification

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.hazrat.datastore.NotificationType
import com.hazrat.datastore.PrayerName
import com.hazrat.permission.PermissionTypes
import com.hazrat.permission.isPermissionGranted
import com.hazrat.permission.rememberPermissionRequester
import com.hazrat.prayer.ui.component.AzanData
import com.hazrat.prayer.ui.component.AzanList
import com.hazrat.prayer.ui.component.ToggleNotification
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.openAppSettings

/**
 * @author Hazrat Ummar Shaikh
 *
 * A unified prayer notification screen that replaces the individual FajrNotification,
 * DhuhrNotification, AsrNotification, MaghribNotification, and IshaNotification composables.
 *
 * Handles notification permission requests internally.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerNotificationScreen(
        modifier: Modifier = Modifier,
        prayerName: PrayerName,
        @StringRes titleRes: Int,
        isNotificationEnabled: Boolean,
        onToggleNotification: () -> Unit,
        onAzanClick: (url: String, fileName: String) -> Unit,
        selectedAzan: Int,
        onAzanOptionSelected: (Int) -> Unit,
        azanList: List<AzanData>,
        notificationEvent: (NotificationEvent) -> Unit,
        onBackClick: () -> Unit,
        notificationState: NotificationState
) {
    val context = LocalContext.current
    val requestNotificationPermission =
            rememberPermissionRequester(
                    permission = PermissionTypes.NOTIFICATION!!,
                    onGranted = { onToggleNotification() },
                    onDenied = {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                    },
                    onPermissionDenied = { openAppSettings(context = context) }
            )

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(dimens.size30))
            BasicTopBar(
                    topBarTitle = stringResource(titleRes),
                    onBackClick = {
                        onBackClick.invoke()
                        notificationEvent(NotificationEvent.RefreshNotificationState)
                    }
            )
            ToggleNotification(
                    modifier = Modifier,
                    isCheck = isNotificationEnabled,
                    notificationEvent = {
                        if (isPermissionGranted(
                                        context = context,
                                        permission = PermissionTypes.NOTIFICATION
                                )
                        ) {
                            onToggleNotification()
                        } else {
                            requestNotificationPermission()
                        }
                    },
                    notificationName = titleRes
            )
            Spacer(Modifier.height(dimens.size20))
            AnimatedVisibility(isNotificationEnabled) {
                AzanList(
                        onAzanPlayClick = { index, azanName, azanUrl ->
                            if (!notificationState.isAzanPlaying[index]) {
                                notificationEvent(
                                        NotificationEvent.OnAzanPlayClick(
                                                fileName = azanName,
                                                aazanIndex = index,
                                                azanUrl = azanUrl
                                        )
                                )
                            } else {
                                notificationEvent(NotificationEvent.StopAzan)
                            }
                        },
                        isAzanPlaying = notificationState.isAzanPlaying,
                        onAzanClick = { url, fileName -> onAzanClick(url, fileName) },
                        listOfAzan = azanList,
                        onSilentNotificationClick = {
                            notificationEvent(
                                    NotificationEvent.OnSilentNotificationClick(
                                            prayerName,
                                            NotificationType.SILENT
                                    )
                            )
                        },
                        onDefaultNotificationClick = {
                            notificationEvent(
                                    NotificationEvent.OnDefaultNotificationClick(
                                            prayerName,
                                            NotificationType.DEFAULT
                                    )
                            )
                        },
                        selectedOption = selectedAzan,
                        onOptionSelected = { onAzanOptionSelected(it) }
                )
            }
        }
        // 🔴 Disable background clicks when downloading
        if (notificationState.isAzanDownloading) {
            Box(
                    modifier =
                            Modifier.fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f)) // Dim background
                                    .clickable(
                                            enabled = true,
                                            onClick = {}
                                    ) // Consume all touch events
            )

            CircularProgressIndicator()
        }
    }
}
