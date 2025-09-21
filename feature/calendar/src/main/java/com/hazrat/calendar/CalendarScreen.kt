package com.hazrat.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hazrat.common.BasicTopBarWithAction
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens

@Composable
fun CalendarScreen(
    onBackClick: () -> Unit
) {
    var toggleCalendar by remember { mutableStateOf(false) }
    val topBarTitle = if (toggleCalendar) "Gregorian Calendar" else "Hijri Calendar"

    // compute painter in a local val to reduce possible FIR confusion
    val actionIcon = painterResource(id = R.drawable.calendar1)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicTopBarWithAction(
                topBarTitle = topBarTitle,
                onBackClick = { onBackClick.invoke() },
                actionIcon = actionIcon,
                onActionClick = { toggleCalendar = !toggleCalendar }
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    if (!toggleCalendar) {
                        HijriCalendarHomeScreen()
                    } else {
                        GregorianCalendarScreen()
                    }
                }
            }
        }
    }
}
