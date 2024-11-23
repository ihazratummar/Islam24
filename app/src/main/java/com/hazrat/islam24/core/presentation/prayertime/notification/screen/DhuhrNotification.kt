package com.hazrat.islam24.core.presentation.prayertime.notification.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.prayertime.component.ToggleNotification
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationState

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhuhrNotification(
    modifier: Modifier = Modifier,
    notificationEvent: (NotificationEvent) -> Unit,
    onBackClick: () -> Unit,
    notificationState: NotificationState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.dhuhr_notification),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                        notificationEvent(NotificationEvent.RefreshNotificationState)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.backicon),
                            contentDescription = "BackClick"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        ToggleNotification(
            modifier = Modifier.padding(paddingValues),
            isCheck = notificationState.isDhuhrNotification,
            notificationEvent = { notificationEvent(NotificationEvent.ToggleDhuhrNotification) },
            notificationName = R.string.dhuhr_notification
        )
    }
}