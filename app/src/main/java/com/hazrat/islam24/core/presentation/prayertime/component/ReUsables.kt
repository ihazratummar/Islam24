package com.hazrat.islam24.core.presentation.prayertime.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.prayertime.notification.NotificationEvent
import com.hazrat.islam24.ui.theme.dimens


@Composable
fun PrayerSettingCard(
    text: String,
    methodID: String?,
    method: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size10, vertical = dimens.size4)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer

        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = dimens.size20, vertical = dimens.size10),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.width(dimens.size8))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )
            if (methodID != null) {
                Text(
                    text = methodID,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            if (method != null) {
                Text(
                    text = method,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }

    }
}


@Composable
fun PrayerTimeCard(
    @DrawableRes icon: Int,
    text: String,
    time: String,
    countDownText: String,
    isPrayerTime: Boolean,
    onClick: () -> Unit = {},
    isNotification: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = dimens.size10,
                vertical = dimens.size4
            ),
        colors = if (isPrayerTime) {
            CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary.copy(0.8f),
            )
        } else {
            CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimens.size10,
                    vertical = dimens.size30
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.size5)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(dimens.size30)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = dimens.size10)
            )

            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = countDownText,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = dimens.size10)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = time,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = dimens.size10)
            )
            Spacer(modifier = Modifier.width(dimens.size10))
            Icon(
                painter = if (isNotification) painterResource(R.drawable.notificationonn) else painterResource(R.drawable.notificationoff),
                contentDescription = "Notification Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(dimens.size20)
            )
        }
    }
}



@Composable
fun PrayerDateCard(
    modifier: Modifier = Modifier,
    enDate: String,
    hrDate: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = enDate,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(dimens.size3))
            Text(
                text = hrDate,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}


@Composable
fun ToggleNotification(
    modifier: Modifier,
    isCheck: Boolean,
    notificationEvent: () -> Unit,
    notificationName: Int
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.size10),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(notificationName))

                Switch(
                    checked = isCheck,
                    onCheckedChange = {notificationEvent.invoke()},
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        checkedBorderColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        }
    }
}




@Composable
fun AzanList(
    modifier: Modifier = Modifier,
    onAzanPlayClick: (Int, Int) -> Unit,
    isAzanPlaying: List<Boolean>,
    onAzanClick: (Int) -> Unit,
    listOfAzan: List<Int>,
    onDefaultNotificationClick: () -> Unit,
    onSilentNotificationClick: () -> Unit,
    selectedOption: Int = 0,
    onOptionSelected: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onSilentNotificationClick()
                    onOptionSelected(0)
                },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedOption == 0) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.size30, horizontal = dimens.size10)
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.size30),
                        painter = painterResource(R.drawable.bell_off),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(dimens.size20))
                    Text("Silent")
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onDefaultNotificationClick()
                    onOptionSelected(1)
                },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedOption == 1) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.size30, horizontal = dimens.size10)
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.size30),
                        painter = painterResource(R.drawable.bell_ringing),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(dimens.size20))
                    Text("Default Notification")
                }
            }
        }
        itemsIndexed(listOfAzan) { index, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onAzanClick(item)
                    onOptionSelected(index + 2)
                },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedOption == index + 2) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.size30, horizontal = dimens.size10),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.size40),
                        painter = painterResource(R.drawable.volume),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(dimens.size20))
                    Text(text = "Azan ${index + 1}")
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .size(dimens.size30)
                            .clickable {
                                if (index < isAzanPlaying.size) {
                                    onAzanPlayClick(index, item)
                                }
                            },
                        painter = if (index < isAzanPlaying.size && !isAzanPlaying[index]) {
                            painterResource(R.drawable.play)
                        } else {
                            painterResource(R.drawable.stop)
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(dimens.size5))

                }
            }
        }

    }
}

val listOfAzan = listOf(
    R.raw.azan2,
    R.raw.azan3,
    R.raw.azan4,
    R.raw.azan5,
    R.raw.azan6,
    R.raw.azan7,
    R.raw.azan8,
    R.raw.azan9,
    R.raw.azan10,
)

val listOfFajrAzan = listOf(
    R.raw.fajr1,
    R.raw.fajr2
)

