package com.hazrat.prayer.ui.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hazrat.model.prayersettingmodel.JuristicMethod
import com.hazrat.model.prayersettingmodel.prayerMethods
import com.hazrat.prayer.ui.component.JuristicSelectionDialog
import com.hazrat.prayer.ui.component.PrayerCalculationDialog
import com.hazrat.prayer.ui.component.PrayerSettingCard
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerSetting(
    onBackClick: () -> Unit,
    state: PrayerSettingState,
    event: (PrayerSettingEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(dimens.space32))
            BasicTopBar(
                topBarTitle = stringResource(R.string.prayer_setting),
                onBackClick = { onBackClick.invoke() }
            )
            HorizontalDivider(
                thickness = dimens.divider,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(dimens.space20))
            val calculationMethod = prayerMethods[state.calculationMethod]
            PrayerSettingCard(
                text = stringResource(R.string.calculation_method),
                methodID = calculationMethod.name,
                method = "${calculationMethod.farjAngle}°/${calculationMethod.ishaAngle}° ● ${calculationMethod.region}",
                onClick = {
                    event(PrayerSettingEvent.OpenCalculationDialog)
                }
            )
            PrayerSettingCard(
                text = stringResource(R.string.juristic_method),
                methodID = JuristicMethod.entries[state.juristic].name,
                method = null,
                onClick = {
                    event(PrayerSettingEvent.OpenJuristicDialog)
                }
            )
        }
        if (state.isCalculationDialogOpen) {
            PrayerCalculationDialog(
                onMethodSelected = { method ->
                    event(PrayerSettingEvent.CalculationChanged(method.method))
                },
                onDismiss = { event(PrayerSettingEvent.OpenCalculationDialog) }
            )
        }
        if (state.isJuristicDialogOpen) {
            JuristicSelectionDialog(
                onJuristicSelected = { juristic ->
                    event(PrayerSettingEvent.JuristicChanged(juristic.number))
                },
                onDismiss = { event(PrayerSettingEvent.OpenJuristicDialog) }
            )
        }
        if (state.isRefresh){
            LinearProgressIndicator(
                modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
            )
        }
    }

}
