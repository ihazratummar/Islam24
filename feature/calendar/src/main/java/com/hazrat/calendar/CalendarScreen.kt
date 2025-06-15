package com.hazrat.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onBackClick:() -> Unit

) {
    var toggleCalendar by remember { mutableStateOf(false) }
    val topBarTitle = if (toggleCalendar) " Gregorian Calendar" else "Hijri Calendar"

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicTopBarWithAction(
                topBarTitle = topBarTitle,
                onBackClick = {onBackClick.invoke()},
                actionIcon = painterResource(R.drawable.calendar1),
                onActionClick = { toggleCalendar = !toggleCalendar }
            )
            LazyColumn(
                modifier = Modifier
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