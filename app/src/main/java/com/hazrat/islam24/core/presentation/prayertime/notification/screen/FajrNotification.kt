package com.hazrat.islam24.core.presentation.prayertime.notification.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.common.BasicTopBar
import com.hazrat.islam24.core.presentation.prayertime.component.AzanList
import com.hazrat.islam24.core.presentation.prayertime.component.ToggleNotification
import com.hazrat.islam24.core.presentation.prayertime.component.listOfAzan
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent.OnDefaultNotificationClick
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationState
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.datastore.NotificationType
import com.hazrat.islam24.util.datastore.PrayerName

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
        modifier = modifier.fillMaxSize()
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
                    onAzanPlayClick = { index, item ->
                        if (!notificationState.isAzanPlaying[index]) {
                            notificationEvent(NotificationEvent.OnAzanPlayClick(item, index))
                        } else {
                            notificationEvent(NotificationEvent.StopAzan)
                        }
                    },
                    isAzanPlaying = notificationState.isAzanPlaying,
                    onAzanClick = {
                        notificationEvent(NotificationEvent.OnFajrAzanClick(it))
                    },
                    listOfAzan = listOfAzan,
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
    }
}


