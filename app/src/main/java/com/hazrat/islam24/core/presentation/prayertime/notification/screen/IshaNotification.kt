package com.hazrat.islam24.core.presentation.prayertime.notification.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.common.BasicTopBar
import com.hazrat.islam24.core.presentation.prayertime.component.AzanList
import com.hazrat.islam24.core.presentation.prayertime.component.ToggleNotification
import com.hazrat.islam24.core.presentation.prayertime.component.listOfAzan
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationState
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.datastore.NotificationType
import com.hazrat.islam24.util.datastore.PrayerName

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IshaNotification(
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
                topBarTitle = stringResource(R.string.isha_notification),
                onBackClick = {
                    onBackClick.invoke()
                    notificationEvent(NotificationEvent.RefreshNotificationState)
                }
            )

            ToggleNotification(
                modifier = Modifier,
                isCheck = notificationState.isIshaNotification,
                notificationEvent = { notificationEvent(NotificationEvent.ToggleIshaNotification) },
                notificationName = R.string.isha_notification
            )

            AnimatedVisibility(notificationState.isIshaNotification) {
                AzanList(
                    onAzanPlayClick = {index, azanName, azanUrl  ->
                        if (!notificationState.isAzanPlaying[index]){
                            notificationEvent(NotificationEvent.OnAzanPlayClick(fileName = azanName, aazanIndex =  index, azanUrl = azanUrl))
                        }else{
                            notificationEvent(NotificationEvent.StopAzan)
                        }
                    },
                    isAzanPlaying = notificationState.isAzanPlaying,
                    onAzanClick = {url, fileName ->
                        notificationEvent(NotificationEvent.OnIshaAzanClick(azanUrl = url, fileName = fileName))
                    },
                    listOfAzan = listOfAzan,
                    onSilentNotificationClick = {notificationEvent(NotificationEvent.OnSilentNotificationClick(PrayerName.ISHA, NotificationType.SILENT))},
                    onDefaultNotificationClick = { notificationEvent(NotificationEvent.OnDefaultNotificationClick(PrayerName.ISHA, NotificationType.DEFAULT))},
                    selectedOption = notificationState.selectedIshaAzan,
                    onOptionSelected = {
                        notificationEvent(NotificationEvent.SelectIshaAzanOption(it))
                    }
                )
            }
        }

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