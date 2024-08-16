package com.hazrat.islam24.core.presentation.prayertime.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.presentation.prayertime.component.JuristicSelectionDialog
import com.hazrat.islam24.core.presentation.prayertime.component.PrayerCalculationDialog
import com.hazrat.islam24.core.presentation.prayertime.component.PrayerSettingCard
import com.hazrat.islam24.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerSetting(
    prayerTimeEntity: List<PrayerTimeEntity>,
    onBackClick: () -> Unit,
    state: PrayerSettingState,
    event: (PrayerSettingEvent) -> Unit
) {

    val prayerTimeEntities = prayerTimeEntity.getOrNull(0)
    val school = prayerTimeEntities?.school

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.prayer_setting),
                        modifier = Modifier.padding(dimens.size5)
                    )
                },
                navigationIcon = {
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.backicon),
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable {
                                onBackClick()
                            }
                            .padding(dimens.size5)
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
                thickness = dimens.size1,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(dimens.size20))
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
}
