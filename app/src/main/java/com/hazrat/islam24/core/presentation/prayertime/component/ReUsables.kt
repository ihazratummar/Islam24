package com.hazrat.islam24.core.presentation.prayertime.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.R
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
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface

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
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,

                )
        } else {
            CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        },
        elevation = if (isPrayerTime) {
            CardDefaults.cardElevation(
                defaultElevation = dimens.size10
            )
        } else {
            CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                contentDescription = "Icon",
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

