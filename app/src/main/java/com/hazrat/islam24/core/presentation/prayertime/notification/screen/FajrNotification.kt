package com.hazrat.islam24.core.presentation.prayertime.notification.screen

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
import androidx.compose.ui.res.stringResource
import com.hazrat.ui.R
import com.hazrat.islam24.core.presentation.common.BasicTopBar
import com.hazrat.islam24.core.presentation.prayertime.component.AzanList
import com.hazrat.islam24.core.presentation.prayertime.component.ToggleNotification
import com.hazrat.islam24.core.presentation.prayertime.component.listOfFajrAzan
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent.OnDefaultNotificationClick
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationState
import com.hazrat.ui.theme.dimens
import com.hazrat.datastore.NotificationType
import com.hazrat.datastore.PrayerName

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FajrNotification(
    modifier: Modifier = Modifier,
    notificationEvent: (NotificationEvent) -> Unit,
    onBackClick: () -> Unit,
    notificationState: NotificationState
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size30))
            BasicTopBar(
                topBarTitle = stringResource(R.string.fajr_notification),
                onBackClick = {
                    onBackClick.invoke()
                    notificationEvent(NotificationEvent.RefreshNotificationState)
                }
            )
            ToggleNotification(
                modifier = Modifier,
                isCheck = notificationState.isFajrNotification,
                notificationEvent = { notificationEvent(NotificationEvent.ToggleFajrNotification) },
                notificationName = R.string.fajr_notification
            )
            Spacer(Modifier.height(dimens.size20))
            AnimatedVisibility(notificationState.isFajrNotification) {
                AzanList(
                    onAzanPlayClick = { index, azanName, azanUrl ->
                        if (!notificationState.isAzanPlaying[index]) {
                            notificationEvent(NotificationEvent.OnAzanPlayClick(fileName = azanName, aazanIndex =  index, azanUrl = azanUrl))
                        } else {
                            notificationEvent(NotificationEvent.StopAzan)
                        }
                    },
                    isAzanPlaying = notificationState.isAzanPlaying,
                    onAzanClick = {url, name->
                        notificationEvent(NotificationEvent.OnFajrAzanClick(azanUrl = url, fileName =  name))
                    },
                    listOfAzan = listOfFajrAzan,
                    onSilentNotificationClick = {
                        notificationEvent(
                            NotificationEvent.OnSilentNotificationClick(
                                PrayerName.FAJR,
                                NotificationType.SILENT
                            )
                        )
                    },
                    onDefaultNotificationClick = {
                        notificationEvent(
                            OnDefaultNotificationClick(
                                PrayerName.FAJR,
                                NotificationType.DEFAULT
                            )
                        )
                    },
                    selectedOption = notificationState.selectedFajrAzan,
                    onOptionSelected = {
                        notificationEvent(NotificationEvent.SelectFajrAzanOption(it))
                    }
                )
            }
        }
        // 🔴 Disable background clicks when downloading
        if (notificationState.isAzanDownloading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f)) // Dim background
                    .clickable(enabled = true, onClick = {}) // Consume all touch events
            )

            CircularProgressIndicator()
        }
    }
}


