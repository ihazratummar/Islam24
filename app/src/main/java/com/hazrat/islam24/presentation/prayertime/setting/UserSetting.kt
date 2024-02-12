package com.hazrat.islam24.presentation.prayertime.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.data.prayertime.PrayerSettingEntity
import com.hazrat.islam24.presentation.Dimens.SpSize25
import com.hazrat.islam24.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.presentation.prayertime.component.MethodSelectionDialog
import com.hazrat.islam24.presentation.prayertime.component.PrayerTimeSettingCard
import com.hazrat.islam24.presentation.prayertime.component.SchoolSelectionDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSetting(
    navController: NavController,
    settingViewModel: UserSettingViewModel = hiltViewModel(),
    prayerTimeViewModel: PrayerTimeViewModel = hiltViewModel()
) {

    fun openMethodSelectionDialog() {
        settingViewModel.openMethodSelectionDialog()
    }
    val methodList = settingViewModel.methodList.collectAsState()

    val prayerTimes by prayerTimeViewModel.prayerTimes.collectAsState()
    val prayerTimeEntities = prayerTimes.getOrNull(0)

    LazyColumn(
        modifier = Modifier.statusBarsPadding()
    ) {
        item {
            TopBarWithBack(navController)
        }
        item {
            Divider()
        }
        item {
            val methodName = prayerTimeEntities?.methodName
            if (methodName != null) {
                PrayerTimeSettingCard(
                    icon = R.drawable.athkar,
                    text = "Prayer Method",
                    subText = methodName,
                    onClick = {
                        openMethodSelectionDialog()
                    }
                )
            }else{
                PrayerTimeSettingCard(
                    icon = R.drawable.athkar,
                    text = "Prayer Method",
                    subText = null,
                    onClick = {
                        openMethodSelectionDialog()
                    }
                )
            }
        }
        item {
            val school = prayerTimeEntities?.school
            if (school != null){
                PrayerTimeSettingCard(
                    icon = R.drawable.duaicon,
                    text = "Select Madhab",
                    subText = school,
                    onClick = {
                        settingViewModel.showSchoolSelectionDialog = true
                    }
                )
            }else{
                PrayerTimeSettingCard(
                    icon = R.drawable.duaicon,
                    text = "Select Madhab",
                    subText = null,
                    onClick = {
                        settingViewModel.showSchoolSelectionDialog = true
                    }
                )
            }

        }
    }

    if (settingViewModel.showMethodSelectionDialog) {
        MethodSelectionDialog(
            showMethodSelectionDialog = settingViewModel.showMethodSelectionDialog,
            onMethodSelected = { method ->
                settingViewModel.selectedMethod = method
                settingViewModel.insertMethod(
                    PrayerSettingEntity(
                        method.method,
                        settingViewModel.selectedSchool.number
                    )
                )
                settingViewModel.showMethodSelectionDialog = false
            },
            onDismiss = { settingViewModel.showMethodSelectionDialog = false }
        )
    }

    if (settingViewModel.showSchoolSelectionDialog) {
        SchoolSelectionDialog(
            showSchoolSelectionDialog = settingViewModel.showSchoolSelectionDialog,
            onSchoolSelected = { school ->
                settingViewModel.selectedSchool = school
                settingViewModel.insertMethod(
                    PrayerSettingEntity(settingViewModel.selectedMethod.method, school.number)
                )
                settingViewModel.showSchoolSelectionDialog = false
            },
            onDismiss = { settingViewModel.showSchoolSelectionDialog = false }
        )
    }
}

@Composable
private fun TopBarWithBack(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
                    .size(30.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Settings", style = TextStyle(
                    fontSize = SpSize25,
                    color = Color.White
                )
            )
        }
    }
}