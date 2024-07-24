package com.hazrat.islam24.core.presentation.prayertime.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.prayertime.component.JuristicSelectionDialog
import com.hazrat.islam24.core.presentation.prayertime.component.PrayerCalculationDialog
import com.hazrat.islam24.core.presentation.prayertime.component.PrayerSettingCard
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PrayerSetting(
    navController: NavController,
    prayerTimeViewModel: MainViewModel = hiltViewModel(),
    prayerSettingViewModel: PrayerSettingViewModel = hiltViewModel()
) {

    val state = prayerSettingViewModel.state.collectAsState()
    val event = prayerSettingViewModel::onEvent

    val prayerTimes by prayerTimeViewModel.prayerTimes.collectAsState()
    val prayerTimeEntities = prayerTimes.getOrNull(0)
    val school = prayerTimeEntities?.school

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.prayer_setting),
                        modifier = Modifier.padding(MaterialTheme.dimens.size5)
                    )
                },
                navigationIcon = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            HorizontalDivider(
                thickness = MaterialTheme.dimens.size1,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size20))
            PrayerSettingCard(
                text = stringResource(R.string.calculation_method),
                methodID = prayerTimeEntities?.methodName,
                method = "${prayerTimeEntities?.methodFajrParam}° / ${prayerTimeEntities?.methodIshaParam}°",
                onClick = {
                    event(PrayerSettingEvent.OpenCalculationDialog)
                }
            )

            PrayerSettingCard(
                text = "Juristic Method",
                methodID = school,
                method = null,
                onClick = {
                    event(PrayerSettingEvent.OpenJuristicDialog)
                }
            )

        }
    }
    if (state.value.isCalculationDialogOpen) {
        PrayerCalculationDialog(
            showMethodSelectionDialog = state.value.isCalculationDialogOpen,
            onMethodSelected = { method ->
                event(PrayerSettingEvent.CalculationChanged(method.method))
            },
            onDismiss = { event(PrayerSettingEvent.OpenCalculationDialog) }
        )
    }
    if (state.value.isJuristicDialogOpen) {
        JuristicSelectionDialog(
            showJuristicSelectionDialog = state.value.isJuristicDialogOpen,
            onJuristicSelected = { juristic ->
                event(PrayerSettingEvent.JuristicChanged(juristic.number))
            },
            onDismiss = { event(PrayerSettingEvent.OpenJuristicDialog) }
        )
    }
}
