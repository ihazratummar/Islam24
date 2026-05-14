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
import com.hazrat.model.PrayerTimeModel
import com.hazrat.prayer.ui.component.JuristicSelectionDialog
import com.hazrat.prayer.ui.component.PrayerCalculationDialog
import com.hazrat.prayer.ui.component.PrayerSettingCard
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerSetting(
    prayerTimeEntity: List<PrayerTimeModel>,
    onBackClick: () -> Unit,
    state: PrayerSettingState,
    event: (PrayerSettingEvent) -> Unit
) {

    val prayerTimeEntities = prayerTimeEntity.getOrNull(0)
    val school = prayerTimeEntities?.school

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
            PrayerSettingCard(
                text = stringResource(R.string.calculation_method),
                methodID = prayerTimeEntities?.methodName,
                method = "${prayerTimeEntities?.methodFajrParam}° / ${prayerTimeEntities?.methodIshaParam}°",
                onClick = {
                    event(PrayerSettingEvent.OpenCalculationDialog)
                }
            )
            PrayerSettingCard(
                text = stringResource(R.string.juristic_method),
                methodID = school,
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
