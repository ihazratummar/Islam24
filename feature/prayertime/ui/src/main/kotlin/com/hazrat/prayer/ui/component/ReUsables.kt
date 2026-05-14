package com.hazrat.prayer.ui.component

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
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens


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
            .padding(horizontal = dimens.space12, vertical = dimens.space4)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer

        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = dimens.space20, vertical = dimens.space12),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.width(dimens.space8))
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
                horizontal = dimens.space12,
                vertical = dimens.space4
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
                    horizontal = dimens.space12,
                    vertical = dimens.space32
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.space4)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(dimens.iconLg)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = dimens.space12)
            )

            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = countDownText,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = dimens.space12)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = time,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = dimens.space12)
            )
            Spacer(modifier = Modifier.width(dimens.space12))
            Icon(
                painter = if (isNotification) painterResource(R.drawable.notificationonn) else painterResource(R.drawable.notificationoff),
                contentDescription = "Notification Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(dimens.iconMd)
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
            Spacer(modifier = Modifier.height(dimens.space4))
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
                    .padding(dimens.space12),
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
    onAzanPlayClick: (Int, String, String) -> Unit,
    isAzanPlaying: List<Boolean>,
    onAzanClick: (String, String) -> Unit,
    listOfAzan: List<AzanData>,
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
                        .padding(vertical = dimens.space32, horizontal = dimens.space12)
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.iconLg),
                        painter = painterResource(R.drawable.bell_off),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(dimens.space20))
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
                        .padding(vertical = dimens.space32, horizontal = dimens.space12)
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.iconLg),
                        painter = painterResource(R.drawable.bell_ringing),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(dimens.space20))
                    Text("Default Notification")
                }
            }
        }
        itemsIndexed(listOfAzan) { index, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onAzanClick(item.url, item.name)
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
                        .padding(vertical = dimens.space32, horizontal = dimens.space12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(dimens.iconXl),
                        painter = painterResource(R.drawable.volume),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(dimens.space20))
                    Text(text = "Azan ${index + 1}")
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .size(dimens.iconLg)
                            .clickable {
                                if (index < isAzanPlaying.size) {
                                    onAzanPlayClick(index, item.name, item.url)
                                }
                            },
                        painter = if (index < isAzanPlaying.size && !isAzanPlaying[index]) {
                            painterResource(R.drawable.play)
                        } else {
                            painterResource(R.drawable.stop)
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(dimens.space4))

                }
            }
        }

    }
}

val listOfAzan = listOf(
    AzanData(
        name = "azan1",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan1.mp3"
    ),
    AzanData(
        name = "azan2",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan2.mp3"
    ),
    AzanData(
        name = "azan3",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan3.mp3"
    ),
    AzanData(
        name = "azan4",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan4.mp3"
    ),
    AzanData(
        name = "azan5",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan5.mp3"
    ),
    AzanData(
        name = "azan6",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan6.mp3"
    ),
    AzanData(
        name = "azan7",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan7.mp3"
    ),
    AzanData(
        name = "azan8",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan8.mp3"
    ),
    AzanData(
        name = "azan9",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/azan9.mp3"
    )
)

val listOfFajrAzan = listOf(
    AzanData(
        name = "fajr1",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/fajr1.mp3"
    ),
    AzanData(
        name = "fajr2",
        url = "https://raw.githubusercontent.com/ihazratummar/azan/main/fajr2.mp3"
    )
)


data class AzanData(
    val name: String,
    val url: String
)
