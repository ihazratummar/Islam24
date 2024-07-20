package com.hazrat.islam24.core.presentation.prayertime.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
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
import com.hazrat.islam24.ui.theme.dimens


@Composable
fun PrayerTimeSettingCard(
    @DrawableRes icon: Int,
    text: String,
    subText: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.size8)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primaryContainer,
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = BorderStroke(MaterialTheme.dimens.size1, color = Color.Green)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(MaterialTheme.dimens.size8)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                modifier = Modifier.size(MaterialTheme.dimens.size30),
            )
            Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.size10)
            ) {
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))
                Text(
                    text = text,
                    style = MaterialTheme.typography.displaySmall,
                )
                if (subText != null) {
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
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
    isPrayerTime : Boolean,
    isNow: String
) {

    Card (
        modifier = Modifier
        .padding(horizontal = MaterialTheme.dimens.size10, vertical = MaterialTheme.dimens.size4 ),
        colors =  if (isPrayerTime) {
            CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,

            )
        }else{
            CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        },
        elevation = if (isPrayerTime) {
            CardDefaults.cardElevation(
                defaultElevation = MaterialTheme.dimens.size10
            )
        }else {
            CardDefaults.cardElevation(defaultElevation = 0.dp)
        }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size20),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "Icon",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(MaterialTheme.dimens.size30)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.size10))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
                    )
                }
                Text(
                    text = isNow,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
                )
            }
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = countDownText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.size40))
                    Text(
                        text = time,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
                    )
                }
                Text(
                    text = "",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = MaterialTheme.dimens.size10)
                )
            }
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
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size3))
            Text(
                text = hrDate,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}


//@Preview
//@Composable
//fun PrayerTimeCardPreview() {
//    Islam24Theme {
//        PrayerTimeCard(icon = R.drawable.athkar,
//            text = "Fajr",
//            time = "04:59",
//            onClick = {}
//        )
//    }
//
//}

