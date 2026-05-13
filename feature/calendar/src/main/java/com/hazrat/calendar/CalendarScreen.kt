package com.hazrat.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eltohamy.materialhijricalendarview.CalendarDay
import com.hazrat.common.BasicTopBarWithAction
import com.hazrat.ui.R
import com.hazrat.ui.theme.DeepPineGreen
import com.hazrat.ui.theme.SoftCream
import com.hazrat.ui.theme.SpiritualGold
import com.hazrat.ui.theme.dimens
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onBackClick: () -> Unit
) {
    var toggleCalendar by remember { mutableStateOf(false) }
    val topBarTitle = if (toggleCalendar) "Gregorian Calendar" else "Hijri Calendar"
    val isDark = isSystemInDarkTheme()

    // Detail State
    var selectedHijriDate by remember { mutableStateOf<CalendarDay?>(null) }
    var selectedGregorianDate by remember { mutableStateOf<LocalDate?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val actionIcon = painterResource(id = R.drawable.calendar1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) MaterialTheme.colorScheme.background else SoftCream)
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.size15)
            ) {
                if (!toggleCalendar) {
                    HijriCalendarHomeScreen(
                        onDateSelected = { day ->
                            selectedHijriDate = day
                            selectedGregorianDate = null
                            showBottomSheet = true
                        }
                    )
                } else {
                    GregorianCalendarScreen(
                        onDateSelected = { date ->
                            selectedGregorianDate = date
                            selectedHijriDate = null
                            showBottomSheet = false
                        }
                    )
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = if (isDark) DeepPineGreen else Color.White
            ) {
                DateDetailContent(
                    hijriDay = selectedHijriDate,
                    gregorianDate = selectedGregorianDate
                )
            }
        }
    }
}

@Composable
fun DateDetailContent(
    hijriDay: CalendarDay?,
    gregorianDate: LocalDate?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selected Date",
            style = MaterialTheme.typography.titleMedium,
            color = SpiritualGold
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (hijriDay != null) {
            Text(
                text = "${hijriDay.day} ${getMonthName(hijriDay.month)} ${hijriDay.year} AH",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = if (isSystemInDarkTheme()) Color.White else DeepPineGreen
            )
        }

        if (gregorianDate != null) {
            val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
            Text(
                text = gregorianDate.format(formatter),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (isSystemInDarkTheme()) Color.White else DeepPineGreen
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Example Placeholder for Sunnah Fasts or Events
        Text(
            text = "Significant Islamic events or Sunnah fasts will appear here in the next update.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "Muharram"
        2 -> "Safar"
        3 -> "Rabi' al-awwal"
        4 -> "Rabi' ath-thani"
        5 -> "Jumada al-ula"
        6 -> "Jumada al-akhira"
        7 -> "Rajab"
        8 -> "Sha'ban"
        9 -> "Ramadan"
        10 -> "Shawwal"
        11 -> "Dhu al-Qi'dah"
        12 -> "Dhu al-Hijjah"
        else -> ""
    }
}
