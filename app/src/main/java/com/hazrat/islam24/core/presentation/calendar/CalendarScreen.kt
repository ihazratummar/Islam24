package com.hazrat.islam24.core.presentation.calendar

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
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.core.presentation.common.BasicTopBarWithAction
import com.hazrat.islam24.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GregorianCalendarScreen(
    onBackClick:() -> Unit,
    gregorianToHijriEntity: List<GregorianToHijriEntity>
) {
    var toggleCalendar by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicTopBarWithAction(
                topBarTitle = "Hijri Calendar",
                onBackClick = {onBackClick.invoke()},
                actionIcon = painterResource(R.drawable.calendar),
                onActionClick = { toggleCalendar = !toggleCalendar }
            )
            LazyColumn(
                modifier = Modifier
            ) {
                item {
                    if (!toggleCalendar) {
                        HijriCalendarHomeScreen(
                            gregorianToHijriEntity = gregorianToHijriEntity
                        )
                    } else {
                        GregorianCalendarScreen()
                    }
                }
            }
        }
    }
}