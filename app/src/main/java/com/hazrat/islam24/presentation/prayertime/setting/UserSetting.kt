package com.hazrat.islam24.presentation.prayertime.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.data.entity.PrayerSettingEntity
import com.hazrat.islam24.presentation.mainActivity.MainViewModel
import com.hazrat.islam24.presentation.prayertime.PrayerTimeViewModel
import com.hazrat.islam24.presentation.prayertime.component.MethodSelectionDialog
import com.hazrat.islam24.presentation.prayertime.component.PrayerTimeSettingCard
import com.hazrat.islam24.presentation.prayertime.component.SchoolSelectionDialog
import com.hazrat.islam24.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSetting(
    navController: NavController,
    settingViewModel: UserSettingViewModel = hiltViewModel(),
    prayerTimeViewModel: MainViewModel = hiltViewModel()
) {

    fun openMethodSelectionDialog() {
        settingViewModel.openMethodSelectionDialog()
    }

    val methodList = settingViewModel.methodList.collectAsState()

    val prayerTimes by prayerTimeViewModel.prayerTimes.collectAsState()
    val prayerTimeEntities = prayerTimes.getOrNull(0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PRAYER SETTING",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(MaterialTheme.dimens.size5)
                    )
                },
                colors = TopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = colorResource(id = R.color.text),
                    titleContentColor = colorResource(id = R.color.text),
                    actionIconContentColor = colorResource(id = R.color.text)
                ),
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()
                            }
                            .padding(MaterialTheme.dimens.size5)
                    )
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
        ) {
            item {
                HorizontalDivider(
                    thickness = MaterialTheme.dimens.size1,
                    color = colorResource(id = R.color.primary)
                )
            }
            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size20))
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
                } else {
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
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size20))
            }
            item {
                val school = prayerTimeEntities?.school
                if (school != null) {
                    PrayerTimeSettingCard(
                        icon = R.drawable.duaicon,
                        text = "Select Madhab",
                        subText = school,
                        onClick = {
                            settingViewModel.showSchoolSelectionDialog = true
                        }
                    )
                } else {
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
